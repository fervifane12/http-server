import java.util.Map;
import java.util.Optional;

public record Request(String httpMethod,
                      String path,
                      String httpVersion,
                      Map<String, String> headers,
                      String body) {
    public String getHeader(String key){
        return Optional.ofNullable(headers.get(key)).orElse("");
    }
}