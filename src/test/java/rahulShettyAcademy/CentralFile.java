package rahulShettyAcademy;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.locators.RelativeLocator.RelativeBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import junit.framework.Assert;
import resource.base;

public class CentralFile extends base {

	WebDriver driver;
	protected String vari;

	@BeforeTest
	public void initializeBrowser() throws IOException {
		driver = initializeDriver();
	}

//	@Parameters({"emailID", "password"})
	@Test
	public void loginTest(/* String emailID, String password */) throws InterruptedException {
		driver.get(prop.getProperty("url"));
		LoginPage login = new LoginPage(driver);
//		System.out.println(emailID + " "+ password);
		login.emailM().sendKeys("rajatan98@gmail.com");
		login.passwordM().sendKeys("Neelammanavar@24");
		login.loginM().click();

//		WebElement assertLog = new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(login.assertLoginM()));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		System.out.println(login.assertLoginM().getText().contains("Showing"));
		Assert.assertEquals(login.assertLoginM().getText().contains("Showing"), true);

	}

	@Test(dependsOnMethods = "loginTest")
	public void homePageTest() {
//		Home Page
		HomePage home = new HomePage(driver);
//		List<WebElement> itemList= ;
		int size = home.itemsM().size();
		System.out.println("Size of items " + size);
//		for(WebElement item:home.itemsM()) {
//			System.out.println(item.getText());
//			System.out.println( item.getText().contains("IPHONE 13 PRO") ? true : false); 
//		}
		System.out.println("List Data");
		List<String> items = home.itemsM().stream().map(s -> s.getText()).collect(Collectors.toList());
		System.out.println(items);
		System.out.println(items.contains("IPHONE 13 PRO") ? "Index of Item: " + items.indexOf("IPHONE 13 PRO")
		: "Item not found");
		String prodcutname = prop.getProperty("productName");
		System.out.println(prodcutname);
		int i=items.indexOf(prodcutname);
		vari= driver.findElement(By.xpath("//div["+(i+1)+"]/div[1]/img[1]")).getAttribute("src");
		System.out.println("Image src: "+vari);
//		for(String item:items) {
//			System.out.println(item);	
//		}
	}

	private Object with(By xpath) {
		// TODO Auto-generated method stub
		return null;
	}

	@AfterTest
	public void tearDown() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.close();
	}

}
