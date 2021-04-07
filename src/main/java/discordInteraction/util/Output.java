package discordInteraction.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.card.AbstractCard;
import discordInteraction.command.QueuedCommandTargeted;
import discordInteraction.command.QueuedCommandTargetless;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

import java.text.Format;
import java.util.Map;

public class Output {
    // Shortcut to send messages to a user.
    public static void sendMessageToUser(User user, String message) {
        user.openPrivateChannel().queue((channel) ->
        {
            sendMessageToUser(channel, message);
        });
    }

    // Shortcut to send message to a private channel.
    public static void sendMessageToUser(PrivateChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

    // Send a simplified list of cards to a viewer. No formatting.
    public static String listHandForViewer(User viewer) {
        if (!Main.viewers.containsKey(viewer))
            return "You do not currently have a hand registered with the game. You can request a hand by typing !join in " + Main.bot.channel.getName() + ".";

        StringBuilder sb = new StringBuilder();
        for (AbstractCard card : Main.viewers.get(viewer).getCards()) {
            sb.append(card.getName());
            sb.append(" : ");
            sb.append(card.getDescriptionForViewerDisplay());
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    // Get a comma separated formatted list of targets.
    public static String getTargetListForDisplay(boolean aliveOnly) {
        if (Main.battle.getBattleRoom() == null)
            return "Battle has not yet started.";

        if (Main.battle.getTargets(aliveOnly).size() == 0)
            return "Battle currently has no valid targets.";

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, AbstractCreature> target : Main.battle.getTargets(aliveOnly).entrySet()) {
            if (target.getValue().isDeadOrEscaped() && aliveOnly)
                continue;

            sb.append("| ");
            sb.append(target.getValue().name);
            sb.append(" [");
            sb.append(target.getKey());
            sb.append("] |");

            sb.append("\n");
        }

        return sb.toString().replace('\n', ' ');
    }

    // Get a message showcasing all viewer cards to be resolved.
    public static String getUpcomingViewerCards() {
        if (!Main.commandQueue.hasQueuedCommands())
            return "No commands currently queued.";
        StringBuilder sb = new StringBuilder();
        for (QueuedCommandTargeted command : Main.commandQueue.targeted.getCommands()) {
            sb.append(command.getViewer().getName());
            sb.append(" is going to cast ");
            sb.append(command.getCard().getName());
            sb.append(" on");
            String targets = "";
            for (AbstractCreature target : command.getTargetsList())
                targets += " " + target.name;
            sb.append(targets);
            sb.append(".\n");
        }
        for (QueuedCommandTargetless command : Main.commandQueue.targetless.getCommands()) {
            sb.append(command.getViewer().getName());
            sb.append(" is going to cast ");
            sb.append(command.getCard().getName());
            sb.append(".\n");
        }

        return sb.toString().replace('\n', ' ');
    }

    // List all FlavorTypes currently supported by the game, excluding basic.
    public static String getFlavorList() {
        String output = "The current flavors are:";
        for (FlavorType flavor : FlavorType.values())
            if (flavor == FlavorType.basic)
                continue;
            else
                output += " " + flavor.toString();
        return output;
    }

    public static String getStartOfInProgressBattleMessage() {
        return "A battle is in progress!\n" +
                "If you have not yet joined in, you can type !join to join the game!\n" +
                "You may request an updated list of enemies with !targets in a private message.";
    }

    public static String getEndOfBattleMessage() {
        return "This battle has ended; any cards in the queue have been refunded.\n";
    }

    // An 'all in one' display of sorts.
    public static String getStatusForUser(User user){
        StringBuilder sb = new StringBuilder();
        sb.append(Formatting.putInCodeBlock("Cards"));
        sb.append(listHandForViewer(user));
        if (Main.battle.isInBattle()) {
            sb.append(Formatting.putInCodeBlock("Targets [TargetingID]"));
            sb.append(getTargetListForDisplay(true));
            sb.append(Formatting.putInCodeBlock("Status"));
            if (Main.battle.hasLivingViewerMonster(user)) {
                AbstractCreature viewer = Main.battle.getViewerMonster(user);
                sb.append("Health: ");
                sb.append(viewer.currentHealth);
                sb.append('/');
                sb.append(viewer.maxHealth);
                sb.append("\n");
                sb.append("Command Queued: ");
                sb.append(Main.commandQueue.userHasCommandQueued(user));
                sb.append("\n");
            } else if (Main.battle.canUserSpawnIn(user))
                sb.append("You have not yet spawned in, and should at the start of the next turn.");
            else
                sb.append("You are currently dead, and are unable to play any cards until the next battle.");
        } else{
            sb.append(Formatting.putInCodeBlock("Status"));
            sb.append("There is currently no battle in progress.");
        }
        return sb.toString();
    }
}
