package discordInteraction.command.list;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import discordInteraction.card.triggered.onPlayerDamage.AbstractCardTriggeredOnPlayerDamage;
import discordInteraction.command.*;
import net.dv8tion.jda.api.entities.User;

import static discordInteraction.util.Output.sendMessageToUser;

public class CommandQueue {
    public List<QueuedTimedCommandTargeted> targeted;
    public List<QueuedTimedCommandTargetless> targetless;
    public List<QueuedCommandTriggered> triggerOnPlayerDamage;

    public CommandQueue() {
        targeted = new List<QueuedTimedCommandTargeted>();
        targetless = new List<QueuedTimedCommandTargetless>();
        triggerOnPlayerDamage = new List<QueuedCommandTriggered>();
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

    private int handleTriggerOnPlayerDamageCommands(int incomingDamage, DamageInfo damageInfo, List<QueuedCommandTriggered> queue) {
        List<QueuedCommandTriggered> toRetain = new List<QueuedCommandTriggered>();
        for (QueuedCommandTriggered command : triggerOnPlayerDamage.getCommands()) {
            // Viewer died to something, pop their command off the list.
            if (!command.hasLivingViewerMonster())
                continue;

            ResultWithInt result = ((AbstractCardTriggeredOnPlayerDamage) command.getCard()).handleOnPlayerDamageTrigger(incomingDamage, damageInfo, AbstractDungeon.player, command.getViewer());

            command.handleAfterTriggerLogic();

            if (command.shouldBeRetained()) {
                toRetain.add(command); // Move it back into the queue.

                // Let them know what they did.
                sendMessageToUser(command.getViewer(), result.getWhatHappened());
            } else {

                // If needed, give the card back and let them know.
                if (command.shouldBeRefundedOnFail()) {
                    sendMessageToUser(command.getViewer(), command.getCard().getName() + " failed to trigger, and has been refunded. " + result.getWhatHappened());
                    command.handleRemovalLogic(true);
                } else {
                    sendMessageToUser(command.getViewer(), result.getWhatHappened());
                    command.handleRemovalLogic(false);
                }
            }

            incomingDamage = result.getReturnInt();
        }
        triggerOnPlayerDamage = toRetain;
        return incomingDamage;
    }

    public void handleEndOfPlayerTurnLogic() {
        for (QueuedTimedCommandTargeted command : targeted.getCommands()) {

            // Viewer died to something, pop their command off the list.
            if (!command.hasLivingViewerMonster())
                continue;

            Result result = command.getCard().activate(command.getViewer(), AbstractDungeon.player, command.getTargets());
            if (result.wasSuccessful()) {
                sendMessageToUser(command.getViewer(), "You successfully casted " + command.getCard().getName() + ". " + result.getWhatHappened());
                command.handleRemovalLogic(false);
            } else {
                sendMessageToUser(command.getViewer(), "You failed to cast " + command.getCard().getName() + ". " + result.getWhatHappened());
                command.handleRemovalLogic(true);
            }

        }

        for (QueuedTimedCommandTargetless command : targetless.getCommands()) {

            // Viewer died to something, pop their command off the list.
            if (!command.hasLivingViewerMonster())
                continue;

            Result result = command.getCard().activate(command.getViewer(), AbstractDungeon.player);
            if (result.wasSuccessful()) {
                sendMessageToUser(command.getViewer(), "You successfully casted " + command.getCard().getName() + ". " + result.getWhatHappened());
                command.handleRemovalLogic(false);
            } else {
                sendMessageToUser(command.getViewer(), "You failed to cast " + command.getCard().getName() + ". " + result.getWhatHappened());
                command.handleRemovalLogic(true);
            }
        }

        triggerOnPlayerDamage = handleEndTurnLogicHelper(triggerOnPlayerDamage);
    }

    private List<QueuedCommandTriggered> handleEndTurnLogicHelper(List<QueuedCommandTriggered> queue) {
        List<QueuedCommandTriggered> toRetain = new List<QueuedCommandTriggered>();
        for (QueuedCommandTriggered command : queue.getCommands()) {
            command.handleEndTurnLogic();
            if (command.shouldBeRetained())
                toRetain.add(command);
            else
                command.handleRemovalLogic(false);
        }
        return toRetain;
    }
}
