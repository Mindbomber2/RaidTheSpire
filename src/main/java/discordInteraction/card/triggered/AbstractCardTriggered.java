package discordInteraction.card.triggered;

import discordInteraction.card.AbstractCard;

public abstract class AbstractCardTriggered extends AbstractCard {
    /**
     * If your trigger timing is NOT infinite, this must be set.
     * If this is not set, the card will be treated as though it has a
     * TriggerTimingType of infinite.
     */
    public int getTimesToBeTriggered(){
        return -1;
    }

    public abstract TriggerTimingType getTriggerTimingType();
}
