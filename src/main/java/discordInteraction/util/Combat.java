package discordInteraction.util;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Combat {
    // Shortcuts
    public static void applyPower(AbstractCreature target, AbstractPower power) {
        ApplyPowerAction action = new ApplyPowerAction(target, target, power);
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static int calculateDamage(AbstractCreature source, AbstractCreature target, int damageBase, DamageInfo.DamageType damageType) {
        float rounded = (float) damageBase;

        for (AbstractPower power : source.powers) {
            rounded = power.atDamageGive(rounded, damageType);
        }

        for (AbstractPower power : target.powers) {
            rounded = power.atDamageReceive(rounded, damageType);
        }

        for (AbstractPower power : source.powers) {
            rounded = power.atDamageFinalGive(rounded, damageType);
        }

        for (AbstractPower power : target.powers) {
            rounded = power.atDamageFinalReceive(rounded, damageType);
        }

        if (rounded < 0.0f) {
            rounded = 0.0f;
        }

        return MathUtils.floor(rounded);
    }

    public static int applyDamage(AbstractCreature source, AbstractCreature target, int damageBase, DamageInfo.DamageType damageType) {
        int flat = calculateDamage(source, target, damageBase, damageType);

        target.damage(new DamageInfo(source, flat, damageType));

        return flat;
    }
}
