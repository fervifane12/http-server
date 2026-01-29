import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public record StartServer(int port, String baseDirectory) {

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted new connection");
                Thread.startVirtualThread(new Handler(clientSocket, baseDirectory));
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
