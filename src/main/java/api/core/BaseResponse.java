package api.core;

import lombok.Data;

import java.util.HashMap;
@Data
public class BaseResponse {

        private int code;
        private String message;
        private HashMap<String, String> heards;
        private String contentTtpe;
        private String body;
}
