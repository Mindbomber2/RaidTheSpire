package discordInteraction;

import discordInteraction.card.AbstractCard;

import java.util.ArrayList;

public class Hand {
    private int capacity;
    private ArrayList<AbstractCard> cards;
    private ArrayList<FlavorType> flavorTypes;

    public ArrayList<AbstractCard> getCards(){
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
        capacity = 10;
        cards = new ArrayList<AbstractCard>();

        flavorTypes = new ArrayList<>();
        for (FlavorType flavor : FlavorType.values())
            if (flavor != FlavorType.basic)
                flavorTypes.add(flavor);

        drawNewHand(5, 2);
    }

    public void insertCard(AbstractCard card){
        if (cards.size() < capacity)
            cards.add(card);
    }

    public void removeCard(AbstractCard card){
        cards.remove(card);
    }

    public AbstractCard getFirstCardByName(String cardName) {
        for (AbstractCard card : cards)
            if (card.getName().replaceAll("\\s+","").equalsIgnoreCase(cardName.replaceAll("\\s+","")))
                return card;

        return null;
    }

    public void discardHand(){
        cards.clear();
    }

    public void draw(int pointsToDraw, int basicsToDraw){
        ArrayList<AbstractCard> cardPool = new ArrayList<AbstractCard>();

        for(FlavorType type : flavorTypes)
            for(AbstractCard card : Main.deck.getCardsByFlavorType(type))
                for(int x = 0; x < Main.deck.getHighestCost(); x++)
                    cardPool.add(card);

        while(pointsToDraw > 0 && cards.size() < capacity){
            int toDraw = Main.random.nextInt(cardPool.size() + 1) - 1;
            if (toDraw < 0)
                toDraw = 0;
            AbstractCard drawnCard = cardPool.get(toDraw);
            pointsToDraw -= drawnCard.getCost();
            cards.add(drawnCard);
        }

        drawBasics(basicsToDraw);
    }

    public void drawNewHand(int pointsToDraw, int basicsToDraw){
        discardHand();
        draw(pointsToDraw, basicsToDraw);
    }

    public void drawBasics(int pointsToDraw){
        ArrayList<AbstractCard> cardPool = new ArrayList<AbstractCard>();

        for(AbstractCard card : Main.deck.getCardsByFlavorType(FlavorType.basic))
            cardPool.add(card);

        while(pointsToDraw > 0 && cards.size() < capacity){
            int toDraw = Main.random.nextInt(cardPool.size() + 1) - 1;
            if (toDraw < 0)
                toDraw = 0;
            AbstractCard drawnCard = cardPool.get(toDraw);
            pointsToDraw -= drawnCard.getCost();
            cards.add(drawnCard);
        }
    }
}
