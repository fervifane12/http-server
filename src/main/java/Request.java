import java.util.Map;

public record Request(String httpMethod,
                      String path,
                      String httpVersion,
                      Map<String, String> headers,
                      String body) {
    public String getHeader(String key){
        assert headers.get(key) != null;
        return headers.get(key);
    }
}