package discordInteraction.command;

import discordInteraction.card.triggered.AbstractCardTriggered;
import discordInteraction.card.triggered.TriggerTimingType;
import net.dv8tion.jda.api.entities.User;

public class QueuedCommandTriggered extends QueuedCommandBase<AbstractCardTriggered> {
    private int timeLeft;

    public void handleEndTurnLogic() {
        if (card.getTriggerTimingType() == TriggerTimingType.perTurn && timeLeft > 0)
            timeLeft--;
    }

    public void handleAfterTriggerLogic(){
        if (card.getTriggerTimingType() == TriggerTimingType.perTrigger && timeLeft > 0)
            timeLeft--;
    }

    public boolean shouldBeRetained(){
        return hasLivingViewerMonster() && timeLeft != 0;
    }

    public boolean shouldBeRefundedOnFail(){
        return card.getTriggerTimingType() != TriggerTimingType.infinite && card.getTimesToBeTriggered() == 1;
    }

    public QueuedCommandTriggered(User viewer, AbstractCardTriggered card){
        super(viewer, card);
        if (card.getTriggerTimingType() == TriggerTimingType.infinite || card.getTimesToBeTriggered() <= 0)
            timeLeft = -1;
        else
            timeLeft = card.getTimesToBeTriggered();
    }
}
