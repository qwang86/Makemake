package api.core;

import lombok.Data;
import java.util.HashMap;

@Data
public class BaseRequest {

    private String url;

    private String method;

    private HashMap<String, String> headers;

    private String contentTtpe;

    private HashMap<String, String> parameters;

    private String body;
}
