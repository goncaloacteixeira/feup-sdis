import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Operation {
    protected List<String> operands = new ArrayList<>();

    public Operation(List<String> operands) {
        this.operands = operands;
    }

    public abstract String toString();

    public abstract String execute(ConcurrentHashMap<String, String> DNSTable);

    public static Operation parse(String[] command) {
        if (command[0].equals("register")) {
            return new Register(Arrays.asList(command[1].trim(), command[2].trim()));
        } else if (command[0].equals("lookup")) {
            return new Lookup(Collections.singletonList(command[1].trim()));
        }
        return null;
    }
}
