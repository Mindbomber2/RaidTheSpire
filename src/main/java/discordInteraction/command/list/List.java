package discordInteraction.command.list;

import discordInteraction.Main;
import discordInteraction.command.QueuedCommandBase;

import java.util.ArrayList;

import static discordInteraction.util.Output.sendMessageToUser;

// This class is designed to be safe from multiple queries.
public class List<T extends QueuedCommandBase> {
    private final Object lock = new Object();
    private ArrayList<T> commands;

    public List() {
        commands = new ArrayList<T>();
    }

    public ArrayList<T> getCommands() {
        synchronized (lock) {
            ArrayList<T> list = new ArrayList<T>();
            for (T command : commands)
                list.add(command);
            return list;
        }
    }

    public void add(T command) {
        synchronized (lock) {
            commands.add(command);
        }
    }

    public boolean hasAnotherCommand() {
        synchronized (lock) {
            return commands != null && !commands.isEmpty();
        }
    }

    public void refund() {
        synchronized (lock) {
            for (T command : commands) {
                if (Main.viewers.containsKey(command.getViewer())) {
                    Main.viewers.get(command.getViewer()).insertCard(command.getCard());
                    sendMessageToUser(command.getViewer(), "Your " + command.getCard().getName() +
                            " failed to cast before the battle ended, and has been refunded.");
                }
            }
        }
    }

    public void clear() {
        synchronized (lock) {
            commands.clear();
        }
    }
}
