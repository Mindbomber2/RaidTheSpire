package discordInteraction.card.targeted;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ThornsPower;
import discordInteraction.FlavorType;
import discordInteraction.util.Combat;
import discordInteraction.battle.TargetType;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class Caltrops extends AbstractCardTargeted {
    @Override
    public String getName() {
        return "Caltrops";
    }

    @Override
    public int getCost() {
        return 4;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Apply Thorns 2 to an enemy.";
    }

    @Override
    public String getFlavorText() {
        return "Watch your step.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.oppose
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
    protected Result apply(User user, AbstractPlayer player, ArrayList<AbstractCreature> targets) {
        Combat.applyPower(targets.get(0), new ThornsPower(targets.get(0), 2));
        return new Result(true, "You applied 2 thorns to " + targets.get(0).name);
    }
}
