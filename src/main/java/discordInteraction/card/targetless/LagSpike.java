package discordInteraction.card.targetless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.util.Combat;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

public class LagSpike extends AbstractCardTargetless {
    @Override
    public String getName() {
        return "Lag Spike";
    }

    @Override
    public int getCost() {
        return 3;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Apply Buffer 2 to the player and all enemies.";
    }

    @Override
    public String getFlavorText() {
        return "getFlavorText() took too long to respond: Error 504.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.chaos
        };
    }

    @Override
    public Result activate(User user, AbstractPlayer player) {
        Combat.applyPower(player, new BufferPower(player, 2));
        for(AbstractMonster monster : Main.battle.getBattleRoom().monsters.monsters)
            Combat.applyPower(monster, new BufferPower(monster, 2));
        return new Result(true, "You applied 2 buffer to everyone.");
    }
}
