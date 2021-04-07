package discordInteraction.util;

public class Old {

    // For output, spits out nicly formatted hands of cards.
//    public static String[] getHandForViewer(User viewer) {
//        if (!Main.viewers.containsKey(viewer))
//            return new String[] {"You do not currently have a hand registered with the game. You can request a hand by typing !join in " + Main.channel.getName() + "."};
//
//        Hand viewerHand = Main.viewers.get(viewer);
//
//        if (viewerHand.getCards().size() == 0)
//            return new String[] {"You do not currently have a hand. You'll draw cards when the streamer starts a new game, " +
//                    "wins a battle, reaches a campfire, or starts a new act."};
//
//        String[] result = new String[1 + ((viewerHand.getCards().size() - 1) / 3)];
//        for(int x = 0; x < viewerHand.getCards().size(); x+=3) {
//            int widthPerCard = 40;
//            int width = 1 + ((widthPerCard - 1) * Math.min(3, viewerHand.getCards().size() - x));
//            int internalWidthPerCard = widthPerCard - 4;
//
//            StringBuilder sb = new StringBuilder(width);
//
//            sb.append("```\n");
//
//            sb.append(Utilities.lineBreak(width));
//            sb.append("\n");
//
//            sb.append("| ");
//
//            for(int y = x; y < x+3 && y < viewerHand.getCards().size(); y++){
//                Card card = viewerHand.getCards().get(y);
//                sb.append(Utilities.center(card.getName(), widthPerCard - 4));
//                sb.append(" | ");
//            }
//
//            sb.append("\n");
//            sb.append(Utilities.lineBreak(width));
//            sb.append("\n");
//
//            sb.append("| ");
//
//            for(int y = x; y < x+3 && y < viewerHand.getCards().size(); y++){
//                Card card = viewerHand.getCards().get(y);
//                sb.append(Utilities.center("Rarity: " + Rarity.getRarityForCost(card.getCost()), widthPerCard - 4));
//                sb.append(" | ");
//            }
//
//            sb.append("\n");
//            sb.append(Utilities.lineBreak(width));
//            sb.append("\n");
//
//            ArrayList<ArrayList<String>> descriptions = new ArrayList<>();
//            for(int y = x; y < x+3 && y < viewerHand.getCards().size(); y++){
//                Card card = viewerHand.getCards().get(y);
//                descriptions.add(Utilities.split(card.getDescription(), internalWidthPerCard));
//            }
//
//            int max = 0;
//            for (ArrayList<String> list : descriptions)
//                max = Math.max(max, list.size());
//
//            for (int y = 0; y < max; y++) {
//                sb.append("| ");
//                for (ArrayList<String> lines : descriptions) {
//                    if (y < lines.size())
//                        sb.append(Utilities.center(lines.get(y), internalWidthPerCard));
//                    else
//                        for (int z = 0; z < internalWidthPerCard; z++)
//                            sb.append(' ');
//                    sb.append(" | ");
//                }
//                sb.append("\n");
//            }
//
//            sb.append(Utilities.lineBreak(width));
////            sb.append("\n");
////
////            ArrayList<ArrayList<String>> flavors = new ArrayList<>();
////            for(int y = x; y < x+3 && y < viewerHand.getCards().size(); y++){
////                Card card = viewerHand.getCards().get(y);
////                flavors.add(Utilities.split(card.getFlavorText(), internalWidthPerCard));
////            }
////
////            max = 0;
////            for (ArrayList<String> list : flavors)
////                max = Math.max(max, list.size());
////
////            for (int y = 0; y < max; y++) {
////                sb.append("| ");
////                for (ArrayList<String> lines : flavors) {
////                    if (y < lines.size())
////                        sb.append(Utilities.center(lines.get(y), internalWidthPerCard));
////                    else
////                        for (int z = 0; z < internalWidthPerCard; z++)
////                            sb.append(' ');
////                    sb.append(" | ");
////                }
////                sb.append("\n");
////            }
////
////            sb.append(Utilities.lineBreak(width));
//
//            sb.append("```");
//
//            result[x/3] = sb.toString().trim();
//        }
//        return result;
//    }

    // Format a viewer's hand and send it to them for ease of viewing.
//    public static void sendHandToViewer(User viewer){
//        String[] handFormatted = getHandForViewer(viewer);
//        for(String line : handFormatted)
//            sendMessageToUser(viewer, line);
//    }

}
