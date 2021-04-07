package discordInteraction;

import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewerMinion extends AbstractFriendlyMonster {
    private static String ID = "Viewer";
    private AbstractMonster target;

    private static String getImageDirectory(User user) {
        String dir = ConfigUtils.CONFIG_DIR + File.separator + "DiscordInteraction" + File.separator + "CachedImages" + File.separator;
        if (!(new File(dir).exists()))
            new File(dir).mkdirs();
        File imageFile = new File(dir + user.getName() + ".png");

        if (!imageFile.exists()) {
            try {
                URL url = new URL(user.getAvatarUrl());
                HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                httpcon.addRequestProperty("User-Agent", "");
                BufferedImage bImage = ImageIO.read(httpcon.getInputStream());

                Image tmp = bImage.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                BufferedImage dimg = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = dimg.createGraphics();
                g2d.drawImage(tmp, 0, 0, null);
                g2d.dispose();

                ImageIO.write(dimg, "png", imageFile);
            } catch (Exception e) {
                Main.logger.debug(e);
            }
        }

        if (imageFile.exists())
            return imageFile.getPath();
        else
            return "images/monsters/Viewer.png";
    }

    public ViewerMinion(User user, int offsetX, int offsetY) {
        super(user.getName(), ID, 35, -3.0F, 10.0F, 5.0F, 5.0F,
                getImageDirectory(user),
                offsetX, offsetY);
    }
}
