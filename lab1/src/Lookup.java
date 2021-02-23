public class Lookup extends Operation {
    public Lookup(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "LOOKUP " + name;
    }

    @Override
    public String execute(Server server) {
        if (server.getDNSTable().containsKey(name)) {
            String ip = server.getDNSTable().get(name);
            System.out.println(this + " : SUCCESS: " + ip);
            return name + " " + ip;
        } else {
            System.out.println(this + " : NAME NOT FOUND");
            return "-1";
        }
    }
}
