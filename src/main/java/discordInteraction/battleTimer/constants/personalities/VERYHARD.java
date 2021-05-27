package discordInteraction.battleTimer.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class VERYHARD extends AbstractPersonality {

    @Override
    public float calculateTimeValue() { return AbstractDungeon.monsterRng.random(-4.5f, -5); }
    @Override
    public AbstractPersonality nextPersonality() { return new INSANE(); }
}