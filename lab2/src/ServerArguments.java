import java.net.InetAddress;

public class ServerArguments {
    private final int servicePort;
    private final InetAddress multicastAddress;
    private final int multicastPort;

    public ServerArguments(int servicePort, InetAddress multicastAddress, int multicastPort) {
        this.servicePort = servicePort;
        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;
    }

    public int getServicePort() {
        return servicePort;
    }

    public InetAddress getMulticastAddress() {
        return multicastAddress;
    }

    public int getMulticastPort() {
        return multicastPort;
    }
}
