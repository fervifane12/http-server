import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Response {
    private final String httpVersion;
    private final String httpCode;
    private final String httpCodeName;
    private final Map<String, String> headers;
    private final String body;

    public Response(String httpVersion, String httpCode, String httpCodeName, Map<String, String> headers, String body) {
        this.httpVersion = httpVersion;
        this.httpCode = httpCode;
        this.httpCodeName = httpCodeName;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder headers = new StringBuilder();
        if (!headers.isEmpty()){
            for (Map.Entry<String, String> header : this.headers.entrySet()){
                headers.append(header.getKey())
                        .append(": ")
                        .append(header.getValue())
                        .append("\r\n");
            }
        } else {
            headers.append("\r\n");
        }

        return  httpVersion + ' ' +
                httpCode + ' ' +
                httpCodeName + "\r\n" +
                headers + "\r\n" +
                (body!=null ? body : "");
    }
}