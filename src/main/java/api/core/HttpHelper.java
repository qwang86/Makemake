package api.core;

import api.model.BaseModel;
import api.service.impl.Okhttp3ServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.stream.Collectors;

public class HttpHelper {

    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    public static BaseResponse sendRequest(BaseRequest baseRequest, BaseModel model)  {
        if (baseRequest == null){
            throw new MissingRequestPartException("baseRequest");
        }
        if (model == null){
            throw new MissingRequestPartException("request model");
        }
        String jsonM = JSONObject.toJSONString(model);
        baseRequest.setBody(jsonM);
        logger.warn("current request model is {}", jsonM);
        convert(baseRequest);
        return sendRequest(baseRequest);
    }

    private static void convert(BaseRequest baseRequest) {
        String contentTtpe = baseRequest.getContentTtpe();
        String requestBody = baseRequest.getBody();
        switch (contentTtpe.trim()){
            //针对不同的contexttype，playload（body）需要做处理；
            case "application/x-www-form-urlencoded":
                HashMap<String, String> mapBody = JSONObject.parseObject(requestBody, HashMap.class);
                requestBody = mapBody.keySet().stream()
                        .map(x->x + "=" + URLEncoder.encode(mapBody.get(x)))
                        .collect(Collectors.joining("&"));
                break;
            default:
                break;
        }
        baseRequest.setBody(requestBody);
    }

    public static BaseResponse sendRequest(BaseRequest baseRequest)  {
        logger.info("current request method is =======: {}", baseRequest.getMethod());
        logger.info("current request url is ==========: {}", baseRequest.getUrl());
        logger.info("current request contenttype is===: {}", baseRequest.getContentTtpe());
        logger.info("current request body is==========: {}", baseRequest.getBody());
        BaseResponse response = null;
        try {
            //todo 根据config中http配置引擎决定，由哪个service提供http服务
            response = new Okhttp3ServiceImpl().post(baseRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("current request respone is {}", response.toString());
        return response;
    }
}

