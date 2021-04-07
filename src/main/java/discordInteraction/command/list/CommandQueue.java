package discordInteraction.command.list;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import discordInteraction.Main;
import discordInteraction.command.*;
import net.dv8tion.jda.api.entities.User;

import static discordInteraction.util.Output.sendMessageToUser;

public class CommandQueue {
    public List<QueuedTimedCommandTargeted> targeted;
    public List<QueuedTimedCommandTargetless> targetless;
    public List<QueuedCommandTriggerOnPlayerDamage> continousTriggerOnPlayerDamage;
    public List<QueuedCommandTriggerOnPlayerDamage> oneTimeTriggerOnPlayerDamage;

    public CommandQueue(){
        targeted = new List<QueuedTimedCommandTargeted>();
        targetless = new List<QueuedTimedCommandTargetless>();
        continousTriggerOnPlayerDamage = new List<QueuedCommandTriggerOnPlayerDamage>();
        oneTimeTriggerOnPlayerDamage = new List<QueuedCommandTriggerOnPlayerDamage>();
    }

    public boolean hasQueuedCommands() {
        return targeted.hasAnotherCommand() || targetless.hasAnotherCommand();
    }

    public boolean userHasCommandQueued(User user) {
        for (QueuedTimedCommandTargeted command : targeted.getCommands())
            if (command.getViewer() == user)
                return true;
        for (QueuedTimedCommandTargetless command : targetless.getCommands())
            if (command.getViewer() == user)
                return true;
        for (QueuedCommandBase command : continousTriggerOnPlayerDamage.getCommands())
            if (command.getViewer() == user)
                return true;
        for (QueuedCommandBase command : oneTimeTriggerOnPlayerDamage.getCommands())
            if (command.getViewer() == user)
                return true;
        return false;
    }

    public void handlePerTurnLogic() {
        for (QueuedTimedCommandTargeted command : targeted.getCommands()) {
            command.reduceTimer();
            if (command.timer <= 0f) {
                Result result = command.getCard().activate(command.getViewer(), AbstractDungeon.player, command.getTargets());
                if (result.wasSuccessful()) {
                    sendMessageToUser(command.getViewer(), "You successfully casted " + command.getCard().getName() + ". " + result.getWhatHappened());
                } else {
                    sendMessageToUser(command.getViewer(), "You failed to cast " + command.getCard().getName() + ". " + result.getWhatHappened());
                    Main.viewers.get(command.getViewer()).insertCard(command.getCard());
                }
                Main.battle.getViewerMonster(command.getViewer()).clearMoves();
            }
        }
        for (QueuedTimedCommandTargetless command : targetless.getCommands()) {
            command.reduceTimer();
            if (command.timer <= 0f) {
                Result result = command.getCard().activate(command.getViewer(), AbstractDungeon.player);
                if (result.wasSuccessful()) {
                    sendMessageToUser(command.getViewer(), "You successfully casted " + command.getCard().getName() + ". " + result.getWhatHappened());
                } else {
                    sendMessageToUser(command.getViewer(), "You failed to cast " + command.getCard().getName() + ". " + result.getWhatHappened());
                    Main.viewers.get(command.getViewer()).insertCard(command.getCard());
                }
                Main.battle.getViewerMonster(command.getViewer()).clearMoves();
            }
        }
    }

    public void handlePostBattleLogic() {
        targeted.refund();
        targetless.refund();
        oneTimeTriggerOnPlayerDamage.refund();

        // We don't need to do anything with these triggers except clear them.
        continousTriggerOnPlayerDamage.clear();
    }

    public int handleOnPlayerDamagedLogic(int incomingDamage, DamageInfo damageInfo) {
        if (damageInfo.type == DamageInfo.DamageType.HP_LOSS || !continousTriggerOnPlayerDamage.hasAnotherCommand())
            return incomingDamage;

        // Handle one time triggers first.
        while (incomingDamage > 0){
            for(QueuedCommandTriggerOnPlayerDamage command : oneTimeTriggerOnPlayerDamage.getCommands()) {
                incomingDamage = handleTriggerOnPlayerDamageCommand(incomingDamage, damageInfo, command, true);
            }
        }

        while (incomingDamage > 0){
            for(QueuedCommandTriggerOnPlayerDamage command : continousTriggerOnPlayerDamage.getCommands()) {
                incomingDamage = handleTriggerOnPlayerDamageCommand(incomingDamage, damageInfo, command, false);
            }

        }

        return incomingDamage;
    }
    private int handleTriggerOnPlayerDamageCommand(int incomingDamage, DamageInfo damageInfo, QueuedCommandTriggerOnPlayerDamage command, boolean refundOnFail){
        ResultWithInt result = command.getCard().handleOnPlayerDamageTrigger(incomingDamage, damageInfo, AbstractDungeon.player, command.getViewer());
        if (result.wasSuccessful()){
            sendMessageToUser( command.getViewer(), result.getWhatHappened());
        } else if (refundOnFail){
            sendMessageToUser(command.getViewer(), command.getCard().getName() + " failed to trigger, and has been refunded. " + result.getWhatHappened());
            Main.viewers.get(command.getViewer()).insertCard(command.getCard());
        }
        return result.getReturnInt();
    }
}
