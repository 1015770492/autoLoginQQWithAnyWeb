package top.yumbo.music.test.web;


import org.springframework.util.StringUtils;

import java.util.HashMap;

public enum WebLoginEnum {
    QQ_MUSIC("music","https://graph.qq.com/oauth2.0/authorize?response_type=code&state=state&client_id=100497308&redirect_uri=https://y.qq.com/portal/wx_redirect.html?login_type=1%26surl=https%3A%2F%2Fy.qq.com%2Fportal%2Fradio.html%23stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26use_customer_cb=0"),
    NETEASE_MUSIC("netease","https://graph.qq.com/oauth2.0/show?which=Login&display=pc&client_id=100495085&response_type=code&redirect_uri=https://music.163.com/back/qq&forcelogin=true&state=EVLxphoRvu&checkToken=9ca17ae2e6ffcda170e2e6eeb7f23d83bd83a3f760a2bc8fa3d45f879b9baef46bb59dbe8bea6fb6918dd8b52af0feaec3b92a85f0a685b73998bbfb9ac14f929f9ea7d85b8eaf8188dc5aab9d9ab9d77ca994ee9e"),
    CSDN("csdn","https://graph.qq.com/oauth2.0/show?which=Login&display=pc&client_id=100270989&response_type=code&redirect_uri=https%3A%2F%2Fpassport.csdn.net%2Faccount%2Flogin%3FpcAuthType%3Dqq%26state%3Dtest"),

    ;
    private static HashMap<String,String> webLoginEnumHashMap=new HashMap<>();
    static {
        for (WebLoginEnum value : WebLoginEnum.values()) {
            webLoginEnumHashMap.put(value.name,value.url);
        }
    }
    public static String getUrl(String name){
        final String url = webLoginEnumHashMap.get(name);
        if (StringUtils.hasText(url)){
            return url;
        }
        return name;
    }

    String name;
    String url;

    WebLoginEnum(String name,String url) {
        this.name = name;
        this.url = url;
    }


}
