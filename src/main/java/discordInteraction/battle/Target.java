package discordInteraction.battle;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class Target {
    private AbstractCreature target;
    private TargetType type;

    public AbstractCreature getTarget(){
        return target;
    }

    public TargetType getTargetType(){
        return  type;
    }

    public Target(AbstractCreature target, TargetType type){
        this.target = target;
        this.type = type;
    }
}
