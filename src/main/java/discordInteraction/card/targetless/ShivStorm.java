package discordInteraction.card.targetless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import discordInteraction.FlavorType;
import discordInteraction.card.targetless.AbstractCardTargetless;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class ShivStorm extends AbstractCardTargetless {
    @Override
    public String getName() {
        return "Shiv Storm";
    }

    @Override
    public int getCost() {
        return 3;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Shuffle 5 + X Shivs in the player's draw pile, where X is equal to the number of exhausted Shivs.";
    }

    @Override
    public String getFlavorText() {
        return "I think you dropped these.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.oppose,
                FlavorType.chaos
        };
    }

    @Override
    public Result activate(User user, AbstractPlayer player) {
        for (int x = 0; x < 5; x++)
            player.drawPile.addToRandomSpot(new Shiv());
        ArrayList<AbstractCard> cardsToMove = new ArrayList<AbstractCard>();
        for (AbstractCard card : player.exhaustPile.group)
            if (card.cardID == Shiv.ID)
                cardsToMove.add(card);
        for (AbstractCard card : cardsToMove)
            player.drawPile.addToRandomSpot(new Shiv());
        int moved = 5 + cardsToMove.size();
        return new Result(true, "You added " + moved + " Shivs to the player's deck.");
    }
}
