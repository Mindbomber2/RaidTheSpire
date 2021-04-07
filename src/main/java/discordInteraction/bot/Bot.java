package discordInteraction.bot;

import discordInteraction.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.LocalDateTime;
import java.util.List;

public class Bot {
    public static JDA bot;
    public static MessageChannel channel;
    public static LocalDateTime lastMessageSent;

    public Bot(){
        lastMessageSent = LocalDateTime.now();
    }

    public static void connectBot(){
        // Reload config.
        Main.config.reload();

        // Create our bot.
        int attempt = 0;
        while (attempt < 10 && bot == null) {
            try {
                bot = JDABuilder.createDefault(Main.config.bot.getToken()).build().awaitReady();
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
                List<Guild> guilds = bot.getGuildsByName(Main.config.bot.getServerName(), true);
                for (Guild guild : guilds) {
                    List<TextChannel> channels = guild.getTextChannelsByName(Main.config.bot.getChannelName(), true);
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
                Main.logger.debug("Server Name: " + Main.config.bot.getServerName());
                Main.logger.debug("Chanel Name: " + Main.config.bot.getChannelName());

            } catch (Exception e) {
                Main.logger.debug(e.getMessage());
            }
        }
    }
}
