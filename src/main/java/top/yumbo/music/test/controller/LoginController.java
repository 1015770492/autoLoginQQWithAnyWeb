package top.yumbo.music.test.controller;

import com.alibaba.fastjson.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.yumbo.music.test.web.WebLoginEnum;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
public class LoginController {

    @Autowired
    ChromeDriver chromeDriver;


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

    @RequestMapping(value = "/login/qq/{urlkey}", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject commonLogin(@RequestBody(required = false) JSONObject jsonObject,
                                  @PathVariable(value = "urlkey") String urlkey,
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
        System.out.println("进入了/login/qq/" + urlkey + "   ====> commonLogin");
        return LoginAndGetCookie(username, password, urlkey, format);
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
        WebDriver.Options manage = chromeDriver.manage();
        try {
            // 登录前先清除cookie
            final String url = WebLoginEnum.getUrl(name);
            if (!StringUtils.hasText(url)) {
                cookieJson.put("msg", "请使用另外一个接口进行登录");
                return cookieJson;
            }
            System.out.println("准备访问");
            chromeDriver.get(url);// 访问登录页面
            System.out.println("进行访问 => " + url);
            final WebDriver ptlogin_iframe = chromeDriver.switchTo().frame("ptlogin_iframe");
            ptlogin_iframe.findElement(By.id("switcher_plogin")).click();
            final WebElement u = ptlogin_iframe.findElement(By.className("inputstyle"));
            u.clear();// 清空输入的用户名
            u.sendKeys(username + "\n");// 输入账号
            final WebElement p = ptlogin_iframe.findElement(By.id("p"));
            p.clear();// 清空输入的密码数据
            p.sendKeys(password + "\n");// 输入密码，回车就提交了下面的这个点击登录不需要
            System.out.println("判断页面跳转前");
            final String title = chromeDriver.getTitle();// 得到标题
            System.out.println("当前标题 ==> " + title);
            // 如果标题没有换则说明页面没有切换
            int times = 0;
            while (chromeDriver.getTitle().equals(title) || !StringUtils.hasText(chromeDriver.getTitle())) {
                //如果标题相等就让他自旋，不相等说明跳转了页面
                try {
                    times++;
                    if (times > 20) {
                        System.out.println("===============>>");
                        chromeDriver.findElementById("login_button").click();

                        chromeDriver.switchTo().defaultContent();
                        chromeDriver.switchTo().frame("ptlogin_iframe");
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        final WebDriver tcaptcha_iframe = chromeDriver.switchTo().frame("tcaptcha_iframe");
                        System.out.println("111");

                        Actions actions = new Actions(tcaptcha_iframe);
                        final WebElement tcaptcha_drag_button = tcaptcha_iframe.findElement(By.id("tcaptcha_drag_thumb"));
                        actions.clickAndHold(tcaptcha_drag_button);
                        final Point location = tcaptcha_drag_button.getLocation();// 获取拖拽验证码的位置
                        IntStream.range(1, 170).forEach(x -> {
                            actions.moveToElement(tcaptcha_drag_button, 1, 0).perform();
                        });
                        actions.release(tcaptcha_drag_button).perform();

                        break;
                    }
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


//            chromeDriver.navigate().refresh();// 刷新页面
            manage = chromeDriver.manage();// 重新获取manage，之前的那个manage已经失效了
            System.out.println("标题变化后 ==> " + chromeDriver.getTitle());// 获取到了标题说明页面跳转成功了
            System.out.println("页面进行了跳转或者超时，限定了5秒内，否则结束循环");
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
//            manage.deleteAllCookies();// 每次登录完就清除cookie
        } catch (Exception e) {
            System.out.println("抛异常了");
            e.printStackTrace();
        }
        return cookieJson;
    }


    /**
     * @param jsonObject 可选，目的是兼容json数据，可能客户端两种都传，如果json数据完整则使用json的，下面的参数就会失效
     * @param url        必选
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
        System.out.println("进入了/login/qq  ==> loginQQBackCookie");
        return LoginAndGetCookie(username, password, url, format);
    }

}