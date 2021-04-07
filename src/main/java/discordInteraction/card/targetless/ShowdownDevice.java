package discordInteraction.card.targetless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.util.Combat;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

public class ShowdownDevice extends AbstractCardTargetless {
    @Override
    public String getName() {
        return "Showdown Device";
    }

    @Override
    public int getCost() {
        return 6;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Apply Ritual 3 to the player and all enemies.";
    }

    @Override
    public String getFlavorText() {
        return "One way or another, things are about to go down.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.chaos
        };
    }

    @Override
    public Result activate(User user, AbstractPlayer player) {
        Combat.applyPower(player, new RitualPower(player, 3, true));
        for (AbstractMonster monster : Main.battle.getBattleRoom().monsters.monsters)
            if (!monster.isDeadOrEscaped())
                Combat.applyPower(monster, new RitualPower(monster, 3, false));

        return new Result(true, "You have started the showdown device. Ritual 3 has been applied to all targets.");
    }
}
