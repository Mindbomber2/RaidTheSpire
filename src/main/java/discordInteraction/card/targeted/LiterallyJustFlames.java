package discordInteraction.card.targeted;

import basemod.patches.com.megacrit.cardcrawl.relics.AbstractRelic.RelicOutlineColor;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.battle.Battle;
import discordInteraction.battle.TargetType;
import discordInteraction.command.Result;
import discordInteraction.util.Combat;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class LiterallyJustFlames extends AbstractCardTargeted {
    @Override
    public String getName() {
        return "Literally Just Flames";
    }

    @Override
    public int getCost() {
        return 4;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Deal 6 damage to 2 targets.";
    }

    @Override
    public String getFlavorText() {
        return "Sometimes flames just burn.";
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
            Combat.applyDamage(Main.battle.getViewerMonster(user), target, 6, DamageInfo.DamageType.NORMAL);
        }
        return new Result(true, "You burnt your targets.");
    }
}
