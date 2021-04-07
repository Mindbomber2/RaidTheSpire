package discordInteraction.command;

import discordInteraction.card.targetless.AbstractCardTargetless;
import net.dv8tion.jda.api.entities.User;

public class QueuedCommandTargetless extends QueuedCommandBase<AbstractCardTargetless> {
    public QueuedCommandTargetless(User viewer, AbstractCardTargetless card) {
        super(viewer, card);
    }
}
