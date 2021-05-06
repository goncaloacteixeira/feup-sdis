import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends ServerSocket {
    private final ConcurrentHashMap<String, String> DNSTable = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Server <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        new Server(port).run();
    }

    public Server(int port) throws IOException {
        super(port);
    }

    private void run() throws IOException {
        System.out.println("Accepting Connections on: " + this.getLocalSocketAddress());

        while (true) {
            Socket socket = this.accept();

            new Thread(() -> {
                try {
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String command = in.readLine();
                    Operation operation = Operation.parse(command.split(" "));
                    System.out.println("Received Command");
                    System.out.println(operation);

                    assert operation != null;
                    String result = operation.execute(this.DNSTable);

                    System.out.println("Executed operation");
                    System.out.println("Result: " + result);

                    writer.println(result);
                } catch (IOException e) {
                    try {
                        this.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
