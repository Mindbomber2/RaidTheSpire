package discordInteraction.battle;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import discordInteraction.Main;
import discordInteraction.util.Output;
import discordInteraction.ViewerMinion;
import discordInteraction.command.Result;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.patches.PlayerAddFieldsPatch;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static discordInteraction.util.Output.listHandForViewer;
import static discordInteraction.util.Output.sendMessageToUser;

public class Battle {
    private final Object battleLock = new Object();

    private Boolean inBattle;
    private AbstractRoom battleRoom;
    private String battleMessageID;

    // The following is used for secondary start battle logic that triggers at the start of monsters' turns.
    // Some fights in this game don't properly trigger the pre battle hook, so we will enable battle on a monster turn if it isn't yet.
    // However, there are potential race conditions between end of battle and monster turns, so we want to stop rapid toggles based purely on monster turns.
    // Proper start/end battle hooks will always apply regardless of this timing.
    private LocalDateTime lastBattleToggle;

    private HashMap<User, AbstractFriendlyMonster> viewers;
    private HashSet<User> viewersDeadUntilNextBattle;

    // I have baked in +1 to the index in the getters and setters since this is often used for viewer display.
    private ArrayList<Target> targets;

    public Boolean isInBattle(){
        synchronized (battleLock){
            return inBattle;
        }
    }
    public AbstractRoom getBattleRoom(){
        synchronized (battleLock){
            return battleRoom;
        }
    }
    public String getBattleMessageID() {
        synchronized (battleLock) {
            return battleMessageID;
        }
    }
    public void setBattleMessageID(String id){
        synchronized (battleLock){
            battleMessageID = id;
        }
    }

    public void attemptToAddTarget(AbstractCreature creature, TargetType targetType){
        synchronized (battleLock){
            for (Target target : targets)
                if (target.getTarget() == creature)
                    return;
            if (!targets.contains(creature))
                targets.add(new Target(creature, targetType));
        }
    }
    public AbstractCreature getTargetByID(int targetID){
        synchronized (battleLock){
            return targets.get(targetID - 1).getTarget();
        }
    }
    public Result isTargetValid(int targetID, TargetType[] targetTypes){
        synchronized (battleLock){
            if (targetID > targets.size())
                return new Result(false, "Not in targeting list.");
            Target target = targets.get(targetID-1);
            if (!Arrays.asList(targetTypes).contains(target.getTargetType()))
                return new Result(false, "Invalid target type.");
            if (!targets.contains(target))
                return new Result(false, "Not in targeting list.");
            if (target.getTarget().isDeadOrEscaped())
                return new Result(false, "Target is dead or escaped.");
            return new Result(true, "Target is valid.");
        }
    }
    public HashMap<Integer, AbstractCreature> getTargets(boolean aliveOnly){
        synchronized (battleLock){
            HashMap<Integer, AbstractCreature> toReturn = new HashMap<>();
            for(int x = 0; x < targets.size(); x++){
                AbstractCreature target = targets.get(x).getTarget();
                if (target.isDeadOrEscaped() && aliveOnly)
                    continue;
                toReturn.put(x+1, target);
            }
            return toReturn;
        }
    }
    public ArrayList<AbstractCreature> getTargetList(boolean aliveOnly) {
        return getTargetList(true, null);
    }
    public ArrayList<AbstractCreature> getTargetList(boolean aliveOnly, TargetType[] targetTypes){
        synchronized (battleLock) {
            ArrayList<AbstractCreature> toReturn = new ArrayList<>();
            for (int x = 0; x < targets.size(); x++) {
                Target target = targets.get(x);
                if (target.getTarget().isDeadOrEscaped() && aliveOnly)
                    continue;
                if (targetTypes != null && !Arrays.asList(targetTypes).contains(target.getTargetType()))
                    continue;
                toReturn.add(target.getTarget());
            }
            return toReturn;
        }
    }

