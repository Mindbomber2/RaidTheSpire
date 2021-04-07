package discordInteraction.battleTimer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static discordInteraction.battleTimer.battletimer_const.*;

public class AbstractMonsterPatch {

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class patchIntoTimer {
        public static SpireField<Float> currentMonsterTimer = new SpireField<>(() -> 10f);

        public static float calculateTime(AbstractMonster __instance){
            float f =  __instance.type.equals(AbstractMonster.EnemyType.BOSS) ? TURN_TIMER_BOSS : ( __instance.type.equals(AbstractMonster.EnemyType.ELITE) ? TURN_TIMER_ELITE : TURN_TIMER_NORMAL);
            f += AbstractDungeon.monsterRng.random(__instance.type.equals(AbstractMonster.EnemyType.BOSS) ? -6 : ( __instance.type.equals(AbstractMonster.EnemyType.ELITE) ? -4 : -1), __instance.type.equals(AbstractMonster.EnemyType.BOSS) ? 0 : ( __instance.type.equals(AbstractMonster.EnemyType.ELITE) ? 2 : 2));
            if(AbstractDungeon.ascensionLevel == 20){ f /= 1.5f; }
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

    public static class constructorTimer{
        @SpirePostfixPatch
        public static void timerCtorPatch(AbstractMonster __instance, String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
            patchIntoTimer.currentMonsterTimer.set(__instance, patchIntoTimer.calculateTime(__instance));
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "render")
    public static class timerRenderPatch {
        @SpirePostfixPatch
        public static void timerCtorPatch(AbstractMonster __instance, SpriteBatch sb) {
            patchIntoTimer.currentMonsterTimer.set(__instance,
                    patchIntoTimer.currentMonsterTimer.get(__instance) - Gdx.graphics.getDeltaTime());

            if(patchIntoTimer.currentMonsterTimer.get(__instance) <= 0f){
                AbstractDungeon.actionManager.addToBottom(new monsterTakeTurnAction(__instance));
                patchIntoTimer.currentMonsterTimer.set(__instance, patchIntoTimer.calculateTime(__instance));
            }
        }
    }
}