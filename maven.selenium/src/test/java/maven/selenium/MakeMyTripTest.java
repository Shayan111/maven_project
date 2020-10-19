package maven.selenium;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.AfterClass;



public class MakeMyTripTest {
	
	public static WebDriver driver;
	public static String path = System.getProperty("user.dir");
	@BeforeClass
	public static void before_test()
	{
		System.setProperty("webdriver.chrome.driver", path+"/chromedriver");
		driver = new ChromeDriver();
	}
	
	

	@Test
	public void main_test() throws FileNotFoundException, IOException, InterruptedException, NoSuchFrameException
	{

		FileInputStream makemytrip_file= new FileInputStream(path+"/input_details.properties");
		Properties makemytrip_properties = new Properties();
		makemytrip_properties.load(makemytrip_file);

		
		WebDriverWait wait = new WebDriverWait(driver, 20);

		driver.get(makemytrip_properties.getProperty("URL"));
		driver.manage().window().maximize();	

		String handle = driver.getWindowHandle();
		
		
		Thread.sleep(50000);
	

		List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
		for(int i=0;i<iframes.size();i++)
		{	
			
			ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframes.get(i));
			if (driver.findElements(By.xpath("//i[@class='wewidgeticon we_close']")).size()>0)
			{
				driver.findElement(By.xpath("//i[@class='wewidgeticon we_close']")).click();
				driver.switchTo().window(handle);
				break;
			}
		}
		
		if (driver.findElements(By.xpath("//p[text()='Login/Signup for Best Prices']")).size()>0)
		{
			driver.findElement(By.xpath("//p[contains(text(),'Login or Create Account')]")).click();
		}
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='fromCity']//parent::label")));
		WebElement ele = driver.findElement((By.xpath("//input[@id='fromCity']//parent::label")));
		
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].click()", ele);
		
		driver.findElement(By.xpath("(//input[@type='text'])[1]")).sendKeys(makemytrip_properties.getProperty("from"));
		driver.findElement(By.xpath("//p[text()='Delhi, India']//parent::div")).click();
		
		driver.findElement(By.xpath("//input[@id='toCity']")).click();
		driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(makemytrip_properties.getProperty("to"));
		driver.findElement(By.xpath("(//p[text()='Kolkata, India']//parent::div)[1]")).click();
		
		driver.findElement(By.xpath("//label[@for='departure']")).click();
		
		driver.findElement(By.xpath("(//div[@class='DayPicker-wrapper']//div[@class='dateInnerCell'])[40]")).click();
				
		driver.findElement(By.xpath("//a[text()='Search']")).click();
		
		
		
		Thread.sleep(15000);
		
		
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//button[contains(@id,'bookbutton')])[1]"))).click();
		
		driver.findElement(By.xpath("(//button[text()=' Book Now '])[1]")).click();
		
		for(String winHandle : driver.getWindowHandles()){
		    driver.switchTo().window(winHandle);
		}
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h4[text()='Review your booking']")));
		String actual_text = driver.findElement(By.xpath("//h4[text()='Review your booking']")).getText();
		String expected_text = "Review your booking";
		Assert.assertEquals(actual_text,expected_text);
	}
	@AfterClass
	public static void after_test()
	{
		driver.quit();
	}
}



