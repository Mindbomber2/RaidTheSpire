package discordInteraction.command;

public class ResultWithInt extends Result {
    private int returnInt;

    public int getReturnInt() {
        return returnInt;
    }

    public ResultWithInt(Boolean resolved, String whatHappened, int valueToReturn) {
        super(resolved, whatHappened);
        this.returnInt = valueToReturn;
    }
}
