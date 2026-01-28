
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Handler implements Runnable{
    private final Socket clientSocket;
    private final Parser parser = new Parser();

    public Handler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try  {
            OutputStream out = clientSocket.getOutputStream();

            Request request = parser.parseRequest(clientSocket);
            ResponseBuilder responseBuilder = new ResponseBuilder();
            Response response;

            if (request.path().startsWith("/echo")) {
                response = responseBuilder.withStatus("200", "OK")
                        .withHeaders("Content-Type", "text/plain")
                        .withHeaders("Content-Length", String.valueOf(request.path().split("/")[1].length()))
                        .withBody(request.path().split("/")[1])
                        .buildResponse();
            } else if (request.path().startsWith("/user-agent")) {
                String userAgentValue = request.getHeader("user-agent").trim();
                response = responseBuilder.withStatus("200", "OK")
                        .withHeaders("Content-Type", "text/plain")
                        .withHeaders("Content-Length", String.valueOf(userAgentValue.length()))
                        .withBody(userAgentValue)
                        .buildResponse();
            } else if (request.path().equals("/")) {
                response = responseBuilder.withStatus("200", "OK")
                        .buildResponse();
            } else {
                response = responseBuilder.withStatus("404", "Not Found")
                        .buildResponse();
            }

            out.write(response.toString().getBytes());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
