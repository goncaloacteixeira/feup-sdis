public class Register extends Operation {
    private final String IP;

    public Register(String name, String IP) {
        super(name);
        this.IP = IP;
    }

    @Override
    public String toString() {
        return "REGISTER " + this.name + " " + this.IP;
    }
}
