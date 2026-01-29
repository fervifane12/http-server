import java.util.Map;

public record Request(String httpMethod,
                      String path,
                      String httpVersion,
                      Map<String, String> headers,
                      String body) {
    public String getHeader(String key){
        return headers.get(key);
    }
}