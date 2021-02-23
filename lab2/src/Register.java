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

    @Override
    public String execute(Server server) {
        if (server.getDNSTable().containsKey(name)) {
            System.out.println(this + " :: -1");
            return "-1";
        }
        else {
            server.getDNSTable().put(name, IP);
            System.out.println(this + " :: " + (server.getDNSTable().keySet().size() - 1));
            return String.valueOf(server.getDNSTable().keySet().size() - 1);
        }
    }
}
