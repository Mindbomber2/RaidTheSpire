package discordInteraction.card.targeted;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.util.Combat;
import discordInteraction.battle.TargetType;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class Poke extends AbstractCardTargeted {
    @Override
    public String getName() {
        return "Poke";
    }

    @Override
    public int getCost() {
        return 1;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Deal 3 damage to a target.";
    }

    @Override
    public String getFlavorText() {
        return "Nothing like a good hard bonk to annoy somebody.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.basic
        };
    }

    @Override
    public int getTargetCountMin() {
        return 1;
    }

    @Override
    public int getTargetCountMax() {
        return 1;
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
    public Result apply(User user, AbstractPlayer player, ArrayList<AbstractCreature> targets) {
        AbstractCreature target = targets.get(0);
        int damageDealt = Combat.applyDamage(Main.battle.getViewerMonster(user), target, 3, DamageInfo.DamageType.NORMAL);
        return new Result(true, "You dealt " + damageDealt + " damage to " + target.name + ".");
    }
}
