package api.service;

import api.core.BaseRequest;
import api.core.BaseResponse;

import java.io.IOException;

public interface HttpRequestService {

     BaseResponse get(BaseRequest request) throws IOException;

     BaseResponse post(BaseRequest request) throws IOException;
}
