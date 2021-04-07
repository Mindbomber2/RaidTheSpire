package discordInteraction;

import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import discordInteraction.battle.Battle;
import discordInteraction.bot.Bot;
import discordInteraction.bot.MessageListener;
import discordInteraction.command.queue.CommandQueue;
import discordInteraction.config.Config;
import discordInteraction.util.FileSystem;
import discordInteraction.util.Output;
import kobting.friendlyminions.helpers.MinionConfigHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basemod.interfaces.PreMonsterTurnSubscriber;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;


@SpireInitializer
public class Main implements PreMonsterTurnSubscriber, PostBattleSubscriber, OnStartBattleSubscriber,
        PostCampfireSubscriber, StartActSubscriber, StartGameSubscriber, OnPlayerDamagedSubscriber,
        PostEnergyRechargeSubscriber, PostInitializeSubscriber {
    public static final String modName = "DiscordInteraction";
    public static final Logger logger = LogManager.getLogger(Main.class.getName());

    public Main() {
        BaseMod.subscribe(this);
    }

    // Various important aspects.
    public static Bot bot;
    public static Random random;

    // Holds a list of viewers along with their respective hands.
    public static HashMap<User, Hand> viewers;
    // Holds all cards split up into their respective card types.
    public static Deck deck;
    // Holds current battle information.
    public static Battle battle;
    // Holds current viewer command information.
    public static CommandQueue commandQueue;
    // Holds various configuration options.
    public static Config config;

    public static void initialize() {
        // Setup the mod.
        random = new Random();
        battle = new Battle();
        bot = new Bot();
        viewers = new HashMap<User, Hand>();
        commandQueue = new CommandQueue();
        config = new Config();

        new Main();
    }

    @Override
    public boolean receivePreMonsterTurn(AbstractMonster monster) {
        // Handle battle logic.
        battle.handlePreMonsterTurnLogic();

        return true;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        // Let the rest of our program know we're in a fight.
        battle.startBattle(abstractRoom, true);
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        // Victory! Let all viewers draw 1 more card.
        for (User viewer : viewers.keySet()) {
            viewers.get(viewer).draw(1, 1);
            Output.listHandForViewer(viewer);
            Output.sendMessageToUser(viewer, "A battle was won! You have drawn 1 random and 1 basic card, hand size permitting.");
        }

        // Refund any cards that weren't cast in time due to the player rudely winning the fight.
        commandQueue.handlePostBattleLogic();

        // Update our battle information.
        battle.endBattle();
    }

    @Override
    public boolean receivePostCampfire() {
        for (User viewer : viewers.keySet()) {
            viewers.get(viewer).draw(3 + (AbstractDungeon.actNum / 2), 2);
            Output.listHandForViewer(viewer);
            Output.sendMessageToUser(viewer, "You have drawn new cards at the campfire, hand size permitting.");
        }

        return true;
    }

    @Override
    public void receiveStartAct() {
        for (User viewer : viewers.keySet()) {
            viewers.get(viewer).drawNewHand(5 + (AbstractDungeon.actNum * 2), 2);
            Output.listHandForViewer(viewer);
            viewers.get(viewer).drawBasics(2);
            Output.sendMessageToUser(viewer, "You have drawn a new hand of cards due to a new game or new act.");
        }
    }

    @Override
    public void receiveStartGame() {
        // Register our cards.
        if (deck == null)
            deck = new Deck();

        // Attempt to connect the bot; if needed.
        // Done here, to allow the player to adjust settings before starting.
        Bot.connectBot();

        if (Bot.channel != null)
            Bot.channel.sendMessage(AbstractDungeon.player.name + " has started a game in this channel! Type !join to join in.").queue();

        MinionConfigHelper.MinionAttackTargetChance = 0;
    }

    @Override
    public int receiveOnPlayerDamaged(int incomingDamage, DamageInfo damageInfo) {
        return commandQueue.handleOnPlayerDamagedLogic(incomingDamage, damageInfo);
    }

    @Override
    public void receivePostEnergyRecharge() {
        // This acts as a pseudo 'pre player turn' event.
        battle.handlePostEnergyRecharge();
    }

    @Override
    public void receivePostInitialize() {
       config.registerConfigMenu();
    }
}