package discordInteraction.card.triggered.onPlayerDamage;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import discordInteraction.FlavorType;
import discordInteraction.card.targeted.Poke;
import discordInteraction.card.targeted.UnPoke;
import discordInteraction.card.triggered.TriggerTimingType;
import discordInteraction.command.ResultWithInt;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class DeathByAThousandPokes extends AbstractCardTriggeredOnPlayerDamage {
    @Override
    public String getName() {
        return "Death By A Thousand Pokes";
    }

    @Override
    public int getCost() {
        return 6;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "For the rest of this fight, you cannot play additional cards, but any time the player takes damage, you will cast a free Poke, aimed at the attacker, and a free UnPoke, aimed at the player.";
    }

    @Override
    public String getDescriptionForGameDisplay() {
        return "For the rest of this fight, whenever the player is attacked, this viewer will cast a free Poke, aimed at the attacker, and a free UnPoke, aimed at the player.";
    }

    @Override
    public String getFlavorText() {
        return "This death will not be swift.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.support
        };
    }

    @Override
    public ResultWithInt handleOnPlayerDamageTrigger(int incomingDamage, DamageInfo damageInfo, AbstractPlayer player, User user) {
        ArrayList<AbstractCreature> attacker = new ArrayList<>();
        if (damageInfo.owner == null) {
            new Poke().activate(user, player);
        } else {
            attacker.add(damageInfo.owner);
            new Poke().activate(user, player, attacker);
        }
        ArrayList<AbstractCreature> toHeal = new ArrayList<>();
        toHeal.add(player);
        new UnPoke().activate(user, player, toHeal);

        return new ResultWithInt(true, "You cast a free Poke and UnPoke.", incomingDamage);
    }

    @Override
    public TriggerTimingType getTriggerTimingType() {
        return TriggerTimingType.infinite;
    }
}
