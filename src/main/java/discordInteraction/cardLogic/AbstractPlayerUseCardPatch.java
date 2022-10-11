package discordInteraction.cardLogic;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import discordInteraction.battleTimer.PlayerCountdownPatch;

public class AbstractPlayerUseCardPatch {
    @SpirePatch2(clz = AbstractPlayer.class, method = "useCard", paramtypez = {AbstractCard.class, AbstractMonster.class, int.class})
    public static class patchConstructor {
        @SpirePrefixPatch
        public static SpireReturn<Void> stopPlay(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            if(PlayerCountdownPatch.patchIntoTimer.canPlayCard.get(AbstractDungeon.player)==false){
                AbstractDungeon.actionManager.addToBottom(new HoldCardAction(c,monster,energyOnUse));
                return SpireReturn.Return();
            } else {
                PlayerCountdownPatch.patchIntoTimer.canPlayCard.set(AbstractDungeon.player, false);
                return SpireReturn.Continue();
            }
        }
    }
}