    public void startBattle(AbstractRoom room, boolean isStartOfTurnHook){
        synchronized (battleLock) {
            if (!isStartOfTurnHook && LocalDateTime.now().minusSeconds(15).isBefore(lastBattleToggle))
                return;
            inBattle = true;
            battleRoom = room;

            // Spawn in viewers.
            for (User user : Main.viewers.keySet()) {
                addViewerMonster(user);
            }

            updateTargets();

            // Give viewers some initial information.
            for(User user : Main.viewers.keySet()){
                sendMessageToUser(user, Output.getStatusForUser(user));
            }


            // Let our battle know what message to edit for game updates.
            if (Main.bot.channel != null) {
                Main.bot.channel.sendMessage(Output.getStartOfInProgressBattleMessage()).queue((message -> {
                    setBattleMessageID(message.getId());
                }));
            }

            lastBattleToggle = LocalDateTime.now();
        }
    }

    public void endBattle(){
        synchronized (battleLock) {
            // End the battle; edit the battle message to showcase the end result.
            Main.bot.channel.retrieveMessageById(getBattleMessageID()).queue((message -> {
                message.editMessage(Output.getEndOfBattleMessage()).queue();
                battleMessageID = null;
            }));

            // Remove all of our stored viewers.
            removeAllViewerMonsters();
            viewersDeadUntilNextBattle.clear();

            // Let the rest of the program know the fight ended.
            inBattle = false;
            battleRoom = null;

            lastBattleToggle = LocalDateTime.now();

            targets.clear();
        }
    }

    public boolean canUserSpawnIn(User user){
        return !viewersDeadUntilNextBattle.contains(user);
    }

    public void addViewerMonster(User user){
        if (!viewers.containsKey(user)){
            int x = -1200;
            int y = 500;

            int count = viewers.size();
            if ((Integer)PlayerAddFieldsPatch.f_maxMinions.get(AbstractDungeon.player) < count + 2)
                PlayerAddFieldsPatch.f_maxMinions.set(AbstractDungeon.player, count + 2);
            while (count >= 8){
                count -= 8;
                y -= 140;
            }
            x += (count * 120);

            AbstractFriendlyMonster viewer = new ViewerMinion(user, x, y);
            BasePlayerMinionHelper.addMinion(AbstractDungeon.player, viewer);
            viewers.put(user, viewer);
        }
    }
    public void removeViewerMonster(User user, boolean untilEndOfBattle){
        if (viewers.containsKey(user))
            viewers.remove(user);
        if (untilEndOfBattle)
            viewersDeadUntilNextBattle.add(user);
    }
    public void removeAllViewerMonsters(){
        viewers.clear();
    }
    public boolean hasViewerMonster(User user){
        return viewers.containsKey(user);
    }
    public boolean hasLivingViewerMonster(User user){
        if (!hasViewerMonster(user))
            return false;
        return !getViewerMonster(user).isDeadOrEscaped();
    }
    public HashMap<User, AbstractFriendlyMonster> getViewerMonsters(){
        return viewers;
    }
    public AbstractFriendlyMonster getViewerMonster(User user){
        if (viewers.containsKey(user))
            return viewers.get(user);
        else
            return null;
    }

    public Battle(){
        inBattle = false;
        battleRoom = null;
        viewers = new HashMap<>();
        viewersDeadUntilNextBattle = new HashSet<User>();
        targets = new ArrayList<>();
    }

    public void handlePreMonsterTurnLogic() {
        // If a battle isn't going, try to start it.
        if (!isInBattle())
            startBattle(AbstractDungeon.getCurrRoom(), false);

        // Add any viewers that join mid fight.
        addMissingMonsters();

        updateTargets();
    }

    public void handlePostEnergyRecharge() {
        addMissingMonsters();
        updateTargets();

        // Update our battle message to remove any commands that have been executed.
        Main.bot.channel.retrieveMessageById(Main.battle.getBattleMessageID()).queue((message -> {
            message.editMessage(Output.getStartOfInProgressBattleMessage()).queue();
        }));
    }

    public void updateTargets() {
            attemptToAddTarget(AbstractDungeon.player, TargetType.player);
            for (AbstractCreature creature : battleRoom.monsters.monsters)
                attemptToAddTarget(creature, TargetType.monster);
            for (AbstractCreature creature : viewers.values())
                attemptToAddTarget(creature, TargetType.viewer);
    }

    public void addMissingMonsters(){
        for (User viewer : Main.viewers.keySet())
            if (!hasViewerMonster(viewer) && canUserSpawnIn(viewer))
                addViewerMonster(viewer);
    }
}
