package top.yumbo.music.test.web;


import org.springframework.util.StringUtils;

import java.util.HashMap;

public enum WebLoginEnum {
    QQ_MUSIC("music","https://graph.qq.com/oauth2.0/authorize?response_type=code&state=state&client_id=100497308&redirect_uri=https://y.qq.com/portal/wx_redirect.html?login_type=1%26surl=https%3A%2F%2Fy.qq.com%2Fportal%2Fradio.html"),
    NETEASE_MUSIC("netease","https://graph.qq.com/oauth2.0/show?which=Login&display=pc&client_id=100495085&response_type=code&redirect_uri=https://music.163.com/back/qq&forcelogin=true&state=nNIYBHMKvq&checkToken=9ca17ae2e6ffcda170e2e6eed6eb7bb4b000b8db2598868eb6d44b878b9aabf148b0bc83abc24191b9f8b6db2af0feaec3b92a949e888ac145bba9a482c44f978e9ea3c85ea1e9abd1c9429a99b8afeb49b0b5ee9e"),
    CSDN("csdn","https://graph.qq.com/oauth2.0/show?which=Login&display=pc&client_id=100270989&response_type=code&redirect_uri=https%3A%2F%2Fpassport.csdn.net%2Faccount%2Flogin%3FpcAuthType%3Dqq%26state%3Dtest"),

    ;
    private static HashMap<String,String> webLoginEnumHashMap=new HashMap<>();
    static {
        for (WebLoginEnum value : WebLoginEnum.values()) {
            webLoginEnumHashMap.put(value.name,value.url);
        }
    }
    public static String getUrl(String name){
        final String url = webLoginEnumHashMap.getOrDefault(name,name);// 得到url
        return url;
    }

    String name;
    String url;

    WebLoginEnum(String name,String url) {
        this.name = name;
        this.url = url;
    }


}
