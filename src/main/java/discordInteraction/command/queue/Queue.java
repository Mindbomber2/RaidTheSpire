package discordInteraction.command.queue;

import discordInteraction.Main;
import discordInteraction.command.QueuedCommandBase;

import java.util.ArrayList;
import java.util.Stack;

import static discordInteraction.util.Output.sendMessageToUser;

// This class is designed to be safe from multiple queries.
public class Queue<T extends QueuedCommandBase> {
    private final Object lock = new Object();
    private Stack<T> commands;

    public Queue(){
        commands = new Stack<T>();
    }

    public ArrayList<T> getCommands(){
        synchronized (lock){
            ArrayList<T> list = new ArrayList<T>();
            for(T command : commands)
                list.add(command);
            return list;
        }
    }

    public void add(T command){
        synchronized (lock){
            commands.add(command);
        }
    }

    public boolean hasAnotherCommand(){
        synchronized (lock){
            return commands != null && !commands.isEmpty();
        }
    }
    public T getNextCommand(){
        synchronized (lock){
            return commands.pop();
        }
    }
    public void refund(){
        synchronized (lock){
            while (hasAnotherCommand()) {
                QueuedCommandBase command = getNextCommand();
                if (Main.viewers.containsKey(command.getViewer())) {
                    Main.viewers.get(command.getViewer()).insertCard(command.getCard());
                    sendMessageToUser(command.getViewer(), "Your " + command.getCard().getName() +
                            " failed to cast before the battle ended, and has been refunded.");
                }
            }
        }
    }
    public void clear(){
        synchronized (lock){
            commands.clear();
        }
    }
}
