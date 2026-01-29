import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    public Parser() {
    }

    public Request parseRequest(Socket clientSocket) {
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String[] splitRequest = extractSplittedString(reader);
            String[] requestLine = splitRequest[0].split(" ");
            Map<String, String> headers = extractHeaders(splitRequest);
            String httpMethod = requestLine[0];
            String path= requestLine[1];
            String httpVersion = requestLine[2];

            int contentLength = Integer.parseInt(headers.get("content-length"));
            System.out.println(contentLength);
            char[] bodyChars = new char[contentLength];

            reader.read(bodyChars, 0, contentLength);
            String body = new String(bodyChars);

            return new Request(httpMethod,
                    path,
                    httpVersion,
                    headers,
                    body);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] extractSplittedString(BufferedReader reader) throws IOException {
        StringBuilder requestString = new StringBuilder();
        String line;
        while ((line = reader.readLine())!=null && !line.isEmpty()){
            requestString.append(line).append("\r\n");
        }

        return requestString.toString().split("\\r\\n");
    }

    public Map<String, String> extractHeaders(String[] splitRequest){
        Map<String, String> headers = new HashMap<>();
        for (int i = 0; i<splitRequest.length; i++){
            if (i>0 && !splitRequest[i].isEmpty()){
                int divIndex = splitRequest[i].indexOf(":");
                if (divIndex>0){
                    String headerName = splitRequest[i].substring(0, divIndex).toLowerCase();
                    String value = splitRequest[i].substring(divIndex + 1).trim();
                    headers.put(headerName, value);
                }
            }
        }
        return headers;
    }


}