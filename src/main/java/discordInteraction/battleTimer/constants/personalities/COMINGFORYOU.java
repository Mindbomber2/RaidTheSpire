package discordInteraction.battleTimer.constants.personalities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class COMINGFORYOU extends AbstractPersonality {

    @Override
    public float calculateTimeValue() { return AbstractDungeon.monsterRng.random(-8.8f, -9); }
    @Override
    public AbstractPersonality nextPersonality() { return new HAHAHAHAHAHA(); }
}