package discordInteraction.cardLogic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public abstract class CooldownManager {
    public static void reduceCooldowns(){
        ArrayList<AbstractCard> cardsToRemove = new ArrayList<AbstractCard>();
        for(AbstractCard c : AbstractDungeon.player.discardPile.group){
            int remainingCooldown = AbstractCardPatch.patchSpireField.currentCooldown.get(c);
            remainingCooldown--;
            if(remainingCooldown<=0){
                cardsToRemove.add(c);
                AbstractCardPatch.patchSpireField.currentCooldown.set(c, AbstractCardPatch.patchSpireField.maxCooldown.get(c));
            } else {
                AbstractCardPatch.patchSpireField.currentCooldown.set(c, remainingCooldown);
            }
        }
        for(AbstractCard tmp : cardsToRemove){
            AbstractDungeon.player.discardPile.moveToDeck(tmp, true);
        }
    }

 /*   public static void setCooldown(AbstractCard card){
        AbstractCardPatch.patchSpireField.currentCooldown.set(card, card.cost);
    }*/
}
