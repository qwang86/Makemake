package api.testcase;

import api.Listener.MyAnnotationListener;
import api.core.BaseRequest;
import org.testng.annotations.Listeners;

@Listeners(MyAnnotationListener.class)
public class BaseTest {

    public BaseRequest baseRequest;

}
