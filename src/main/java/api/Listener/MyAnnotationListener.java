package api.Listener;

import api.annotation.TestApi;
import api.core.BaseRequest;
import api.core.HttpHelper;
import api.core.MissingRequestPartException;
import api.service.InterfaceParseService;
import api.service.impl.DefaultParseImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

public class MyAnnotationListener implements IInvokedMethodListener, ITestListener {

    private  static String runmode;
    private  static String host;
    private static Properties properties = new Properties();

    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    static {
        try {
            String defaultMode = "test";
            properties.load(MyAnnotationListener.class.getClassLoader().getResourceAsStream("config.properties"));
            runmode = properties.getProperty("runmode");
            host = properties.getProperty("host");
            if(StringUtils.isBlank(runmode)){
                logger.warn("the node of [mode] in config.properties unset!! ");
                runmode = defaultMode;
            }
            if (StringUtils.isBlank(host)){
                logger.warn("the node of [host] in config.properties unset!! ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    public void onTestStart(ITestResult result){
//        System.out.println(" my denifine anotations");
//    }

    @SneakyThrows
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult){
        if (method.isTestMethod()){
            final Method method1 = method.getTestMethod().getConstructorOrMethod().getMethod();
            if ((method1.isAnnotationPresent(TestApi.class)) || method.getTestMethod().getInstance().getClass().isAnnotationPresent(TestApi.class)){
                //intefaceName，接口名称
                String intefaceName = method1.isAnnotationPresent(TestApi.class)?
                    method1.getAnnotation(TestApi.class).name():
                    method.getTestMethod().getInstance().getClass().getAnnotation(TestApi.class).name();
                if (StringUtils.isBlank(intefaceName)){
                    logger.error("current test method has not unspecified iterface name");
                    throw new MissingRequestPartException("TestApi.name()");
                }
                BaseRequest tmpReq = getRequestExtend(intefaceName);
                //获取接口测试方法继承的basttest中的baseRequest属性
                Field request = method.getTestMethod().getInstance().getClass().getSuperclass().getDeclaredField("baseRequest");
                //将从接口文件中拿到的extend属性，提前装入请求方法中，为后续请求准备
                request.set(method.getTestMethod().getInstance(), tmpReq);
            }
        }
    }

    /*
    根据接口描述文件，获取request的相关属性，并以baserequest形式返回
     */
    private BaseRequest getRequestExtend(String intefaceName){
        //todo 根据config中的解析引擎来决定由那个server解析
        InterfaceParseService service = new DefaultParseImpl();
        String basePath = service.getBasePath(intefaceName);
        String serviceMethod = service.getMethod(intefaceName);
        String mine = service.getMINE(intefaceName);
        BaseRequest request = new BaseRequest();
        request.setUrl(host+basePath);
        request.setContentTtpe(mine);
        request.setMethod(serviceMethod);
        return request;
    }


}
