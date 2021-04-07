package discordInteraction.card.targeted;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.RegenPower;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.battle.TargetType;
import discordInteraction.command.Result;
import discordInteraction.util.Combat;
import net.dv8tion.jda.api.entities.User;

import javax.swing.text.Utilities;
import java.util.ArrayList;

public class PurifyingFlames extends AbstractCardTargeted {
    @Override
    public String getName() {
        return "Purifying Flames";
    }

    @Override
    public int getCost() {
        return 4;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Deal 5 damage to 2 targets. This damage cannot kill the target. Than apply Regen 4.";
    }

    @Override
    public String getFlavorText() {
        return "Burning away impurities is good for the soul, but the smell of burning flesh can sting.";
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
                TargetType.monster,
                TargetType.viewer
        };
    }

    @Override
    protected Result apply(User user, AbstractPlayer player, ArrayList<AbstractCreature> targets) {
        for(AbstractCreature target : targets) {
            int damage = Combat.calculateDamage(Main.battle.getViewerMonster(user), target, 5, DamageInfo.DamageType.NORMAL);

            while (damage >= target.currentHealth + target.currentBlock)
                damage--;

            target.damage(new DamageInfo(target, damage, DamageInfo.DamageType.NORMAL));

            Combat.applyPower(target, new RegenPower(target, 4));
        }
        return new Result(true, "You bathed your targets in purifying flames.");
    }
}
