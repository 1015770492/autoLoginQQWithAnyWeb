windows和linux都差不多，只要环境搭建好，然后再执行程序就能实现快速登录


### 想要见效果的看这篇博客有gif演示效果：
[java+selenium-java 实现qq自动登录并获取cookie](https://blog.csdn.net/qq_41813208/article/details/112646537)

### 完整的环境搭建过程
github显示不了图片的到csdn看我博客：[实现所有网站的qq登录返回登录后的cookie信息](https://blog.csdn.net/qq_41813208/article/details/112727425)


### 第一步给Linux服务器安装google-chrome（谷歌浏览器）
Centos操作系统的使用下面这个

下载rpm包
```bash
wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm
```
安装依赖
```bash
sudo yum install -y lsb
```
进行安装
```bash
sudo yum localinstall google-chrome-stable_current_x86_64.rpm
```
***
Ubuntu用户的使用下面的
下面命令建议不使用root身份进行下载
因为可能会报权限问题：
N: Download is performed unsandboxed as root as file '/root/google-chrome-stable_current_amd64.deb' couldn't be accessed by user '_apt'. - pkgAcquire::Run (13: Permission denied)


***
下载deb包

```bash
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
```
安装deb包 

```bash
sudo apt install -y ./google-chrome-stable_current_amd64.deb
```
完成后Centos和Ubuntu都会多一个命令

```bash
google-chrome
```

### 第二步，下载chromedriver 它的作用是操作google-chrome浏览器，也就是通过它来对谷歌浏览器操作的
下载地址：[https://npm.taobao.org/mirrors/chromedriver/](https://npm.taobao.org/mirrors/chromedriver/)

随便选一个吧，我选的是这个版本的：[https://npm.taobao.org/mirrors/chromedriver/86.0.4240.22/](https://npm.taobao.org/mirrors/chromedriver/86.0.4240.22/)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210117005250313.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxODEzMjA4,size_16,color_FFFFFF,t_70)
将它上传到linux服务器上，通过FTP工具
ftp工具 例如：[FileZila下载页面](https://www.filezilla.cn/download/client)选择适合自己的
或者xftp
ftp 工具 和 chromedriver也可以通过下面的csdn进行下载，我将它打包成了一个zip包。也可以选择前面发的链接自行下载接口
[https://download.csdn.net/download/qq_41813208/14503894](https://download.csdn.net/download/qq_41813208/14503894)

将他上传到一个目录下，例如`/root`
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210117010516450.png)
#### 授予执行权限
执行完后就会是绿色的提示。
```bash
chmod 777 chromedriver
```

### 第三步、下载jar包，或者去github自行打成jar包
jar包下载地址：[https://github.com/1015770492/autoLoginQQWithAnyWeb/releases/tag/1.0](https://github.com/1015770492/autoLoginQQWithAnyWeb/releases/tag/1.0)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210117023638510.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxODEzMjA4,size_16,color_FFFFFF,t_70)


java环境需要先弄好
Ubuntu用：`apt install -y openjdk-14-jre-headless`进行安装即可
Centos用：`yum install -y java-11-openjdk-devel.x86_64`安装即可（如果需要配置环境变量自行配置即可）

是一个springboot项目，端口是：7000，可以通过运行参数`-Dserver.port`修改启动端口
### 提醒
`-Dwebdriver.chrome.driver`是指定驱动的位置，请更改为正确的位置


![在这里插入图片描述](https://img-blog.csdnimg.cn/20210117023210312.png)
如果是默认的root目录下操作更改对应得路径即可
```
java -jar -Dwebdriver.chrome.driver=/root/chromedriver \
-Dserver.port=7000 autoLoginQQWithAnyWeb-1.0.jar
```

### 第四步发送请求得到cookie信息
`get/post`都支持，接收`json`/`传参数`
如下
接口地址：`/login`
在线地址：`http://yumbo.top:7000/login`

下面是qq音乐的登录地址（qq音乐它不会跳转到后面的那个界面，需要通过抓包分析）
替换下面的username和password即可完成登录获取登录qq音乐后的cookie
方便复制：下面的这个模板只适合qq音乐，其它网站则根据后面参数补充的那个页面直接复制url代替这里参数的url即可
```
http://yumbo.top:7000/login?url=https://graph.qq.com/oauth2.0/authorize?response_type=code%26state=state%26client_id=100497308%26redirect_uri=https://y.qq.com/portal/wx_redirect.html?login_type=1%26surl=https%3A%2F%2Fy.qq.com%2Fportal%2Fradio.html%23stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26use_customer_cb=0&username=qq号&password=qq密码&format=2
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210117013935192.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxODEzMjA4,size_16,color_FFFFFF,t_70)
#### 参数补充说明
url 是登录qq的那个页面，复制登录界面的url 传入即可
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210117014505688.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxODEzMjA4,size_16,color_FFFFFF,t_70)




#### 处理请求的Controller源码
```java
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
```
