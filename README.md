windows和linux都差不多，只要环境搭建好，然后再执行程序就能实现快速登录


### 第一步、安装Chrome浏览器，如果有其它浏览器可以修改源码改成对应的浏览器例如：FireFox

### 第二步，下载驱动，86.0.4其它版本的点击进去后自己退出一级目录就能看到了
[chromedriver淘宝镜像地址](https://npm.taobao.org/mirrors/chromedriver/86.0.4240.22/)

解压缩后是一个单文件，对于linux用户需要加执行权限：`chmod +x chromedriver的路径`
ubuntu用户则不能使用root执行，切换成普通用户执行，java环境是必须的

### 第三步、执行程序实现自动登录，控制台会打印cookie信息，可以自己处理一下
这是我之前处理cookie的方式：将控制台打印的cookie字符串传入自动解析成键值对的信息

```
    /**
     * 解析set-Cookie,去除过期时间等无关信息
     */
    private static String parseSetCookie(String setCookie) {
        if (setCookie == null) {
            return "";
        }
        System.out.println("解析前cookie是" + setCookie);
        final String[] split = setCookie.split(";, ");
        final Optional<String> cookieStringOptional = Arrays.stream(split).map(x -> {
            return x.split(";")[0] + "; ";
        }).reduce((x, y) -> x + y);
        String cookieString = cookieStringOptional.get() + "]";
        System.out.println("解析后cookie是:" + cookieString);
        return cookieString;
    }
```
### 实现原理
原理很简单，因为搞了一下qq互联，知道它的工作流程
见博客：[网站应用接入qq登录,实现免注册原理](https://blog.csdn.net/qq_41813208/article/details/112646397)

然后我就发现了qq互联其实是不需要很多参数的校验的，通过 Fiddler Everywhere 抓包，很快就分析到qq音乐的appid就是：100497308
对应clint_id，而redirect_uri我使用的是原值，直接重定向回到了qq音乐的首页，原数据中有其它好多字段要校验
我全部去掉了，所以第一步获取code请求的四个必要参数填上，其它都不要
也就是：[https://graph.qq.com/oauth2.0/authorize?response_type=code&state=state&client_id=100497308&redirect_uri=https://y.qq.com/portal/wx_redirect.html?login_type=1%26surl=https%3A%2F%2Fy.qq.com%2Fportal%2Fradio.html%23stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26use_customer_cb=0](https://graph.qq.com/oauth2.0/authorize?response_type=code&state=state&client_id=100497308&redirect_uri=https://y.qq.com/portal/wx_redirect.html?login_type=1%26surl=https%3A%2F%2Fy.qq.com%2Fportal%2Fradio.html%23stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26use_customer_cb=0)
直接点过去然后登录就直接登录成功，跳过了前端校验，如果常规的操作点击登录，就会有前端拖动验证
如果前端也这样做模仿的话，唯一麻烦的就是登录成功了不好获取cookie。
对于原项目我给的建议是我可以做出一个jar包，作为插件集成到docker容器一起打包。


下面这个工具是我封装完的网易云音乐api和qq音乐api，详情见:[https://github.com/1015770492/yumbo-music-utils](https://github.com/1015770492/yumbo-music-utils)
如果原文档没有变化则暂时是没有必要再维护，因为springboot版本的会更方便，而且包结构也变化了，不打算直接依赖它，我选择直接将源码引入到springboot进行整改
```xml
<!-- https://mvnrepository.com/artifact/top.yumbo.music/yumbo-music-utils -->
<dependency>
    <groupId>top.yumbo.music</groupId>
    <artifactId>yumbo-music-utils</artifactId>
    <version>1.2.1</version>
</dependency>

```

另外我最近再做springboot版本的统一（网易云一样、qq音乐）api，实现启动器，快速启动，项目已经略有成型，不久的将来会以maven中央仓库的方式发布

