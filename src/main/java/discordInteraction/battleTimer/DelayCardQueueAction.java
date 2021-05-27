package discordInteraction.battleTimer;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class DelayCardQueueAction extends AbstractGameAction {
    private int energy;

    public DelayCardQueueAction(){
        this(2.5f);
    }
    public DelayCardQueueAction(float timer){
        this.duration=timer;
    }
    //ty jobby
    @Override
    public void update() {
        if(!AbstractDungeon.actionManager.isEmpty()){
            AbstractDungeon.actionManager.addToBottom(new DelayCardQueueAction());
            this.isDone=true;
        } else {
            for(AbstractCard c : AbstractDungeon.player.hand.group){
                c.setLocked();
            }
            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < 0.0F) {
                for(AbstractCard c : AbstractDungeon.player.hand.group){
                    c.unlock();
                }
                this.isDone = true;
            }
        }
    }


}
