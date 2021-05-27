package discordInteraction.battleTimer.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MEDIUM extends AbstractPersonality {

    @Override
    public float calculateTimeValue() {
        try { return AbstractDungeon.monsterRng.random(-2f, -3f);
        } catch (Exception e) { return -2; }
    }
    @Override
    public AbstractPersonality nextPersonality() { return new HARD(); }
}