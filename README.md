注意：如果你下载了源码，想在自己的windows环境下测试，将源码中`top.yumbo.music.test.configuration.ChromeAutoConfigration`中
指定chromedriver路径的注释打开，并且指向正确的chromedriver路径，（chromedriver是驱动，同时也需要电脑上有chrome浏览器）
```
// 打开注释并且将值设置为正确的路径
System.setProperty("webdriver.chrome.driver", "D:/Program Files (x86)/chromedriver/chromedriver.exe");
```


windows和linux都差不多，只要环境搭建好，然后再执行程序就能实现快速登录

### 全局参数说明
项目中暴露了两类接口：(post/get都可以，也支持json字符串的请求)
返回数据的格式：format如果不填默认返回Cookie对象

***

一类是已经写好了的登录，例如qq音乐、网易云音乐、csdn网站直接在请求路径中就能表现处理
一类是需要传入登录页面的url进行登录
#### 第一个类接口的使用方式：
请求路径：`/login/qq/{name}` name是在枚举对象中定义WebLoginEnum，两个参数的构造方法 第一个参数就是name，第二个参数就是登录页面的url
QQ音乐登录在线地址格式：
`http://yumbo.top:7000/login/qq/music?username=qq号&password=qq密码&format=2`
网易云音乐登录在线地址格式：
`http://yumbo.top:7000/login/qq/netease?username=qq号&password=qq密码&format=2`
csdn
QQ音乐登录在线地址格式(post/get都可以，也支持json字符串)：
`http://yumbo.top:7000/login/qq/csdn?username=qq号&password=qq密码&format=2`
#### 第二个接口的使用方式
请求路径: `/login/qq`
在线地址格式：
##### 注意：注意url编码问题，传入的url直接复制浏览器地址栏即可，如果是自己抓包分析出来的则注意编码

```
http://yumbo.top:7000/login/qq?username=qq号&password=qq密码&format=2&url=登录页面的url
```

### 想要见效果的看这篇博客有gif演示效果：
[java+selenium-java 实现qq自动登录并获取cookie](https://blog.csdn.net/qq_41813208/article/details/112646537)

### 完整的环境搭建过程
github显示不了图片的到csdn看我博客：[实现所有网站的qq登录返回登录后的cookie信息](https://blog.csdn.net/qq_41813208/article/details/112727425)


## 原理和实现

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
接口地址：`http://yumbo.top:7000/login/qq/{name}`
qq音乐在线地址：`http://yumbo.top:7000/login/qq/music`
网易云音乐在线地址：`http://yumbo.top:7000/login/qq/netease`
csdn在线地址：`http://yumbo.top:7000/login/qq/csdn`

下面是qq音乐的登录地址（qq音乐它不会跳转到后面的那个界面，需要通过抓包分析）
替换下面的username和password即可完成登录获取登录qq音乐后的cookie
方便复制：下面的这个模板只适合qq音乐，其它网站则根据后面参数补充的那个页面直接复制url代替这里参数的url即可
```
http://yumbo.top:7000/login/qq/music?username=qq号&password=qq密码&format=2
```


#### 参数补充说明
url 是登录qq的那个页面，复制登录界面的url 传入即可
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210117014505688.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxODEzMjA4,size_16,color_FFFFFF,t_70)


枚举网站的登录页面地址，然后访问该地址，输入用户名和密码进行登录，登录成功后会自动重定向到登录后的页面，进行一次刷新页面，然后获取cookie，将cookie以json对象的方式返回{"cookie":cookie字符串或原生数组}

#### 处理请求的Controller源码

```java
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
```
