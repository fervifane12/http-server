import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {
    private Response response;
    private String httpVersion = "HTTP/1.1";
    private String httpCode;
    private String httpCodeName;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public ResponseBuilder() {
    }

    public ResponseBuilder withStatus(String code, String name){
        this.httpCode = code;
        this.httpCodeName = name;
        return this;
    }

    public ResponseBuilder withHeaders(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public ResponseBuilder withBody(String body){
        this.body = body;
        return this;
    }

    public Response buildResponse(){
        return new Response(
                httpVersion,
                httpCode,
                httpCodeName,
                headers,
                body
        );
    }
}

