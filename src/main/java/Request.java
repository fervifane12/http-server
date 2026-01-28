import java.util.Map;

public record Request(String httpMethod,
                      String path,
                      String httpVersion,
                      Map<String, String> headers) {
    public String getHeader(String key){
        return this.headers.get(key);
    }
}