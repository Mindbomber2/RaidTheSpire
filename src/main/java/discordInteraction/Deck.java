package discordInteraction;

import discordInteraction.card.AbstractCard;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.reflections.Reflections;

public class Deck {
    private HashMap<FlavorType, ArrayList<AbstractCard>> cards;

    public ArrayList<AbstractCard> getCardsByFlavorType(FlavorType type){
        return cards.get(type);
    }

    private int highestCost;

    public int getHighestCost(){ return highestCost; }

    public Deck() {
        cards = new HashMap<FlavorType, ArrayList<AbstractCard>>();
        highestCost = 1;

        for (FlavorType type : FlavorType.values())
            cards.put(type, new ArrayList<AbstractCard>());

        // So this part is... fun.
        // It will look through the entire project and add all defined subclasses of Card into the above Lists based on their Flavor.
        Reflections reflections = new Reflections(AbstractCard.class);

        for( Class<? extends AbstractCard> cardType : reflections.getSubTypesOf(AbstractCard.class)){
            if (Modifier.isAbstract(cardType.getModifiers()))
                continue;
            try {
                AbstractCard.setTextureForCard(cardType);

                AbstractCard card = cardType.newInstance();
                highestCost = Math.max(highestCost, card.getCost());
                if (Arrays.stream(card.getFlavorTypes()).anyMatch(FlavorType.basic::equals))
                    cards.get(FlavorType.basic).add(card);
                if (Arrays.stream(card.getFlavorTypes()).anyMatch(FlavorType.support::equals))
                    cards.get(FlavorType.support).add(card);
                if (Arrays.stream(card.getFlavorTypes()).anyMatch(FlavorType.oppose::equals))
                    cards.get(FlavorType.oppose).add(card);
                if (Arrays.stream(card.getFlavorTypes()).anyMatch(FlavorType.chaos::equals))
                    cards.get(FlavorType.chaos).add(card);
            } catch (Exception e){
                Main.logger.debug(e);
            }

        }
    }
}
