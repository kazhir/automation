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

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

@RunWith(Parameterized.class)
public class IOSTest {
    private WebDriver driver;
    private String inputName;
    private String checkName;
    private String testNo;

    @Before
    public void setUp() throws MalformedURLException  {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("automationName", "XCUITest");
        capabilities.setCapability("deviceName", "iPhone 13");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "15.2");
        capabilities.setCapability("browserName", "Safari");

        driver = new IOSDriver<IOSElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

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

    public IOSTest(String testNo, String inputName, String checkName){
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