package discordInteraction.card.targeted;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PoisonPower;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.battle.TargetType;
import discordInteraction.command.Result;
import discordInteraction.util.Combat;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class CorruptingFlames extends AbstractCardTargeted {
    @Override
    public String getName() {
        return "Corrupting Flames";
    }

    @Override
    public int getCost() {
        return 4;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Heal 2 targets for 5 and apply Poison 3.";
    }

    @Override
    public String getFlavorText() {
        return "The flames of corruption can be tempting, but the power gained is only temporary.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.support,
                FlavorType.oppose
        };
    }

    @Override
    public int getTargetCountMin() {
        return 2;
    }

    @Override
    public int getTargetCountMax() {
        return 2;
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
        for (AbstractCreature target : targets){
            target.heal(5);
            Combat.applyPower(target, new PoisonPower(target, Main.battle.getViewerMonster(user), 3));
        }
        return new Result(true, "You drowned your targets in corrupting flames.");
    }
}
