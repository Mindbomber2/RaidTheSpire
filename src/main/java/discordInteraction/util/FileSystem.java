package discordInteraction.util;

import java.awt.*;
import java.io.File;

public class FileSystem {
    public static boolean openFileWithDefault(File file) {
        try {

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
    }
}
