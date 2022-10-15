package discordInteraction.battleTimer;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TurnbasedPowerStuff {

    public static void triggerMonsterTurnPowers(AbstractMonster m) {
        //end of turn
        for (AbstractPower p : m.powers) {
            p.atEndOfTurn(false);
            p.atEndOfRound();
            p.atEndOfTurnPreEndTurnCards(false);
        }
        //start of turn
        if (!m.isDying && !m.isEscaping) {
            if (!m.hasPower("Barricade"))
                m.loseBlock();
            m.applyStartOfTurnPowers();
        }
    }
}
