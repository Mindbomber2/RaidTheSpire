package discordInteraction;

import discordInteraction.card.Card;
import discordInteraction.card.FlavorType;

import java.util.ArrayList;

public class Hand {
    private int capacity;
    private ArrayList<Card> cards;
    private ArrayList<FlavorType> flavorTypes;

    public ArrayList<Card> getCards(){
        return cards;
    }
    public ArrayList<FlavorType> getFlavorTypes(){
        return flavorTypes;
    }
    public void addFlavor(FlavorType flavor){
        if (!flavorTypes.contains(flavor))
            flavorTypes.add(flavor);
    }
    public void removeFlavor(FlavorType flavor){
        if (flavorTypes.contains(flavor))
            flavorTypes.remove(flavor);
    }

    public Hand(){
        capacity = 6;
        cards = new ArrayList<Card>();

        flavorTypes = new ArrayList<>();
        for (FlavorType flavor : FlavorType.values())
            flavorTypes.add(flavor);

        drawNewHand(5);
    }

    public void removeCard(Card card){
        cards.remove(card);
    }

    public Card getFirstCardByName(String cardName) {
        for (Card card : cards)
            if (card.getName().replaceAll("\\s+","").equalsIgnoreCase(cardName.replaceAll("\\s+","")))
                return card;

        return null;
    }

    public void discardHand(){
        cards.clear();
    }

    public void draw(int pointsToDraw){
        ArrayList<Card> cardPool = new ArrayList<Card>();

        for(FlavorType type : flavorTypes)
            cardPool.addAll(Main.deck.getCardsByFlavorType(type));

        while(pointsToDraw > 0 && cards.size() < capacity){
            int toDraw = Main.random.nextInt(cardPool.size() + 1) - 1;
            if (toDraw < 0)
                toDraw = 0;
            Card drawnCard = cardPool.get(toDraw);
            pointsToDraw -= drawnCard.getCost();
            cards.add(drawnCard);
        }
    }

    public void drawNewHand(int pointsToDraw){
        discardHand();
        draw(pointsToDraw);
    }
}
