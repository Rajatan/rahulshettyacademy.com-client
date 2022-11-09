package rahulShettyAcademy;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
	
	WebDriver driver;
	
	LoginPage (WebDriver driver){
		this.driver = driver;
	}
	
	private By email = By.id("userEmail");
	private By password = By.id("userPassword");
	private By login = By.id("login");
	private By assertLogin = By.xpath("/html[1]/body[1]/app-root[1]/app-dashboard[1]/section[2]/div[1]/div[1]/div[1]");

	
//	Will test this one by providing the incorrect email ID or Password, also the ID is not registered
//	private By errIncorrect = By.className("toast-bottom-right toast-container");
	
	
	
	public WebElement emailM () {
		return driver.findElement(email);
	}
	public WebElement passwordM() {
		return driver.findElement(password);
	}
	public WebElement loginM() {
		return driver.findElement(login);
	}
	public WebElement assertLoginM() {
		return driver.findElement(assertLogin);
	}
}
