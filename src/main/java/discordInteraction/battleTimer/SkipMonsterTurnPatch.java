package discordInteraction.battleTimer;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
public class SkipMonsterTurnPatch {
    @SpirePrefixPatch
    public static void skipMonsterTurn() {
            AbstractDungeon.actionManager.addToBottom(new SkipEnemiesTurnAction());
            TurnbasedPowerStuff.triggerEndOfTurnPowersOnPlayer();
            AbstractDungeon.actionManager.addToBottom(new DelayCardQueueAction(2.5f));
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    GameActionManager.turn += 1;
                    isDone = true;
                }
            });
    }
}