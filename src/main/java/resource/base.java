package resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;


public class base {

	WebDriver driver;
	protected Properties prop;

	public WebDriver initializeDriver() throws IOException {

		 prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\resource\\data.properties");
		prop.load(fis);
		String browserName = prop.getProperty("browser");
		System.out.println(browserName);

		if (browserName.equals("chrome")) {
//			System.setProperty("webdriver.chrome.driver", "C:\\Maven\\WebDriver\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browserName.equals("edge")) {
//			System.setProperty("webdriver.edge.driver", "C:\\Maven\\WebDriver\\msedgedriver.exe");
			driver = new EdgeDriver();
		} else {
			System.err.println("Invalid browser selection from data file.");
		}
		return driver;
	}

}
