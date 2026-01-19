import java.io.OutputStream;
import java.util.Arrays;

public class Parser {

    public Parser() {
    }

    public String parser(String string) {
        String[] splitString = string.split(" ", 0);
        System.out.println(Arrays.toString(splitString));

        if (splitString[1].contains("/echo")) {
            String[] string1 = splitString[1].split("/", 0);
            return "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + string1[2].length() + "\r\n\r\n" + string1[2];
        } else if (splitString[1].equals("/")) {
            return "HTTP/1.1 200 OK\r\n\r\n";
        } else {
            return "HTTP/1.1 404 Not Found\r\n\r\n";
        }
    }
}