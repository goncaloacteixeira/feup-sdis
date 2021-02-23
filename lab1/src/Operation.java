public abstract class Operation {
    protected final String name;

    protected Operation(String name) {
        this.name = name;
    }

    @Override
    public abstract String toString();
}
