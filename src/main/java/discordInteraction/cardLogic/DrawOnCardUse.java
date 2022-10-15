package discordInteraction.cardLogic;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class DrawOnCardUse {
    public static void drawOnCardUse(){
        //AbstractDungeon.actionManager.addToBottom(new DrawCardAction(1));
        AbstractDungeon.actionManager.addToBottom(new EmptyDeckShuffleAction());
    }
}
