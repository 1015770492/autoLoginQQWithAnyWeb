package top.yumbo.music.test.controller;

import com.alibaba.fastjson.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class LoginController {
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject loginQQBackCookie(@RequestBody(required = false) JSONObject jsonObject,
                                        @RequestParam(value = "url", required = false) String url,
                                        @RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "format", required = false, defaultValue = "1") String format,
                                        @RequestParam(value = "password") String password
    ) {
        if (StringUtils.hasText(url) && StringUtils.hasText(username) && StringUtils.hasText(password)) {
            // 什么也不做
        } else if (jsonObject != null && StringUtils.hasText(jsonObject.getString("url")) && StringUtils.hasText(jsonObject.getString("username")) && StringUtils.hasText(jsonObject.getString("password"))) {
            url = jsonObject.getString("url");
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
        } else {
            final JSONObject error = new JSONObject();
            if (jsonObject == null) {
                error.put("输入了错误的信息", jsonObject);
            } else {
                final JSONObject info = new JSONObject();
                info.put("username", username);
                info.put("password", password);
                info.put("url", url);
                error.put("输入了错误的信息", info);
            }
            return error;
        }
        // idea中为了方便则开启这条注释，指定正确的chrome驱动位置
//        System.setProperty("webdriver.chrome.driver", "D:/Program Files (x86)/chromedriver/chromedriver.exe");
        final JSONObject cookieJson = new JSONObject();
        String cookie = null;
        //设置系统环境变量
        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); //无浏览器模式
            options.addArguments("--no-sandbox");// 为了让root用户也能执行

            // 优化参数
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("blink-settings=imagesEnabled=false");
            options.addArguments("--disable-gpu");
            driver = new ChromeDriver(options);//实例化
            driver.get(url);

            final WebDriver ptlogin_iframe = driver.switchTo().frame("ptlogin_iframe");
            ptlogin_iframe.findElement(By.id("switcher_plogin")).click();
            final WebElement u = ptlogin_iframe.findElement(By.className("inputstyle"));
            u.click();// 点击输入框
            u.sendKeys(username + "\n");// 输入账号
            final WebElement login_button = ptlogin_iframe.findElement(By.id("login_button"));
            final WebElement p = ptlogin_iframe.findElement(By.id("p"));
            p.sendKeys(password + "\n");// 输入密码，回车就提交了下面的这个点击登录不需要
//                    login_button.click();// 点击登录按钮
            System.out.println(driver.getCurrentUrl());
            //获得cookie
            Set<Cookie> coo = driver.manage().getCookies();
            //打印cookie
            System.out.println(coo);

            if (format.equals("2") || (jsonObject != null && (jsonObject.get("format") + "").equals("2"))) {
                cookieJson.put("cookie", parseSetCookie(coo));
            } else {
                cookieJson.put("cookie", coo);
            }
        } catch (Exception e) {
            System.out.println("抛异常了");
            e.printStackTrace();
        } finally {
            //使用完毕，关闭webDriver
            if (driver != null) {
                driver.quit();
            }
        }
        return cookieJson;
    }

    /**
     * cookie数据的处理
     *
     * @param cookies
     * @return
     */
    private String parseSetCookie(Set<Cookie> cookies) {
        if (cookies == null) {
            return "";
        }
        System.out.println("解析前cookie是" + cookies.toString());
        String cookieString = "";
        for (Cookie cookie : cookies) {
            cookieString += (cookie.getName() + "=" + cookie.getValue() + ";");
        }
        System.out.println("解析后：\n" + cookieString);
        return cookieString;
    }

}