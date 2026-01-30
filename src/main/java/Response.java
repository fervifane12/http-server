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
        StringBuilder sb = new StringBuilder();

        sb.append(httpVersion)
                .append(' ')
                .append(httpCode)
                .append(' ')
                .append(httpCodeName)
                .append("\r\n");

        for (Map.Entry<String, String> header : headers.entrySet()) {
            sb.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }

        sb.append("\r\n");

        if (body != null) {
            sb.append(body);
        }

        return sb.toString();
    }
}