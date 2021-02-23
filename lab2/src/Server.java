import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    private final int servicePort;
    private final InetAddress multicastAddress;
    private final int multicastPort;

    private DatagramSocket socket;

    private final Map<String, String> DNSTable = new HashMap<>();

    public Map<String, String> getDNSTable() {
        return DNSTable;
    }

    public Server(int servicePort, InetAddress multicastAddress, int multicastPort) {
        this.servicePort = servicePort;
        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Server <srvc_port> <mcast_addr> <mcast_port>");
            return;
        }
        ServerArguments arguments = Server.parseArgs(args);
        if (arguments == null) {
            System.out.println("Error parsing Arguments");
            return;
        }

        Server server = new Server(arguments.getServicePort(), arguments.getMulticastAddress(), arguments.getMulticastPort());

        server.start();
    }

    private static ServerArguments parseArgs(String[] args) {
        try {
            int servicePort = Integer.parseInt(args[0]);
            InetAddress multicastAddr = InetAddress.getByName(args[1]);
            int multicastPort = Integer.parseInt(args[2]);

            return new ServerArguments(servicePort, multicastAddr, multicastPort);
        }
        catch (NumberFormatException | UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void processPacket(DatagramPacket packet) {
        Operation operation = Operation.fromString(new String(packet.getData()));
        if (operation != null) {
            String result = operation.execute(this);
            sendReply(packet, result);
        } else {
            System.out.println("Error Processing Packet");
        }
    }

    private void sendReply(DatagramPacket packet, String reply) {
        byte[] replyBytes = reply.getBytes(StandardCharsets.UTF_8);
        DatagramPacket replyPacket = new DatagramPacket(replyBytes, replyBytes.length, packet.getAddress(), packet.getPort());
        try {
            this.socket.send(replyPacket);
            System.out.println("Sent Reply");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(this.multicastPort);
            multicastSocket.joinGroup(this.multicastAddress);
            String msg = InetAddress.getLocalHost().getHostAddress() + " " + this.servicePort;

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    DatagramPacket multicastPacket = new DatagramPacket(msg.getBytes(), msg.length(), multicastAddress, multicastPort);
                    try {
                        multicastSocket.send(multicastPacket);
                        System.out.println("multicast: " + multicastAddress + ":" + multicastPort + " : " + InetAddress.getLocalHost() + ":" + servicePort);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            // Start sending a periodic Multicast message (1s)
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(timerTask, 0, 1000);
            System.out.println("Multicast Task started");

            this.socket = new DatagramSocket(this.servicePort);
            while (true) {
                int length = 1024;
                DatagramPacket packet = new DatagramPacket(new byte[length], length);

                this.socket.receive(packet);

                System.out.println("Received Packet");
                this.processPacket(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
