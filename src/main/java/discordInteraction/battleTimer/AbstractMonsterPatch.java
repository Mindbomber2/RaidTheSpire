package discordInteraction.battleTimer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import discordInteraction.Main;
import discordInteraction.ViewerMinion;
import discordInteraction.battleTimer.constants.personalities.*;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import net.dv8tion.jda.api.entities.User;

import static discordInteraction.battleTimer.constants.TurnTimers.*;

public class AbstractMonsterPatch {

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class patchIntoTimer {
        public static SpireField<Float> currentMonsterTimer = new SpireField<>(() -> 10f);
        public static SpireField<Float> currentMaxMonsterTimer = new SpireField<>(() -> 10f);

        public static float calculateTime(AbstractMonster __instance) {
            float f = 0;
            if (AbstractDungeon.isPlayerInDungeon()) {
                AbstractPersonality currentPersonality;
                if (__instance instanceof ViewerMinion) {
                    f = TURN_TIMER_VIEWER;
                } else {
                    switch (__instance.type) {
                        case BOSS:
                            f = TURN_TIMER_BOSS;
                            break;
                        case ELITE:
                            f = TURN_TIMER_ELITE;
                            break;
                        case NORMAL:
                            f = TURN_TIMER_NORMAL;
                            break;
                        default:
                            f = TURN_TIMER_NORMAL;
                            break;
                    }
                }
                if (AbstractDungeon.ascensionLevel <= 5) {
                    currentPersonality = new MEDIUM();
                } else if (AbstractDungeon.ascensionLevel <= 10) {
                    currentPersonality = new HARD();
                } else if (AbstractDungeon.ascensionLevel <= 15) {
                    currentPersonality = new VERYHARD();
                } else {
                    currentPersonality = new INSANE();
                }
                for (int i = 1; i <= GameActionManager.turn; i += 1) {
                    if (i % 2 == 0) {
                        currentPersonality = currentPersonality.nextPersonality();
                    }
                }
                f += currentPersonality.calculateTimeValue();
                try {
                    if (AbstractDungeon.monsterRng.randomBoolean()) {
                        f /= 1.5f;
                    } else {
                        f /= 1.45f;
                    }
                } catch (Exception e) {
                    f /= 1.45f;
                }
            }
            return f;
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    String.class,
                    String.class,
                    int.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    String.class,
                    float.class,
                    float.class
            }
    )

    public static class constructorTimer {
        @SpirePostfixPatch
        public static void timerCtorPatch(AbstractMonster __instance, String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
            System.out.println("Patching ctor of " + __instance.name);
            System.out.println("Hitbox Width: " + __instance.hb.width);
            float calculatedTime = patchIntoTimer.calculateTime(__instance);
            patchIntoTimer.currentMonsterTimer.set(__instance, calculatedTime);
            patchIntoTimer.currentMaxMonsterTimer.set(__instance, calculatedTime);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "render")
    public static class timerRenderPatch {
        @SpirePostfixPatch
        public static void timerCtorPatch(AbstractMonster __instance, SpriteBatch sb) {
            if (__instance instanceof AbstractFriendlyMonster && !(__instance instanceof ViewerMinion)) {
                return;
            }
            if (!__instance.isDeadOrEscaped()) {
                    DrawMonsterTimer.drawMonsterTimer(sb, __instance, patchIntoTimer.currentMonsterTimer.get(__instance),
                            patchIntoTimer.currentMaxMonsterTimer.get(__instance));
            }
            if (!AbstractDungeon.isScreenUp) {
                patchIntoTimer.currentMonsterTimer.set(__instance,
                        patchIntoTimer.currentMonsterTimer.get(__instance) - Gdx.graphics.getDeltaTime());
                if (patchIntoTimer.currentMonsterTimer.get(__instance) <= 0f) {
                    if (__instance instanceof AbstractFriendlyMonster) {
                        User u = Main.battle.getUser((AbstractFriendlyMonster) __instance);
                        Main.commandQueue.handleEndOfPlayerTurnLogic(u);
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new monsterTakeTurnAction(__instance));
                    }
                    TurnbasedPowerStuff.triggerMonsterTurnPowers(__instance);
                    float calculatedTime = patchIntoTimer.calculateTime(__instance);
                    patchIntoTimer.currentMonsterTimer.set(__instance, calculatedTime);
                    patchIntoTimer.currentMaxMonsterTimer.set(__instance, calculatedTime);
                }
            }
        }
    }
}