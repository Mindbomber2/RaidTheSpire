package discordInteraction.card;

import com.gikk.twirk.types.usernotice.subtype.Ritual;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.Utilities;
import discordInteraction.command.Result;

public class ShowdownDevice extends CardTargetless {
    @Override
    public String getName() {
        return "Showdown Device";
    }

    @Override
    public int getCost() {
        return 6;
    }

    @Override
    public String getDescription() {
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
    public Result activate(AbstractPlayer player) {
        Utilities.applyPower(player, new RitualPower(player, 3, true));
        for (AbstractMonster monster : Main.battle.getBattleRoom().monsters.monsters)
            if (!monster.isDeadOrEscaped())
                Utilities.applyPower(monster, new RitualPower(monster, 3, false));

        return new Result(true, "You have started the showdown device. Ritual 3 has been applied to all targets.");
    }
}
