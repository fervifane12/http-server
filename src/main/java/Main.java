import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(4221);

// Since the tester restarts your program quite often, setting SO_REUSEADDR
// ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            Socket socket = serverSocket.accept();
            System.out.println("Accepted new connection");

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Parser parser = new Parser();
            String response = parser.parser(reader.readLine());;
            OutputStream out = socket.getOutputStream();
            out.write(response.getBytes());
            out.flush();

            socket.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
