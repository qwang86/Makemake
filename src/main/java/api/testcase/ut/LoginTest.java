package api.testcase.ut;

import api.annotation.TestApi;
import api.core.BaseResponse;
import api.core.HttpHelper;
import api.model.TinyBoom_Login;
import api.testcase.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest extends BaseTest {


    @Test(description = "正常账号密码登陆测试")
    @TestApi(name = "LoginService")
    public void loginTest() throws IOException {
        TinyBoom_Login tinyBoom_login = new TinyBoom_Login();
        tinyBoom_login.setUsername( "aa");
        tinyBoom_login.setPasswd("332");
        BaseResponse response = HttpHelper.sendRequest(baseRequest, tinyBoom_login);
        Assert.assertEquals(response.getCode(), 200l);
    }

}
