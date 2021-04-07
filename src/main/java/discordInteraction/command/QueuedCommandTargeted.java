package discordInteraction.command;

import com.megacrit.cardcrawl.core.AbstractCreature;
import discordInteraction.card.targeted.AbstractCardTargeted;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class QueuedCommandTargeted extends QueuedCommandBase<AbstractCardTargeted> {
    private ArrayList<AbstractCreature> targets;

    public ArrayList<AbstractCreature> getTargetsList(){
        return targets;
    }

    public ArrayList<AbstractCreature> getTargets() {
        return targets;
    }

    public QueuedCommandTargeted(User player, AbstractCardTargeted card, ArrayList<AbstractCreature> targets){
        super(player, card);

        this.targets = targets;
    }
}
