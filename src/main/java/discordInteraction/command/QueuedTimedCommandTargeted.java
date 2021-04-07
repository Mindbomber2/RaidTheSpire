package discordInteraction.command;

import com.megacrit.cardcrawl.core.AbstractCreature;
import discordInteraction.card.targeted.AbstractCardTargeted;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class QueuedTimedCommandTargeted extends QueuedCommandBase<AbstractCardTargeted> {
    public float timer;
    private ArrayList<AbstractCreature> targets;

    public ArrayList<AbstractCreature> getTargetsList(){
        return targets;
    }

    public ArrayList<AbstractCreature> getTargets() {
        return targets;
    }

    public QueuedTimedCommandTargeted(User player, AbstractCardTargeted card, ArrayList<AbstractCreature> targets, float timer){
        super(player, card);
        this.timer = timer;
        this.targets = targets;
    }

    public void reduceTimer(){
        this.timer-=1;
    }
}
