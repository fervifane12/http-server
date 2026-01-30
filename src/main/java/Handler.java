import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Handler implements Runnable{
    private final Socket clientSocket;
    private final String baseDirectory;
    private final Parser parser = new Parser();

    public Handler(Socket clientSocket, String baseDirectory) {
        this.clientSocket = clientSocket;
        this.baseDirectory = baseDirectory;
    }

    @Override
    public void run() {

        try  {
            OutputStream out = clientSocket.getOutputStream();

            Request request = parser.parseRequest(clientSocket);
            ResponseBuilder responseBuilder = new ResponseBuilder();
            Response response;

            while (true){
                boolean usesGzip = request.getHeader("accept-encoding").contains("gzip");

                if (request.path().startsWith("/echo")) {
                    if (usesGzip) {

                        byte[] echoText = parser.gzipCompress(request.path().substring("/echo/".length()));

                        response = responseBuilder.withStatus("200", "OK")
                                .withHeaders("Content-Encoding", "gzip")
                                .withHeaders("Content-Type", "text/plain")
                                .withHeaders("Content-Length", String.valueOf(echoText.length))
                                .buildResponse();
                        out.write(response.toString().getBytes(StandardCharsets.UTF_8));
                        out.write(echoText);
                        out.flush();
                    } else {
                        response = responseBuilder.withStatus("200", "OK")
                                .withHeaders("Content-Type", "text/plain")
                                .withHeaders("Content-Length", String.valueOf(request.path().split("/")[2].length()))
                                .withBody(request.path().split("/")[2])
                                .buildResponse();
                    }

                } else if (request.path().startsWith("/user-agent")) {
                    String userAgentValue = request.getHeader("user-agent").trim();
                    response = responseBuilder.withStatus("200", "OK")
                            .withHeaders("Content-Type", "text/plain")
                            .withHeaders("Content-Length", String.valueOf(userAgentValue.getBytes(StandardCharsets.UTF_8).length))
                            .withBody(userAgentValue)
                            .buildResponse();
                } else if (request.path().equals("/")) {
                    response = responseBuilder.withStatus("200", "OK")
                            .buildResponse();
                } else if (request.path().startsWith("/files") && request.httpMethod().equals("GET")) {
                    String fileName = request.path().substring("/files/".length());
                    Path filePath = Paths.get(baseDirectory, fileName);

                    if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                        byte[] fileContent = Files.readAllBytes(filePath);
                        response = responseBuilder.withStatus("200", "OK")
                                .withHeaders("Content-Type", "application/octet-stream")
                                .withHeaders("Content-Length", String.valueOf(fileContent.length))
                                .withBody(new String(fileContent, StandardCharsets.UTF_8))
                                .buildResponse();
                    } else {
                        response = responseBuilder.withStatus("404", "Not Found")
                                .buildResponse();
                    }
                } else if (request.httpMethod().equals("POST") && request.path().startsWith("/files")) {
                    String fileName = request.path().substring("/files/".length());
                    Path filePath = Paths.get(baseDirectory, fileName);

                    Files.createDirectories(Paths.get(baseDirectory));
                    Files.writeString(filePath, request.body());
                    response = responseBuilder.withStatus("201", "Created")
                            .buildResponse();

                } else {
                    response = responseBuilder.withStatus("404", "Not Found")
                            .buildResponse();
                }

                if (usesGzip && !response.toString().contains("Content-Encoding")) {
                    response = responseBuilder.withHeaders("Content-Encoding", "gzip")
                            .buildResponse();

                }

                assert response != null;
                out.write(response.toString().getBytes());
                out.flush();

                String connection = request.getHeader("connection");
                if (connection != null && connection.equals("close")) {
                    System.out.println(response);
                    break;
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
