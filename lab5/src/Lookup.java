import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Lookup extends Operation {
    public Lookup(List<String> strings) {
        super(strings);
    }

    @Override
    public String toString() {
        return "lookup " + this.operands.get(0);
    }

    @Override
    public String execute(ConcurrentHashMap<String, String> DNSTable) {
        if (DNSTable.containsKey(this.operands.get(0))) {
            return DNSTable.get(this.operands.get(0));
        }
        return "ERROR";
    }
}
