import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Client {
    private DatagramSocket socket;
    private final int multicastPort;
    private final InetAddress multicastAddress;
    private InetAddress serviceAddress;
    private int servicePort;
    private final Operation operation;

    public Client(int multicastPort, InetAddress multicastAddress, Operation operation) {
        this.multicastPort = multicastPort;
        this.multicastAddress = multicastAddress;
        this.operation = operation;
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java Client <mcast_addr> <mcast_port> <oper> <opnd> *");
            return;
        }
        ClientArguments clientArguments = Client.parseArgs(args);
        if (clientArguments == null) {
            System.out.println("Error parsing Arguments");
            return;
        }

        Client client = new Client(clientArguments.getMulticastPort(), clientArguments.getMulticastAddress(), clientArguments.getOperation());
        client.start();
    }

    private static ClientArguments parseArgs(String[] args) {
        try {
            InetAddress multicastAdress = InetAddress.getByName(args[0]);
            int multicastPort = Integer.parseInt(args[1]);
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
            return new ClientArguments(multicastAdress, multicastPort, operation);

        } catch (NumberFormatException e) {
            System.out.println("Error on port number");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseMulticastResponse(String data) {
        String[] split = data.split(" ");
        try {
            this.serviceAddress = InetAddress.getByName(split[0].trim());
            this.servicePort = Integer.parseInt(split[1].trim());
        } catch (UnknownHostException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        try {
            // get the service address and port
            System.out.println("Trying to receive multicast reply from " + this.multicastAddress + ":" + this.multicastPort);
            MulticastSocket s = new MulticastSocket(this.multicastPort);
            s.joinGroup(this.multicastAddress);
            DatagramPacket recv = new DatagramPacket(new byte[1024], 1024);
            s.receive(recv);
            this.parseMulticastResponse(new String(recv.getData()));
            s.leaveGroup(this.multicastAddress);

            System.out.println("multicast: " + this.multicastAddress + ":" + this.multicastPort + " : " + this.serviceAddress + ":" + this.servicePort);

            byte[] buffer = this.operation.toString().getBytes(StandardCharsets.UTF_8);

            this.socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.serviceAddress, this.servicePort);

            this.socket.send(packet);
            System.out.println("Sent request!");

            this.socket.setSoTimeout(2000);

            DatagramPacket reply = new DatagramPacket(new byte[1024], 1024);
            this.socket.receive(reply);

            System.out.println("Reply Received");
            System.out.println(this.operation + " :: " + new String(reply.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
