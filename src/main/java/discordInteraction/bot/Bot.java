package discordInteraction.bot;

import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import discordInteraction.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class Bot {
    public static JDA bot;
    public static SpireConfig config;
    public static MessageChannel channel;
    public static LocalDateTime lastMessageSent;

    public Bot(){
        lastMessageSent = LocalDateTime.now();

        try {
            config = getDefaultConfig();

            config.load();
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SpireConfig getDefaultConfig() throws IOException {
        SpireConfig config = new SpireConfig(Main.modName, Main.botConfigName);

        config.setString("BotToken", "");
        config.setString("ChannelName", "");
        config.setString("ServerName", "");

        return config;
    }

    public static void connectBot(){
        // Reload config.
        try {
            config = getDefaultConfig();

            config.load();
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create our bot.
        int attempt = 0;
        while (attempt < 10 && bot == null) {
            try {
                bot = JDABuilder.createDefault(config.getString("BotToken")).build().awaitReady();
                bot.addEventListener(new MessageListener());
            } catch (Exception e) {
                bot = null;
            }
            attempt++;
        }

        if (bot == null)
            Main.logger.debug("Failed to connect to discord bot after 10 attempts, please check your token or connection to discord servers.");

        // Connect our bot to the requested server.
        attempt = 0;
        while (attempt < 100 && channel == null) {
            try {
                List<Guild> guilds = bot.getGuildsByName(config.getString("ServerName"), true);
                for (Guild guild : guilds) {
                    List<TextChannel> channels = guild.getTextChannelsByName(config.getString("ChannelName"), true);
                    if (channels.size() > 0) {
                        channel = channels.get(0);
                        break;
                    }
                }
                if (channel != null)
                    break;
                attempt++;
            } catch (Exception e) {
                attempt++;
            }
        }

        if (channel == null) {
            try {
                Main.logger.debug("Failed to connect to channel after 10 attempts. Please check your server/channel names.");
                Main.logger.debug("Server Name: " + config.getString("Server Name"));
                Main.logger.debug("Chanel Name: " + config.getString("Channel Name"));

            } catch (Exception e) {
                Main.logger.debug(e.getMessage());
            }
        }
    }
}
