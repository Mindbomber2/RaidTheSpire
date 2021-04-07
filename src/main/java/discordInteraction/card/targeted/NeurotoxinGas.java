package discordInteraction.card.targeted;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PoisonPower;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.util.Combat;
import discordInteraction.battle.TargetType;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class NeurotoxinGas extends AbstractCardTargeted {
    @Override
    public String getName() {
        return "Neurotoxin Gas";
    }

    @Override
    public int getCost() {
        return 1;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Apply 3 Poison to a target.";
    }

    @Override
    public String getFlavorText() {
        return "Oh look, it's your old friend; deadly neurotoxin.";
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
                TargetType.viewer,
                TargetType.monster
        };
    }

    @Override
    protected Result apply(User user, AbstractPlayer player, ArrayList<AbstractCreature> targets) {
        AbstractCreature target = targets.get(0);
        Combat.applyPower(target, new PoisonPower(target, Main.battle.getViewerMonster(user), 3));

        return new Result(true, "You applied 3 poison to " + target.name + ", for science of course.");
    }
}
