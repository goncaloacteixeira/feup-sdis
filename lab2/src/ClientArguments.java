import java.net.InetAddress;

public class ClientArguments {
    private final InetAddress address;
    private final int port;
    private final Operation operation;

    public ClientArguments(InetAddress address, int port, Operation operation) {
        this.address = address;
        this.port = port;
        this.operation = operation;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "Address: " + address + "\nPort: " + port + "\nOperation: " + operation;
    }
}
