import java.io.OutputStream;
import java.util.Arrays;

public class Parser {

    public Parser() {
    }

    public String parser(String string){
        String[] splitString = string.split("/");
        for (String stringPart:splitString){
            if (!splitString[1].equals(" HTTP")){
                return "HTTP/1.1 404 Not Found\r\n\r\n";
            } else {
                return "HTTP/1.1 200 OK\r\n\r\n";
            }
        }
        return null;
    }
}
