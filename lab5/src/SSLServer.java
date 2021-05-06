import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class SSLServer {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java SSLServer <port> <cypher-suite>*");
            return;
        }

        final ConcurrentHashMap<String, String> DNSTable = new ConcurrentHashMap<>();

        int port = Integer.parseInt(args[0]);

        SSLServerSocket serverSocket = null;
        SSLServerSocketFactory ssf = null;

        ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        try {
            serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
        } catch (IOException e) {
            System.out.println("Server - Failed to create SSLServerSocket");
            System.out.println(e.getMessage());
            return;
        }

        if (args.length > 1) {
            serverSocket.setEnabledCipherSuites(Arrays.copyOfRange(args, 1, args.length));
        }

        serverSocket.setNeedClientAuth(true);

        System.out.println("Cipher Suites:");
        System.out.println(Arrays.toString(serverSocket.getEnabledCipherSuites()));
        System.out.println("Protocols:");
        System.out.println(Arrays.toString(serverSocket.getEnabledProtocols()));

        System.out.println("Accepting Connections on: " + serverSocket.getLocalSocketAddress());

        while (true) {
            Socket socket = serverSocket.accept();

            new Thread(() -> {
                try {
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String command = in.readLine();
                    Operation operation = Operation.parse(command.split(" "));
                    System.out.println("Received Command");
                    System.out.println(operation);

                    assert operation != null;
                    String result = operation.execute(DNSTable);

                    System.out.println("Executed operation");
                    System.out.println("Result: " + result);

                    writer.println(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
