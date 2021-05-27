package discordInteraction.battleTimer.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class INSANE extends AbstractPersonality {

    @Override
    public float calculateTimeValue() { return AbstractDungeon.monsterRng.random(-5.75f, -6); }
    @Override
    public AbstractPersonality nextPersonality() { return new IMPOSSIBLE(); }
}