package discordInteraction.card.targetless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import discordInteraction.card.AbstractCard;
import discordInteraction.card.ViewerCardType;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

public abstract class AbstractCardTargetless extends AbstractCard {
    @Override
    public ViewerCardType getViewerCardType() {
        return ViewerCardType.targetless;
    }

    public abstract Result activate(User user, AbstractPlayer player);
}
