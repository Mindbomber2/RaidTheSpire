package discordInteraction.card.triggered.onPlayerDamage;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.card.triggered.TriggerTimingType;
import discordInteraction.command.ResultWithInt;
import net.dv8tion.jda.api.entities.User;

public class RiskingItAllForAFriend extends AbstractCardTriggeredOnPlayerDamage {
    @Override
    public String getName() {
        return "Risking it all for a Friend";
    }

    @Override
    public int getCost() {
        return 4;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "For the rest of this fight, you cannot play additional cards, but any damage that the player would take is instead reflected to you. If multiple viewers play this, a random viewer takes each source of damage.";
    }

    @Override
    public String getDescriptionForGameDisplay() {
        return "For the rest of this fight, this viewer will take any unblocked damage the player would take.";
    }

    @Override
    public String getFlavorText() {
        return "Sometimes sacrifices must be made.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.support
        };
    }

    @Override
    public ResultWithInt handleOnPlayerDamageTrigger(int incomingDamage, DamageInfo damageInfo, AbstractPlayer player, User user) {
        if (incomingDamage <= player.currentBlock)
            return new ResultWithInt(false, "The player's block protected them.", incomingDamage);
        int damageToTake = incomingDamage;
        AbstractCreature viewer = Main.battle.getViewerMonster(user);
        if (player.currentBlock > 0) {
            damageToTake -= player.currentBlock;
            if (damageToTake < viewer.currentHealth) {
                Main.battle.getViewerMonster(user).damage(new DamageInfo(damageInfo.owner, damageToTake, DamageInfo.DamageType.NORMAL));
                return new ResultWithInt(true, "You absorbed " + damageToTake + " of the incoming damage.", incomingDamage - damageToTake);
            } else {
                Main.battle.getViewerMonster(user).damage(new DamageInfo(damageInfo.owner, damageToTake, DamageInfo.DamageType.NORMAL));
                Main.battle.removeViewerMonster(user, true);
                return new ResultWithInt(true, "You took fatal damage that would have hit the player, absorbing " + damageToTake + " damage that their block would not have stopped.", incomingDamage - damageToTake);
            }
        }
        if (damageToTake < viewer.currentHealth) {
            Main.battle.getViewerMonster(user).damage(new DamageInfo(damageInfo.owner, damageToTake, DamageInfo.DamageType.NORMAL));
            return new ResultWithInt(true, "You absorbed " + damageToTake + " incoming damage.", 0);
        } else {
            Main.battle.getViewerMonster(user).damage(new DamageInfo(damageInfo.owner, damageToTake, DamageInfo.DamageType.NORMAL));
            Main.battle.removeViewerMonster(user, true);
            return new ResultWithInt(true, "You took fatal damage that would have hit the player, absorbing " + damageToTake + " damage before you went down.", 0);
        }
    }

    @Override
    public TriggerTimingType getTriggerTimingType() {
        return TriggerTimingType.infinite;
    }
}
