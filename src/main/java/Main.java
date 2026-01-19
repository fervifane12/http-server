import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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

            Map<String, String> headers = new HashMap<>();
            String requestLine = reader.readLine();
            String line;

            while ((line = reader.readLine())!=null && !line.isEmpty()){
                int divIndex = line.indexOf(":");
                if (divIndex>0){
                    String headerName = line.substring(0, divIndex).toLowerCase();
                    String value = line.substring(divIndex+1);
                    headers.put(headerName, value);
                }
            }

            Parser parser = new Parser();
            String response = parser.parser(requestLine, headers);
            OutputStream out = socket.getOutputStream();
            out.write(response.getBytes());
            out.flush();

            socket.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
