package top.yumbo.music.test.controller;

import com.alibaba.fastjson.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Set;

@RestController
public class LoginController {

    @Resource
    ChromeDriver chromeDriver;


    /**
     *
     * @param jsonObject 可选，目的是兼容json数据，可能客户端两种都传
     * @param client_id 应用的id
     * @param redirect_uri 重定向地址需要去掉参数，
     * 例如qq音乐的：https://y.qq.com/portal/wx_redirect.html?login_type=1
     * 则变成 https://y.qq.com/portal/wx_redirect.html
     * @param username qq账号
     * @param password qq密码
     * @param format 默认1返回的cookie为json，传入的不是1则返回精简版的cookie
     * @return cookie类型的json数据，cookie咋json的cookie字段中
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject loginQQBackCookie(@RequestBody(required = false) JSONObject jsonObject,
                                        @RequestParam(value = "client_id", required = false) String client_id,
                                        @RequestParam(value = "redirect_uri", required = false) String redirect_uri,
                                        @RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "password") String password,
                                        @RequestParam(value = "format", required = false, defaultValue = "1") String format
    ) {

        if (StringUtils.hasText(client_id) && StringUtils.hasText(username) &&
                StringUtils.hasText(password)&& StringUtils.hasText(redirect_uri)) {
            // 什么也不做
        } else if (jsonObject != null && StringUtils.hasText(jsonObject.getString("redirect_uri")) &&
                StringUtils.hasText(jsonObject.getString("username")) &&
                StringUtils.hasText(jsonObject.getString("client_id")) &&
                StringUtils.hasText(jsonObject.getString("password"))
        ) {
            client_id = jsonObject.getString("client_id");
            redirect_uri = jsonObject.getString("redirect_uri");
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
        } else {
            final JSONObject error = new JSONObject();
            if (jsonObject != null) {
                error.put("输入了错误的信息", jsonObject);
            } else {
                final JSONObject info = new JSONObject();
                info.put("username", username);
                info.put("password", password);
                info.put("client_id", client_id);
                info.put("redirect_uri", redirect_uri);
                error.put("输入了错误的信息", info);
            }
            return error;
        }

        /**
         * 进行登录，前面进行参数处理
         */
        final JSONObject cookieJson = new JSONObject();
        try {

            // 拼接url 得到登录页面的地址
            String loginPage = "https://graph.qq.com/oauth2.0/show?which=Login&display=pc&" +
                    "response_type=code&client_id=" + client_id +
                    "&redirect_uri=" + redirect_uri +
                    "&scope=get_user_info&state=state";
            System.out.println("==============");
            System.out.println(loginPage);
            System.out.println("==============");
            final WebDriver.Options manage = chromeDriver.manage();// 为了后面得到cookie
            // 请求登录页面，输入密码进行登录
            chromeDriver.get(loginPage);// 去除多余参数，不然会导致登录失败

            final WebDriver ptlogin_iframe = chromeDriver.switchTo().frame("ptlogin_iframe");
            ptlogin_iframe.findElement(By.id("switcher_plogin")).click();
            final WebElement u = ptlogin_iframe.findElement(By.className("inputstyle"));
            u.clear();// 清空输入的用户名
            u.sendKeys(username + "\n");// 输入账号
            final WebElement p = ptlogin_iframe.findElement(By.id("p"));
            p.clear();// 清空输入的密码数据
            p.sendKeys(password + "\n");// 输入密码，回车就提交了下面的这个点击登录不需要
//            final WebElement login_button = ptlogin_iframe.findElement(By.id("login_button"));
//                    login_button.click();// 点击登录按钮
            //获得cookie
            Set<Cookie> coo = manage.getCookies();// 得到所有cookie
            //打印cookie
            System.out.println(coo);

            if (format.equals("2") || (jsonObject != null && (jsonObject.get("format") + "").equals("2"))) {
                final String cookie = parseSetCookie(coo);
                cookieJson.put("cookie", cookie);// 解析cookie并添加
            } else {
                cookieJson.put("cookie", coo);
            }
//            manage.deleteAllCookies();// 再次清除cookie
//            chromeDriver.getSessionStorage().clear();// 清空session存储
            chromeDriver.switchTo().defaultContent();
        } catch (Exception e) {
            System.out.println("抛异常了");
            e.printStackTrace();
        }
        return cookieJson;
    }

    /**
     * cookie数据的处理
     *
     * @param cookies 传入cookie的集合
     * @return k1=v1;k2=v2;  这种形式的cookie字符串
     */
    private String parseSetCookie(Set<Cookie> cookies) {
        if (cookies == null) {
            return "";
        }
        System.out.println("\n解析前cookie是" + cookies.toString());
        String cookieString = "";
        for (Cookie cookie : cookies) {
            if (StringUtils.hasText(cookie.getValue())) {// 为空的cookie则去除
                cookieString += (cookie.getName() + "=" + cookie.getValue() + ";");
            }
        }
        System.out.println("解析后：\n" + cookieString);
        return cookieString;
    }

}