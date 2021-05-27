package discordInteraction.battleTimer.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HARD extends AbstractPersonality {

    @Override
    public float calculateTimeValue() { return AbstractDungeon.monsterRng.random(-3.25f, -4); }
    @Override
    public AbstractPersonality nextPersonality() { return new VERYHARD(); }
}