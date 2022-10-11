package discordInteraction.battleTimer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import static discordInteraction.battleTimer.constants.TurnTimers.TURN_TIMER_PLAYER;

public class PlayerCountdownPatch {

    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class patchIntoTimer {
        public static SpireField<Boolean> canPlayCard = new SpireField<>(() -> false);
        public static SpireField<Float> currentPlayerTimer = new SpireField<>(() -> 10f);
        public static SpireField<Float> currentMaxPlayerTimer = new SpireField<>(() -> 10f);

        public static float calculateTime(AbstractPlayer __instance) {
            return TURN_TIMER_PLAYER;
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    String.class,
                    AbstractPlayer.PlayerClass.class
            }
    )

    public static class constructorTimer {
        @SpirePostfixPatch
        public static void timerCtorPatch(AbstractPlayer __instance) {
            System.out.println("Patching ctor of " + __instance.name);
            float calculatedTime = patchIntoTimer.calculateTime(__instance);
            patchIntoTimer.currentPlayerTimer.set(__instance, calculatedTime);
            patchIntoTimer.currentMaxPlayerTimer.set(__instance, calculatedTime);
        }
    }


//   ty Alison again
    public static class EnergyPanelModificationPatches {

        public static String getMessage() {
            return String.valueOf(patchIntoTimer.currentPlayerTimer.get(AbstractDungeon.player).intValue());
        }

        public static Color getColor() {
            AbstractPlayer p = AbstractDungeon.player;
            Color color = Color.YELLOW;
            if (Math.floor(patchIntoTimer.currentPlayerTimer.get(p)) > 2 * patchIntoTimer.currentMaxPlayerTimer.get(p) / 3) {
                color = Color.GREEN;
            } else if (Math.floor(patchIntoTimer.currentPlayerTimer.get(p)) < patchIntoTimer.currentMaxPlayerTimer.get(p) / 3) {
                color = Color.RED;
            }
            return color;
        }

        @SpirePatch2(clz = EnergyPanel.class, method = "render")
        public static class BeDifferentColorPls {
            @SpireInstrumentPatch
            public static ExprEditor patch() {
                return new ExprEditor() {
                    @Override
                    //Method call is basically the equivalent of a methodcallmatcher of an insert patch, checks the edit method against every method call in the function you#re patching
                    public void edit(MethodCall m) throws CannotCompileException {
                        //If the method is from the class FontHelper and the method is called renderFontCentered
                        if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("renderFontCentered")) {
                            m.replace("{" +
                                    //$3 refers to the third input parameter of the method, in this case the message
                                    //You need the full package to your class for this to work
                                    "$3 = discordInteraction.battleTimer.PlayerCountdownPatch.EnergyPanelModificationPatches.getMessage();" +
                                    "$6 = discordInteraction.battleTimer.PlayerCountdownPatch.EnergyPanelModificationPatches.getColor();" +
                                    //Call the method as normal
                                    "$proceed($$);" +
                                    "}");
                        }
                    }
                };
            }
        }
    }

    @SpirePatch2(clz = EnergyPanel.class, method = "update")
    public static class PlayerCooldownUpdatePatch {
        @SpirePostfixPatch
        public static void letItGoDown() {
            AbstractPlayer p = AbstractDungeon.player;
            if (!AbstractDungeon.isScreenUp) {
                patchIntoTimer.currentPlayerTimer.set(p,
                        patchIntoTimer.currentPlayerTimer.get(p) - Gdx.graphics.getDeltaTime());
                if (patchIntoTimer.currentPlayerTimer.get(p) <= 0f) {
                    patchIntoTimer.canPlayCard.set(p, true);
                    TurnbasedPowerStuff.triggerEndOfTurnPowersOnPlayer();
                    float calculatedTime = patchIntoTimer.calculateTime(p);
                    patchIntoTimer.currentPlayerTimer.set(p, calculatedTime);
                    patchIntoTimer.currentMaxPlayerTimer.set(p, calculatedTime);
                }
            }
        }
    }
}