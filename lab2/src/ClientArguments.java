import java.net.InetAddress;

public class ClientArguments {
    private final InetAddress multicastAddress;
    private final int multicastPort;
    private final Operation operation;

    public ClientArguments(InetAddress multicastAddress, int multicastPort, Operation operation) {
        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;
        this.operation = operation;
    }

    public InetAddress getMulticastAddress() {
        return multicastAddress;
    }

    public int getMulticastPort() {
        return multicastPort;
    }

    public Operation getOperation() {
        return operation;
    }
}
