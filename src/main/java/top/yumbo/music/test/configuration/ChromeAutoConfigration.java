package top.yumbo.music.test.configuration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ChromeAutoConfigration {


    // 销毁时关闭谷歌浏览器
    @Bean(destroyMethod = "quit")
    public ChromeDriver chromeDriver() {
        // idea中为了方便则开启这条注释，指定正确的chrome驱动位置
//         System.setProperty("webdriver.chrome.driver", "D:/Program Files (x86)/chromedriver/chromedriver.exe");
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless"); //无浏览器模式
        options.addArguments("--no-sandbox");// 为了让root用户也能执行


        // 优化参数
//        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--verbose");
//        options.addArguments("blink-settings=imagesEnabled=false");
//        options.addArguments("--disable-gpu");
        ChromeDriver chromeDriver = new ChromeDriver(options);//实例化
        chromeDriver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
        return chromeDriver;
    }


}
