package discordInteraction.card;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import discordInteraction.FlavorType;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class Antidote extends CardTargetless {
    @Override
    public String getName() {
        return "Antidote";
    }

    @Override
    public int getCost() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "Remove all poison stacks from the player and heal for 5 + X health, where X is equal to the number of poison stacks removed times 2.";
    }

    @Override
    public String getFlavorText() {
        return "Nothing a little homebrew can't fix.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.support
        };
    }

    @Override
    public Result activate(User user, AbstractPlayer player) {
        int poison = 0;
        if (player.hasPower(PoisonPower.POWER_ID)) {
            AbstractPower power = player.getPower(PoisonPower.POWER_ID);
            poison = power.amount;
            power.reducePower(poison);
        }
        int toHeal = 5 + (poison * 2);
        player.heal(5 + (toHeal));
        return new Result(true, "You removed any existing poison effects from the player and restored " + toHeal + " health.");
    }
}
