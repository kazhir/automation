package automation;

import java.io.File;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.lang.Thread;
import java.util.Arrays;
import java.util.Collection;
import java.net.URL;
import java.net.MalformedURLException;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;

@RunWith(Parameterized.class)
public class LoginTest {
    private WebDriver driver;
    private String inputName;
    private String checkName;
    private String testNo;
    private String browser = System.getenv("browser");
    private DesiredCapabilities capabilities = new DesiredCapabilities();

    @Before
    public void setUp() throws MalformedURLException  {
        switch(browser){
            case "Edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "ie11":
                WebDriverManager.iedriver().setup();
                //System.setProperty("webdriver.ie.driver.loglevel", "DEBUG");
                driver = new InternetExplorerDriver();
                break;
            case "Firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "Chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "safari":
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
                break;
            case "ios_safari":           
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability("deviceName", "iPhone 13");
                capabilities.setCapability("platformName", "iOS");
                capabilities.setCapability("platformVersion", "15.2");
                capabilities.setCapability("browserName", "Safari");
                driver = new IOSDriver<IOSElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
                break;
            case "android_chrome":
                capabilities.setCapability("platformName", "Android");
                capabilities.setCapability("platformVersion", "12");
                capabilities.setCapability("deviceName", "Android Emulator");
                capabilities.setCapability("browserName", "Chrome");
                capabilities.setCapability("avd", "Pixel_XL_API_31");
                capabilities.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
                driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
                break;
        }
        driver.get("http://example.selenium.jp/reserveApp");
    }

    @Parameters(name = "testNo.{index}")
    public static Collection<?> parameters() {
        return Arrays.asList(new Object[][] {
            {"TestNo1", "サンプルユーザ", "サンプルユーザ"},
            {"TestNo2", "サンプルユーザA","サンプルユーザA"},
            {"TestNo3", "サンプルユーザB", "サンプルユーザB"},
            {"TestNo4", "サンプルユーザC", "サンプルユーザC"}
        });
    }

    public LoginTest(String testNo, String inputName, String checkName){
        this.testNo = testNo; 
        this.inputName = inputName;
        this.checkName = checkName;
    }
    @Test
    public void ユーザ名入力チェック() throws IOException, InterruptedException {
        driver.findElement(By.id("guestname")).sendKeys(inputName);
        Thread.sleep(5000); // 10秒(1万ミリ秒)間だけ処理を止める
        String actual = driver.findElement(By.id("guestname")).getAttribute("value");
        String expected = checkName;
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String path = "target/screenshot/" + testNo + "_iOS.png";
        FileUtils.copyFile(scrFile, new File(path));
        assertEquals(expected, actual);
    }
    @After
    public void tearDown(){
        driver.quit();
    }
}