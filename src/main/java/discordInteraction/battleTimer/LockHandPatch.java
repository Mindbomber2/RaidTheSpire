package discordInteraction.battleTimer;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = com.megacrit.cardcrawl.cards.AbstractCard.class,
        method = "canUse"
)
public class LockHandPatch {
    @SpirePrefixPatch
    public static SpireReturn<Boolean> canUsePatch() {
        if(AbstractDungeon.actionManager.currentAction instanceof DelayCardQueueAction){
            return SpireReturn.Return(false);
        }
        return SpireReturn.Continue();
    }
}
