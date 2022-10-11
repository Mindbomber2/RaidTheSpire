package discordInteraction.cardLogic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import discordInteraction.battleTimer.PlayerCountdownPatch;

public class HoldCardAction extends AbstractGameAction {
    private AbstractCard card;
    private AbstractMonster monster;
    private int energyOnUse;

    public HoldCardAction(AbstractCard c, AbstractMonster m, int e) {
        this.card = c;
        this.monster = m;
        this.energyOnUse = e;
    }

    @Override
    public void update() {
        if (PlayerCountdownPatch.patchIntoTimer.canPlayCard.get(AbstractDungeon.player) == true) {
            AbstractDungeon.player.useCard(card, monster, energyOnUse);
            PlayerCountdownPatch.patchIntoTimer.canPlayCard.set(AbstractDungeon.player, false);
        } else {
            AbstractDungeon.actionManager.addToBottom(new HoldCardAction(card, monster, energyOnUse));
            card.target_x = Settings.WIDTH/2;
            card.target_y = Settings.HEIGHT/2;
        }
        this.isDone=true;
    }
}
