package discordInteraction.card.targeted;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import discordInteraction.FlavorType;
import discordInteraction.battle.TargetType;
import discordInteraction.card.targeted.AbstractCardTargeted;
import discordInteraction.card.targetless.AbstractCardTargetless;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class UnPoke extends AbstractCardTargeted {
    @Override
    public String getName() {
        return "UnPoke";
    }

    @Override
    public int getCost() {
        return 1;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Restore 3 health to a target.";
    }

    @Override
    public String getFlavorText() {
        return "A gentle poke can be very refreshing.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.basic
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
                TargetType.player,
                TargetType.monster,
                TargetType.viewer
        };
    }

    @Override
    protected Result apply(User user, AbstractPlayer player, ArrayList<AbstractCreature> targets) {
        AbstractCreature target = targets.get(0);
        target.heal(3, true);
        return new Result(true, "You healed " + target.name + " for 3 health.");
    }
}
