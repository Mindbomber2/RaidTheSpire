package discordInteraction.util;

import java.util.ArrayList;

public class Formatting {
    public static String lineBreak(int width) {
        return lineBreak(width, '-', '|');
    }

    // Mostly for output; put a dividing line.
    public static String lineBreak(int width, char filler, char edge) {
        StringBuilder sb = new StringBuilder(width);
        sb.append(edge);
        for (int x = 0; x < width - 2; x++)
            sb.append(filler);
        sb.append(edge);
        return sb.toString();
    }

    // Mostly for output. Center text in a requested width.
    public static String center(String s, int width) {
        s = s.trim();

        StringBuilder sb = new StringBuilder(width);

        for (int i = 0; i < (width - s.length()) / 2; i++) {
            sb.append(' ');
        }
        sb.append(s);
        while (sb.length() < width) {
            sb.append(' ');
        }

        return sb.toString();
    }

    // Mostly for output. Splits up a string into a List of strings, wich each string being smaller than the requested width.
    public static ArrayList<String> split(String s, int width) {
        ArrayList<String> result = new ArrayList<>();

        StringBuilder sb = new StringBuilder(width);
        for (String word : s.split(" ")) {
            if (sb.toString().trim().length() + word.trim().length() > width - 2) {
                result.add(sb.toString().trim());
                sb = new StringBuilder(width);
            }
            sb.append(word);
            sb.append(' ');
        }
        result.add(sb.toString().trim());

        return result;
    }

    public static String getStringFromArrayList(ArrayList<String> list, String divider){
        String result = list.get(0);
        for(int x = 1; x < list.size(); x++)
            result += divider + list.get(x);
        return  result;
    }

    public static String putInCodeBlock(String string){
        return "```\n" + string + "```";
    }
}
