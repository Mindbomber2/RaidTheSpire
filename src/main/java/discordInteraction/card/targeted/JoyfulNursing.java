package discordInteraction.card.targeted;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.battle.TargetType;
import discordInteraction.card.targetless.AbstractCardTargetless;
import discordInteraction.command.Result;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class JoyfulNursing extends AbstractCardTargeted {
    @Override
    public String getName() {
        return "Joyful Nursing";
    }

    @Override
    public int getCost() {
        return 4;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Heal up to 3 friendly targets for 10 + 10% of their missing health.";
    }

    @Override
    public String getFlavorText() {
        return "Can you really be sure they're different people?";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.support
        };
    }

    @Override
    public int getTargetCountMin() {
        return 1;
    }

    @Override
    public int getTargetCountMax() {
        return 3;
    }

    @Override
    public TargetType[] getTargetTypes() {
        return new TargetType[]{
                TargetType.player,
                TargetType.viewer
        };
    }

    @Override
    protected Result apply(User user, AbstractPlayer player, ArrayList<AbstractCreature> targets) {
        int totalHealed = 0;

        for (AbstractCreature target : targets){
            if (target.isDeadOrEscaped())
                continue;
            int toHeal = 10 + ((target.maxHealth - target.currentHealth) / 10);
            toHeal = Math.min(toHeal, target.maxHealth - target.currentHealth);
            target.heal(toHeal);
            totalHealed += toHeal;
        }

        if (totalHealed > 0)
            return new Result(true, "You healed allies for a total of " + totalHealed + " health!");
        else
            return new Result(false, "You failed to heal anybody, due to targets being either dead or fully healthy.");
    }
}
