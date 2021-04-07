package discordInteraction.command;

import discordInteraction.card.targetless.AbstractCardTargetless;
import net.dv8tion.jda.api.entities.User;

public class QueuedTimedCommandTargetless extends QueuedCommandBase<AbstractCardTargetless> {
    public float timer;

    public QueuedTimedCommandTargetless(User viewer, AbstractCardTargetless card, float timer) {
        super(viewer, card);
        this.timer = timer;
    }

    public void reduceTimer() {
        timer -= 1;
    }
}
