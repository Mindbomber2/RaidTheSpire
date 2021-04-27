package discordInteraction.battleTimer;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AddDelayCardQueueAction {
    public static void addDelayCardQueueAction(){
        AbstractDungeon.actionManager.addToBottom(new DelayCardQueueAction());
    }
}
