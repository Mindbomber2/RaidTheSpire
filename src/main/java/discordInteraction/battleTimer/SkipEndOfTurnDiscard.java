package discordInteraction.battleTimer;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;

@SpirePatch2(clz = DiscardAtEndOfTurnAction.class, method = "update")
public class SkipEndOfTurnDiscard {
    @SpirePrefixPatch
    public static SpireReturn<?> skipEndOfTurnDiscard(DiscardAtEndOfTurnAction __instance) {
        __instance.isDone=true;
        return SpireReturn.Return();
    }
}
