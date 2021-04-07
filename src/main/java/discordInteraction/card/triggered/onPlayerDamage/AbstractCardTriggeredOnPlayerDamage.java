package discordInteraction.card.triggered.onPlayerDamage;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import discordInteraction.card.AbstractCard;
import discordInteraction.card.ViewerCardType;
import discordInteraction.card.triggered.AbstractCardTriggered;
import discordInteraction.command.ResultWithInt;
import net.dv8tion.jda.api.entities.User;

public abstract class AbstractCardTriggeredOnPlayerDamage extends AbstractCardTriggered {
    @Override
    public ViewerCardType getViewerCardType() {
        return ViewerCardType.triggerOnPlayerDamage;
    }

    public abstract ResultWithInt handleOnPlayerDamageTrigger(int incomingDamage, DamageInfo damageInfo, AbstractPlayer player, User user);
}
