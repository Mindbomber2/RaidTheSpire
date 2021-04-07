package discordInteraction.config;

import basemod.ModLabeledButton;
import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import discordInteraction.Main;
import discordInteraction.util.FileSystem;

import java.io.File;
import java.io.IOException;

public class BotConfig {
    private final String filename = "BotConfig";
    private final String configName = "Bot Configuration";
    private SpireConfig config;

    public String getToken(){
        return config.getString("BotToken");
    }

    public String getChannelName(){
        return config.getString("ChannelName");
    }

    public String getServerName(){
        return config.getString("ServerName");
    }

    public BotConfig(){
        reload();
    }

    private SpireConfig getDefaultConfig() throws IOException {
        SpireConfig config = new SpireConfig(Main.modName, filename);

        config.setString("BotToken", "");
        config.setString("ChannelName", "");
        config.setString("ServerName", "");

        return config;
    }

    public void addToConfig(ModPanel settingsPanel) {
        ModLabeledButton openConfig = new ModLabeledButton(configName, 350f, 700f, Settings.BLUE_TEXT_COLOR,
                Settings.RED_TEXT_COLOR, settingsPanel, (button) ->{
            File configFile = new File(SpireConfig.makeFilePath(Main.modName, filename));
            FileSystem.openFileWithDefault(configFile);
        });
        settingsPanel.addUIElement(openConfig);
    }

    public void reload() {
        try {
            config = getDefaultConfig();

            config.load();
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
