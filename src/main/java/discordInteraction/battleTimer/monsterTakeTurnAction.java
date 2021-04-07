package discordInteraction.battleTimer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class monsterTakeTurnAction extends AbstractGameAction {

    private AbstractMonster __instance;
    public monsterTakeTurnAction(AbstractMonster m){
     __instance = m;
    }
    @Override
    public void update() {
        __instance.takeTurn();
        __instance.rollMove();
        __instance.createIntent();
        this.isDone = true;
    }
}
