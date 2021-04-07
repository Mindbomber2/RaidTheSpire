package discordInteraction.card.targetless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.util.Combat;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

public class ToxicAttitude extends AbstractCardTargetless {
    @Override
    public String getName() {
        return "Toxic Attitude";
    }

    @Override
    public int getCost() {
        return 4;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Apply 5 poison to the player and all enemies.";
    }

    @Override
    public String getFlavorText() {
        return "A poisonous attitude is contagious.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.chaos
        };
    }

    @Override
    public Result activate(User user, AbstractPlayer player) {
        PoisonPower power = new PoisonPower(player, player, 5);

        Combat.applyPower(player, power);

        for (AbstractMonster monster : Main.battle.getBattleRoom().monsters.monsters) {
            power = new PoisonPower(monster, Main.battle.getViewerMonster(user), 5);

            Combat.applyPower(monster, power);
        }

        return new Result(true, "You applied 5 poison to all entities in the battle.");
    }
}
