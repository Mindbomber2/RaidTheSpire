package discordInteraction.config;

import basemod.BaseMod;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Texture;

public class Config {
    public BotConfig bot;

    public Config(){
        bot = new BotConfig();
    }

    public void reload(){
        bot.reload();
    }

    public void registerConfigMenu(){
        Texture badgeTexture = new Texture("images/discord.png");

        ModPanel settingsPanel = new ModPanel();

        bot.addToConfig(settingsPanel);

        BaseMod.registerModBadge(badgeTexture, "Discord Interaction", "StarKelp", "Allows viewers to interact with your game via in game viewer monsters, using discord chat commands to control their actions.", settingsPanel);

        bot.addToConfig(settingsPanel);
    }
}
