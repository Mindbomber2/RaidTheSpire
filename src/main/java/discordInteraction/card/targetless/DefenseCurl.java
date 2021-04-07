package discordInteraction.card.targetless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import discordInteraction.FlavorType;
import discordInteraction.util.Combat;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

public class DefenseCurl extends AbstractCardTargetless {
    @Override
    public String getName() {
        return "Defense Curl";
    }

    @Override
    public int getCost() {
        return 6;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Apply Intangible 3 to the player.";
    }

    @Override
    public String getFlavorText() {
        return "Gotta protect those squishy inner bits.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.support
        };
    }

    @Override
    public Result activate(User user, AbstractPlayer player) {
        Combat.applyPower(player, new IntangiblePlayerPower(player, 3));
        return new Result(true, "You've applied 3 stacks of Intangible to the player.");
    }
}
