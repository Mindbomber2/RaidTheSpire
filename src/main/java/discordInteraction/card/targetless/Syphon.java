package discordInteraction.card.targetless;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import discordInteraction.FlavorType;
import discordInteraction.Main;
import discordInteraction.card.targetless.AbstractCardTargetless;
import discordInteraction.command.Result;
import net.dv8tion.jda.api.entities.User;

public class Syphon extends AbstractCardTargetless {
    @Override
    public String getName() {
        return "Syphon";
    }

    @Override
    public int getCost() {
        return 3;
    }

    @Override
    public String getDescriptionForViewerDisplay() {
        return "Deal 2 damage to all enemies, restoring health to the player equal to the amount of non-blocked damage dealt.";
    }

    @Override
    public String getFlavorText() {
        return "Its like a swarm of vampire bees, except the bees are on strike along with the graphics designer.";
    }

    @Override
    public FlavorType[] getFlavorTypes() {
        return new FlavorType[]{
                FlavorType.support
        };
    }

    @Override
    public Result activate(User user, AbstractPlayer player) {
        int damageDealt = 0;
        for (AbstractMonster monster : Main.battle.getBattleRoom().monsters.monsters) {
            if (monster.isDeadOrEscaped())
                continue;
            if (monster.currentBlock < 2)
                if (monster.currentHealth == 1)
                    damageDealt += 1;
                else
                    damageDealt += 2;
            monster.damage(new DamageInfo(Main.battle.getViewerMonster(user), 2, DamageInfo.DamageType.NORMAL));
        }
        player.heal(damageDealt, true);
        return new Result(true, "You dealt 2 damage to all enemies, and healed " +
                damageDealt + " health on the streamer due to non-blocked damage dealt.");
    }
}
