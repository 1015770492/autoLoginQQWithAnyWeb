package top.yumbo.music.test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class QQMusicDemo {


    /**
     * 代码比较随意，自行增改调整
     */
    public static void main(String[] args) {
        // idea中为了方便则开启这条注释，指定正确的chrome驱动位置
//        System.setProperty("webdriver.chrome.driver", "D:/Program Files (x86)/chromedriver/chromedriver.exe");

        //设置系统环境变量
        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            //options.addArguments("--headless"); //无浏览器模式
            driver = new ChromeDriver(options);//实例化
            driver.get("https://graph.qq.com/oauth2.0/authorize?response_type=code&state=state&client_id=100497308&redirect_uri=https://y.qq.com/portal/wx_redirect.html?login_type=1%26surl=https%3A%2F%2Fy.qq.com%2Fportal%2Fradio.html%23stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26stat%3Dy_new.top.pop.logout%26use_customer_cb=0");

            final WebDriver ptlogin_iframe = driver.switchTo().frame("ptlogin_iframe");
            ptlogin_iframe.findElement(By.id("switcher_plogin")).click();
            final WebElement u = ptlogin_iframe.findElement(By.className("inputstyle"));
            u.click();
            final char[] chars = "qq账号".toCharArray();
            for (int i = 0; i < chars.length; i++) {
                try {
                    TimeUnit.MICROSECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                u.sendKeys("" + chars[i]);
            }
            final WebElement login_button = ptlogin_iframe.findElement(By.id("login_button"));
//            final WebElement qq_account = ptlogin_iframe.findElement(By.className("input_tips"));
            final WebElement p = ptlogin_iframe.findElement(By.id("p"));
            final char[] chars1 = "qq密码".toCharArray();
            for (int i = 0; i < chars1.length; i++) {
                p.sendKeys("" + chars1[i]);
            }
            p.sendKeys("\n");
            try {
                login_button.click();
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                System.out.println("<><><>");
            }
            System.out.println(driver.getCurrentUrl());
            //获得cookie
            Set<Cookie> coo = driver.manage().getCookies();
            //打印cookie
            System.out.println(coo);

            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("抛异常了");
            e.printStackTrace();
        } finally {
            //使用完毕，关闭webDriver
            if (driver != null) {
                driver.quit();
            }
        }


    }

    private static void Imgcheck(WebDriver ptlogin_iframe) {
        try {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final WebDriver tcaptcha_iframe = ptlogin_iframe.switchTo().frame("tcaptcha_iframe");
            final WebElement tcaptcha_drag_button = tcaptcha_iframe.findElement(By.id("tcaptcha_drag_button"));
            final Random random = new Random(20);
            for (int i = 22; i < 190; i++) {
                try {
                    final int i1 = random.nextInt();
                    System.out.println(i1);
                    TimeUnit.MICROSECONDS.sleep(i1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tcaptcha_drag_button.click();
                ((JavascriptExecutor) tcaptcha_iframe).executeScript("document.getElementById(\"slideBlock\").style.left='" + i + "px'");
                ((JavascriptExecutor) tcaptcha_iframe).executeScript("document.getElementById(\"tcaptcha_drag_button\").style.left='" + (i + 1) + "px'");
            }


        } catch (Exception e) {
            System.out.println("=====》报异常了");
        }
    }
}
