package top.yumbo.music.test.controller;

import com.alibaba.fastjson.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.yumbo.music.test.web.WebLoginEnum;

import javax.annotation.Resource;
import java.util.Set;

@RestController
public class LoginController {

    @Resource
    ChromeDriver chromeDriver;
    @Resource
    WebDriver.Options manage;

    /**
     * cookie数据的处理
     *
     * @param cookies 传入cookie的集合
     * @return k1=v1;k2=v2;  这种形式的cookie字符串
     */
    private String parseCookieSet(Set<Cookie> cookies) {
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

    @GetMapping({"/login/qq/{name}"})
    public JSONObject commonLogin(@RequestBody(required = false) JSONObject jsonObject,
                                  @PathVariable(value = "name") String name,
                                  @RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "password") String password,
                                  @RequestParam(value = "format", required = false, defaultValue = "1") String format) {
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            // 什么也不做
        } else if (jsonObject != null
                && StringUtils.hasText(jsonObject.getString("username"))
                && StringUtils.hasText(jsonObject.getString("password"))) {
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
                error.put("输入了错误的信息", info);
            }
            return error;
        }
        return LoginAndGetCookie(username, password, name, format);
    }

    /**
     * 登录已知类型的网站
     *
     * @param username qq账号
     * @param password qq密码
     * @param name     枚举登录页面的封装类,如果hashMap中没有则这个name就是登录页面的地址
     * @param format   返回的cookie类型，1表示原cookie数据，2表示处理后的cookie
     * @return json类型的cookie封装类
     */
    public JSONObject LoginAndGetCookie(String username, String password, String name, String format) {
        final JSONObject cookieJson = new JSONObject();
        try {
            // 登录前先清除cookie
            final String url = WebLoginEnum.getUrl(name);
            if (!StringUtils.hasText(url)) {
                cookieJson.put("msg", "网站没有引入项目，请使用另外一个接口进行登录");
                return cookieJson;
            }
            manage.deleteAllCookies();// 清除cookie
            chromeDriver.get(url);// 访问登录页面
            final WebDriver ptlogin_iframe = chromeDriver.switchTo().frame("ptlogin_iframe");
            ptlogin_iframe.findElement(By.id("switcher_plogin")).click();
            final WebElement u = ptlogin_iframe.findElement(By.className("inputstyle"));
            u.clear();// 清空输入的用户名
            u.sendKeys(username + "\n");// 输入账号
            final WebElement p = ptlogin_iframe.findElement(By.id("p"));
            p.clear();// 清空输入的密码数据
            p.sendKeys(password + "\n");// 输入密码，回车就提交了下面的这个点击登录不需要
            final String beforeUrl = chromeDriver.getCurrentUrl();
            while (chromeDriver.getCurrentUrl().equals(beforeUrl)) {
                // 页面没有跳转就让他等待，等待自己重定向到登录后的页面，然后再获取cookie时就是正确的cookie
            }

            System.out.println("=======等待登录成功后跳转到页面<<<<<<<<<<<");
            chromeDriver.navigate().refresh();// 刷新页面获取cookie，不然会导致cookie数据有问题
            //获得cookie
            Set<Cookie> coo = manage.getCookies();// 得到所有cookie
            //打印cookie
            System.out.println(coo);

            if (format.equals("2")) {
                final String cookie = parseCookieSet(coo);
                cookieJson.put("cookie", cookie);// 解析cookie并添加
            } else {
                cookieJson.put("cookie", coo);
            }
            manage.deleteAllCookies();// 每次登录完就清除cookie
        } catch (Exception e) {
            System.out.println("抛异常了");
            e.printStackTrace();
        }
        return cookieJson;
    }


    /**
     * @param jsonObject 可选，目的是兼容json数据，可能客户端两种都传
     * @param url        可选，目的是兼容json数据，可能客户端两种都传
     * @param username   qq账号
     * @param password   qq密码
     * @param format     默认1返回的cookie为json，传入的不是1则返回精简版的cookie
     * @return cookie类型的json数据，cookie咋json的cookie字段中
     */
    @Deprecated
    @RequestMapping(value = "/login/qq", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject loginQQBackCookie(@RequestBody(required = false) JSONObject jsonObject,
                                        @RequestParam(value = "url", required = false) String url,
                                        @RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "password") String password,
                                        @RequestParam(value = "format", required = false, defaultValue = "1") String format
    ) {

        if (StringUtils.hasText(url) && StringUtils.hasText(username) && StringUtils.hasText(password)) {
            // 什么也不做
        } else if (jsonObject != null && StringUtils.hasText(jsonObject.getString("url")) &&
                StringUtils.hasText(jsonObject.getString("username")) &&
                StringUtils.hasText(jsonObject.getString("password"))
        ) {
            url = jsonObject.getString("url");
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
            format = jsonObject.getString("format");
        } else {
            final JSONObject error = new JSONObject();
            if (jsonObject != null) {
                error.put("输入了错误的信息", jsonObject);
            } else {
                final JSONObject info = new JSONObject();
                info.put("username", username);
                info.put("password", password);
                info.put("url", url);
                info.put("format", format);
                error.put("输入了错误的信息", info);
            }
            return error;
        }

        return LoginAndGetCookie(username, password, url, format);
    }

}