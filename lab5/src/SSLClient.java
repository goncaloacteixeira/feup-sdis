import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class SSLClient {
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Usage: java SSLClient <host> <port> <oper> <opnd>* <cypher-suite>*");
            return;
        }

        InetAddress hostname = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        Operation operation;
        String[] cipherSuites = null;
        if (args[2].equals("register")) {
            operation = Operation.parse(Arrays.copyOfRange(args, 2, 5));
            if (args.length > 5){
                cipherSuites = Arrays.copyOfRange(args, 5, args.length);
            }
        } else {
            operation = Operation.parse(Arrays.copyOfRange(args, 2, 4));
            if (args.length > 4) {
                cipherSuites = Arrays.copyOfRange(args, 4, args.length);
            }
        }

        assert operation != null;

        System.out.println("Hostname: " + hostname);
        System.out.println("Port: " + port);


        SSLSocket socket = null;
        SSLSocketFactory f = null;
        f = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try {
            socket = (SSLSocket) f.createSocket(hostname, port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (cipherSuites != null) {
            socket.setEnabledCipherSuites(cipherSuites);
        }

        System.out.println("Cipher Suites:");
        System.out.println(Arrays.toString(socket.getEnabledCipherSuites()));
        System.out.println("Protocols:");
        System.out.println(Arrays.toString(socket.getEnabledProtocols()));

        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        writer.println(operation);
        System.out.println("Sent Command");
        String result = in.readLine();

        System.out.println("Received Response");
        System.out.printf("%s :: %s%n", operation, result);
    }
}
