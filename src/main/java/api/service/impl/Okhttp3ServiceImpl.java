package api.service.impl;

import api.core.BaseRequest;
import api.core.BaseResponse;
import api.service.HttpRequestService;
import okhttp3.*;
import okhttp3.Request.Builder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Okhttp3ServiceImpl implements HttpRequestService {
    private OkHttpClient client;
	private Builder requestBuilder;
	private RequestBody body;

    class HttpProxy{
    protected Proxy proxy;

        public HttpProxy(Type type, String host, int port)
        {
            switch (type)
            {
                case HTTP:
                    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
                    break;
                default:
                    break;
            }
        }

    }
    private enum Type
    {
        HTTP;
    }
	public Okhttp3ServiceImpl()
	{
		this(120, null);
	}

	private Okhttp3ServiceImpl(int timeout, HttpProxy httpProxy)
	{
		if (httpProxy == null)
		{
			client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS)
					.readTimeout(timeout, TimeUnit.SECONDS).writeTimeout(timeout, TimeUnit.SECONDS).build();
		}
		else
		{
			client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS)
					.readTimeout(timeout, TimeUnit.SECONDS).writeTimeout(timeout, TimeUnit.SECONDS)
					.proxy(httpProxy.proxy).build();
		}
		requestBuilder = new Builder();
	}

    @Override
    public BaseResponse get(BaseRequest request) throws IOException {
        init(request);
        BaseResponse baseResponse = new BaseResponse();
        requestBuilder.get();
        Response response = client.newCall(requestBuilder.build()).execute();
        convert(response, baseResponse);
        return baseResponse;
    }

    @Override
    public BaseResponse post(BaseRequest request) throws IOException {
        init(request);
        BaseResponse baseResponse = new BaseResponse();
        String contextTtpe = request.getContentTtpe();
        String content = request.getBody();
        setBody(content, contextTtpe);
        requestBuilder.post(this.body);
        Response response = client.newCall(requestBuilder.build()).execute();
        convert(response, baseResponse);
        return baseResponse;
    }

    private void convert(Response response, BaseResponse baseResponse) throws IOException {
        baseResponse.setCode(response.code());
        baseResponse.setMessage(response.message());
        baseResponse.setContentTtpe(response.body().contentType().toString());
        baseResponse.setBody(response.body().string());
    }

    private void setBody(String content, String contentType)  {
        if (contentType == null || contentType.isEmpty())
        {
            body = RequestBody.create(content, null);
        }
        else
        {
            body = RequestBody.create(content, MediaType.get(contentType));
        }
    }

    private void init(BaseRequest request){
        String url = request.getUrl();
        requestBuilder.url(url);
        HashMap<String, String> requestHeaders = request.getHeaders();
        if (requestHeaders != null && !requestHeaders.isEmpty()){
            requestBuilder.headers(Headers.Companion.of(requestHeaders));
        }
    }
}
