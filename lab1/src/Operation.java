public abstract class Operation {
    protected final String name;

    protected Operation(String name) {
        this.name = name;
    }

    @Override
    public abstract String toString();

    public static Operation fromString(String operation) {
        String[] keywords = operation.split(" ");
        Operation ret = null;
        switch (keywords[0].trim()) {
            case "REGISTER":
                ret = new Register(keywords[1].trim(), keywords[2].trim());
                break;
            case "LOOKUP":
                ret = new Lookup(keywords[1].trim());
                break;
            default:
                break;
        }
        return ret;
    }

    public abstract String execute(Server server);
}
