package discordInteraction.card.targetless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.util.Combat;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Panacea extends AbstractCardTargetless {
    @Override
    public String getName() {
        return "Panacea";
    }

    @Override
    public int getCost() {
        return 3;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Apply Artifact 3 to a random target. Can potentially hit the player.";
    }

    @Override
    public String getFlavorText() {
        return "Should have used less slippery glass for such an important potion.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.chaos
        };
    }

    @Override
    public Result activate(User user, AbstractPlayer player) {
        ArrayList<AbstractCreature> targets = new ArrayList<AbstractCreature>();
        targets.add(player);
        for(AbstractMonster monster : Main.battle.getBattleRoom().monsters.monsters)
            if (!monster.isDeadOrEscaped())
                targets.add(monster);

        AbstractCreature target = targets.get(ThreadLocalRandom.current().nextInt(targets.size()));

        Combat.applyPower(target, new ArtifactPower(target, 3));

        return new Result(true, "You applied 3 Artifact to " + target.name + ".");
    }
}
