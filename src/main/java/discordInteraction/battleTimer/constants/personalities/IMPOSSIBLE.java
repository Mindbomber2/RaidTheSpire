package discordInteraction.battleTimer.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class IMPOSSIBLE extends AbstractPersonality {

    @Override
    public float calculateTimeValue() { return AbstractDungeon.monsterRng.random(-6.75f, -7); }
    @Override
    public AbstractPersonality nextPersonality() { return new ISEEYOU(); }
}