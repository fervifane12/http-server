
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Handler implements Runnable{
    private final Socket clientSocket;
    private final Parser parser = new Parser();

    public Handler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() throws RuntimeException {
        Request request = parser.parseRequest(clientSocket);
        ResponseBuilder responseBuilder = new ResponseBuilder();
        try (OutputStream out = clientSocket.getOutputStream()) {
            if (request.path().startsWith("/echo")) {
                Response response = responseBuilder.withStatus("200", "OK")
                        .withHeaders("Content-Type", "text/plain")
                        .withHeaders("Content-Length", String.valueOf(request.path().split("/")[1].length()))
                        .withBody(request.path().split("/")[1])
                        .buildResponse();
                out.write(response.toString().getBytes());
                out.flush();
            } else if (request.path().startsWith("/user-agent")) {
                String userAgentValue = request.getHeader("user-agent").trim();
                Response response = responseBuilder.withStatus("200", "OK")
                        .withHeaders("Content-Type", "text/plain")
                        .withHeaders("Content-Length", String.valueOf(userAgentValue.length()))
                        .withBody(userAgentValue)
                        .buildResponse();
                out.write(response.toString().getBytes());
                out.flush();
            } else if (request.path().equals("/")) {
                Response response = responseBuilder.withStatus("200", "OK")
                        .buildResponse();
                out.write(response.toString().getBytes());
                out.flush();
            } else {
                Response response = responseBuilder.withStatus("404", "Not Found")
                        .buildResponse();
                out.write(response.toString().getBytes());
                out.flush();
            }
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
