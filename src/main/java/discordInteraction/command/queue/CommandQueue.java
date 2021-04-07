package discordInteraction.command.queue;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import discordInteraction.card.triggered.onPlayerDamage.AbstractCardTriggeredOnPlayerDamage;
import discordInteraction.command.*;
import net.dv8tion.jda.api.entities.User;

import static discordInteraction.util.Output.sendMessageToUser;

public class CommandQueue {
    public Queue<QueuedCommandTargeted> targeted;
    public Queue<QueuedCommandTargetless> targetless;
    public Queue<QueuedCommandTriggered> triggerOnPlayerDamage;

    public CommandQueue(){
        targeted = new Queue<QueuedCommandTargeted>();
        targetless = new Queue<QueuedCommandTargetless>();
        triggerOnPlayerDamage = new Queue<QueuedCommandTriggered>();
    }

    public boolean hasQueuedCommands(){
        return targeted.hasAnotherCommand() || targetless.hasAnotherCommand();
    }

    public boolean userHasCommandQueued(User user) {
        for (QueuedCommandBase command : targeted.getCommands())
            if (command.getViewer() == user)
                return true;
        for (QueuedCommandBase command : targetless.getCommands())
            if (command.getViewer() == user)
                return true;
        for (QueuedCommandBase command : triggerOnPlayerDamage.getCommands())
            if (command.getViewer() == user)
                return true;
        return false;
    }

    public void handlePostBattleLogic() {
        targeted.refund();
        targetless.refund();

        // We don't need to do anything with these triggers except clear them.
        triggerOnPlayerDamage.clear();
    }

    public int handleOnPlayerDamagedLogic(int incomingDamage, DamageInfo damageInfo) {
        if (damageInfo.type == DamageInfo.DamageType.HP_LOSS || !triggerOnPlayerDamage.hasAnotherCommand())
            return incomingDamage;

        incomingDamage = handleTriggerOnPlayerDamageCommands(incomingDamage, damageInfo, triggerOnPlayerDamage);

        return incomingDamage;
    }
    private int handleTriggerOnPlayerDamageCommands(int incomingDamage, DamageInfo damageInfo, Queue<QueuedCommandTriggered> queue) {
        Queue<QueuedCommandTriggered> toRetain = new Queue<QueuedCommandTriggered>();
        while (triggerOnPlayerDamage.hasAnotherCommand()) {
            QueuedCommandTriggered command = triggerOnPlayerDamage.getNextCommand();

            // Viewer died to something, pop their command off the list.
            if (!command.hasLivingViewerMonster())
                continue;

            ResultWithInt result = ((AbstractCardTriggeredOnPlayerDamage) command.getCard()).handleOnPlayerDamageTrigger(incomingDamage, damageInfo, AbstractDungeon.player, command.getViewer());

            command.handleAfterTriggerLogic();

            if (command.shouldBeRetained()){
                toRetain.add(command); // Move it back into the queue.

                // Let them know what they did.
                sendMessageToUser(command.getViewer(), result.getWhatHappened());
            } else {

                // If needed, give the card back and let them know.
                if (command.shouldBeRefundedOnFail()) {
                    sendMessageToUser(command.getViewer(), command.getCard().getName() + " failed to trigger, and has been refunded. " + result.getWhatHappened());
                    command.handleRemovalLogic(true);
                }
                else {
                    sendMessageToUser(command.getViewer(), result.getWhatHappened());
                    command.handleRemovalLogic(false);
                }
            }

            incomingDamage = result.getReturnInt();
        }
        triggerOnPlayerDamage = toRetain;
        return incomingDamage;
    }

    public void handleEndOfPlayerTurnLogic(){
        while (targeted.hasAnotherCommand()){
            QueuedCommandTargeted command = targeted.getNextCommand();

            // Viewer died to something, pop their command off the list.
            if (!command.hasLivingViewerMonster())
                continue;

            Result result = command.getCard().activate(command.getViewer(), AbstractDungeon.player, command.getTargets());
            if (result.wasSuccessful()){
                sendMessageToUser(command.getViewer(), "You successfully casted " + command.getCard().getName() + ". " + result.getWhatHappened());
                command.handleRemovalLogic(false);
            } else{
                sendMessageToUser(command.getViewer(), "You failed to cast " + command.getCard().getName() + ". " + result.getWhatHappened());
                command.handleRemovalLogic(true);
            }

        }

        while (targetless.hasAnotherCommand()){
            QueuedCommandTargetless command = targetless.getNextCommand();

            // Viewer died to something, pop their command off the list.
            if (!command.hasLivingViewerMonster())
                continue;

            Result result = command.getCard().activate(command.getViewer(), AbstractDungeon.player);
            if (result.wasSuccessful()){
                sendMessageToUser( command.getViewer(), "You successfully casted " + command.getCard().getName() + ". " + result.getWhatHappened());
                command.handleRemovalLogic(false);
            } else{
                sendMessageToUser(command.getViewer(), "You failed to cast " + command.getCard().getName() + ". " + result.getWhatHappened());
                command.handleRemovalLogic(true);
            }
        }

        triggerOnPlayerDamage = handleEndTurnLogicHelper(triggerOnPlayerDamage);
    }
    private Queue<QueuedCommandTriggered> handleEndTurnLogicHelper(Queue<QueuedCommandTriggered> queue){
        Queue<QueuedCommandTriggered> toRetain = new Queue<QueuedCommandTriggered>();
        while(queue.hasAnotherCommand()){
            QueuedCommandTriggered command = queue.getNextCommand();
            command.handleEndTurnLogic();
            if (command.shouldBeRetained())
                toRetain.add(command);
            else
                command.handleRemovalLogic(false);
        }
        return toRetain;
    }
}
