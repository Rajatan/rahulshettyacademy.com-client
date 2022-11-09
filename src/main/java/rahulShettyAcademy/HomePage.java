package rahulShettyAcademy;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
	
	WebDriver driver;
	
	public HomePage(WebDriver driver) {
		this.driver=driver;
	}
	
	private By items = By.tagName("h5");
	
	public List<WebElement> itemsM() {
		return driver.findElements(items);
	}

}
