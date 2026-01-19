import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

public class Parser {

    public Parser() {
    }

    public String parser(String requestLine, Map<String, String> headers) {
        String[] splitReqLine = requestLine.split(" ", 0);

        if (splitReqLine[1].startsWith("/echo")) {
            String[] string1 = splitReqLine[1].split("/", 0);
            return "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\n" +
                    "Content-Length: " + string1[2].trim().length() + "\r\n\r\n"
                    + string1[2].trim();

        } else if (splitReqLine[1].startsWith("/user-agent")) {
            return "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\n" +
                    "Content-Length: " + headers.get("User-Agent").length() + "\r\n\r\n" +
                    headers.get("User-Agent");
        } else if (splitReqLine[1].equals("/")) {
            return "HTTP/1.1 200 OK\r\n\r\n";

        } else {
            return "HTTP/1.1 404 Not Found\r\n\r\n";
        }
    }
}