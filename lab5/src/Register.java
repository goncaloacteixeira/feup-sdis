import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Register extends Operation {
    public Register(List<String> operands) {
        super(operands);
    }

    @Override
    public String toString() {
        return "register " + String.join(" ", this.operands);
    }

    @Override
    public String execute(ConcurrentHashMap<String, String> DNSTable) {
        DNSTable.put(operands.get(0), operands.get(1));
        return "OK";
    }
}
