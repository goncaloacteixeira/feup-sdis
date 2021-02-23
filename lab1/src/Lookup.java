public class Lookup extends Operation {
    public Lookup(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "LOOKUP " + name;
    }
}
