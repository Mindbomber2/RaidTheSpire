package discordInteraction.card.targeted;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import discordInteraction.FlavorType;
import discordInteraction.battle.TargetType;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class ArmorUp extends AbstractCardTargeted {
    @Override
    public String getName() {
        return "Armor Up";
    }

    @Override
    public int getCost() {
        return 2;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Apply 3 Block to an enemy, and 9 Block to the player.";
    }

    @Override
    public String getFlavorText() {
        return "You're helping! So what if you make them miss lethal.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.chaos,
                FlavorType.support
        };
    }

    @Override
    public int getTargetCountMin() {
        return 1;
    }

    @Override
    public int getTargetCountMax() {
        return 1;
    }

    @Override
    public TargetType[] getTargetTypes() {
        return new TargetType[]{
                TargetType.monster
        };
    }

    @Override
    public Result apply(User user, AbstractPlayer player, ArrayList<AbstractCreature> targets) {
        AbstractCreature target = targets.get(0);
        target.addBlock(3);
        player.addBlock(9);

        return new Result(true, "You applied 3 armor to " + target.name + " and 9 armor to the streamer.");
    }
}
