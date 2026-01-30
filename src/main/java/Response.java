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
        StringBuilder stringHeaders = new StringBuilder();
        if (!this.headers.isEmpty()){
            for (Map.Entry<String, String> header : this.headers.entrySet()){
                stringHeaders.append(header.getKey())
                        .append(": ")
                        .append(header.getValue())
                        .append("\r\n");
            }
        } else {
            stringHeaders.append("\r\n");
        }

        return  httpVersion + ' ' +
                httpCode + ' ' +
                httpCodeName + "\r\n" +
                stringHeaders + "\r\n" +
                (body!=null ? body : "");
    }
}