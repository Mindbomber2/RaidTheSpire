package discordInteraction.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import discordInteraction.Main;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.rooms.AbstractRoom",
        method = "endTurn"
)
public class EndTurn {
    @SpirePrefixPatch
    public static void Prefix(AbstractRoom _instance) {
        Main.commandQueue.handleEndOfPlayerTurnLogic();
    }
}
