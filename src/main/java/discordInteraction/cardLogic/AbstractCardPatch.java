package discordInteraction.cardLogic;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import javassist.CtBehavior;

public class AbstractCardPatch {

    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {String.class, String.class, String.class, int.class, String.class, AbstractCard.CardType.class, AbstractCard.CardColor.class, AbstractCard.CardRarity.class, AbstractCard.CardTarget.class, DamageInfo.DamageType.class})
    public static class patchConstructor {
        @SpirePostfixPatch
        public static void changeCostToCooldown(AbstractCard __instance) {
            if (patchSpireField.maxCooldown.get(__instance) == 0) {
                int cardCooldown = __instance.cost;
                switch (cardCooldown) {
                    case -1:
                        cardCooldown = 10;
                        break;
                    case -2:
                    case 0:
                        cardCooldown = 1;
                        break;
                    default:
                        cardCooldown = 5 * cardCooldown;
                }
                AbstractCardPatch.patchSpireField.maxCooldown.set(__instance, cardCooldown);
                AbstractCardPatch.patchSpireField.currentCooldown.set(__instance, cardCooldown);
                __instance.cost = 0;
                __instance.costForTurn = 0;
            }
        }
    }

    //Ty Alison
    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class FixEnergyRenderPls {
        @SpireInsertPatch(locator = Locator.class, localvars = {"text"})
        public static void pls(AbstractCard __instance, SpriteBatch sb, @ByRef String[] text) {
            text[0] = String.valueOf(patchSpireField.currentCooldown.get(__instance));
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                com.evacipated.cardcrawl.modthespire.lib.Matcher finalMatcher = new com.evacipated.cardcrawl.modthespire.lib.Matcher.MethodCallMatcher(AbstractCard.class, "getEnergyFont");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class patchSpireField {
        public static SpireField<Integer> currentCooldown = new SpireField<>(() -> 0);
        public static SpireField<Integer> maxCooldown = new SpireField<>(() -> 0);
    }
}
