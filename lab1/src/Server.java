import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final int PORT;
    private DatagramSocket socket;
    private final Map<String, String> DNSTable = new HashMap<>();

    public Map<String, String> getDNSTable() {
        return DNSTable;
    }

    public Server(int port) {
        this.PORT = port;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Server <port number>");
            return;
        }
        Server server = new Server(parseArgs(args));

        server.start();
    }

    private static int parseArgs(String[] args) {
        try {
            return Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e) {
            System.out.println("Error on Port Designation");
            return -1;
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
            this.socket = new DatagramSocket(this.PORT);
            System.out.println("Local Address: " + this.socket.getLocalAddress());
            System.out.println("Local Port: " + this.socket.getLocalPort());

            while (true) {
                int length = 1024;
                DatagramPacket packet = new DatagramPacket(new byte[length], length);

                this.socket.receive(packet);

                System.out.println("Received Packet!");
                this.processPacket(packet);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
