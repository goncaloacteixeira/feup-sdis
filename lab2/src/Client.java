import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Client {
    private DatagramSocket socket;
    private final int PORT;
    private final InetAddress address;
    private final Operation operation;

    public Client(int PORT, InetAddress address, Operation operation) {
        this.PORT = PORT;
        this.address = address;
        this.operation = operation;
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java Client <host> <port> <oper> <opnd>*");
            return;
        }
        ClientArguments clientArguments = Client.parseArgs(args);
        if (clientArguments == null) {
            System.out.println("Error parsing Arguments");
            return;
        }
        System.out.println("Client Arguments parsed");
        System.out.println(clientArguments);

        Client client = new Client(clientArguments.getPort(), clientArguments.getAddress(), clientArguments.getOperation());
        client.start();
    }

    private static ClientArguments parseArgs(String[] args) {
        try {
            InetAddress address = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);
            Operation operation;
            switch (args[2]) {
                case "REGISTER":
                    operation = new Register(args[3], args[4]);
                    break;
                case "LOOKUP":
                    operation = new Lookup(args[3]);
                    break;
                default:
                    throw new Exception("Error on operation code");
            }
            return new ClientArguments(address, port, operation);

        } catch (NumberFormatException e) {
            System.out.println("Error on port number");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void start() {
        try {
            byte[] buffer = this.operation.toString().getBytes(StandardCharsets.UTF_8);

            this.socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.address, this.PORT);

            this.socket.send(packet);
            System.out.println("Sent packet!");

            this.socket.setSoTimeout(2000);

            DatagramPacket reply = new DatagramPacket(new byte[1024], 1024);
            this.socket.receive(reply);

            System.out.println("Reply Received");
            System.out.println(new String(reply.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
