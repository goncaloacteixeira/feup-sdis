import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Usage: java Client <host_name> <port_number> <oper> <opnd> *");
            return;
        }

        InetAddress hostname = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        Operation operation = Operation.parse(Arrays.copyOfRange(args, 2, args.length));

        assert operation != null;

        System.out.println("Hostname: " + hostname);
        System.out.println("Port: " + port);

        Socket client = new Socket(hostname, port);
        PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        writer.println(operation);
        System.out.println("Sent Command");
        String result = in.readLine();

        System.out.println("Received Response");
        System.out.printf("%s :: %s%n", operation, result);
    }
}
