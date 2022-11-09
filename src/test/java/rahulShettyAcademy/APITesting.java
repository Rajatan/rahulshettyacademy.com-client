package rahulShettyAcademy;

import org.apache.http.util.Asserts;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import junit.framework.Assert;
import resource.base;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class APITesting {
	static String token;
	static String userId;
	static String productId;
	static Properties prop;
	static File image1;
	static String orderId;
	WebDriver driver;
	@Test(priority = 1)
	public void loginAPITest() throws IOException {

		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\resource\\data.properties");
		prop.load(fis);
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().header("Content-type", "application/json")
				.body("{\r\n" + "  \"userEmail\": \"rajatan98@gmail.com\",\r\n"
						+ "  \"userPassword\": \"Neelammanavar@24\"\r\n" + "}")
				.when().post("/api/ecom/auth/login").then().log().all().assertThat().statusCode(200).extract()
				.response().asString();
		JsonPath data = parser(response);
		token = data.getString("token");
		userId = data.getString("userId");
		String message = data.getString("message");
		System.out.println("Token: " + token);
		System.out.println("UserId: " + userId);
		System.out.println("Message: " + message);
	}

	JsonPath parser(String json) {
		JsonPath js = new JsonPath(json);

		return js;
	}

	@Test(priority = 2)
	public void createItemAPITest() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		image1 = new File(System.getProperty("user.dir") + "\\src\\main\\java\\resource\\"+prop.getProperty("productImage")+"");

		System.out.println(image1);
		System.out.println(userId);
		System.out.println(token);
		double productPrice = Double.parseDouble(prop.getProperty("productPrice"));

		String responseItem = given().log().all().header("Authorization", token)
				.param("productName", prop.getProperty("productName"))
				.param("productAddedBy", userId)
				.param("productCategory", prop.getProperty("productCategory"))
				.param("productSubCategory", prop.getProperty("productSubCategory"))
				.param("productPrice", productPrice)
				.param("productDescription", prop.getProperty("productDescription"))
				.param("productFor", prop.getProperty("productFor"))
				.multiPart("productImage", image1)
				.when().post("/api/ecom/product/add-product")
				.then().extract().response().asString();

		JsonPath data1 = parser(responseItem);
		productId = data1.getString("productId");
		String message1 = data1.getString("message");
		System.out.println("Product ID: " + productId);
		System.out.println("Message: " + message1);
	}

	@Test(dependsOnMethods = "deleteCartItem")
	public void deleteItemAPITest() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response2 = given().log().all().header("Authorization", token)

				.when().delete("api/ecom/product/delete-product/" + productId + "").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();
		driver.findElement(By.xpath("//button[@routerlink='/dashboard/myorders']")).click();
		driver.findElement(By.xpath("//button[@routerlink='/dashboard/']")).click();
		JsonPath data2 = parser(response2);
		String message2 = data2.getString("message");
		System.out.println("Message: " + message2);

	}

	@Test(priority = 3)
	public void addToCart() throws InterruptedException {
//		Here I'll have to build selenuim code to catch the ProductAddTocart Image link and to perticular item,
//		then that  should be passeed to "productPayload.setProductImage()"
//		System.setProperty("webdriver.chrome.driver", "C:\\Maven\\WebDriver\\chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
//		driver.get(prop.getProperty("url"));
////		driver.findElement(By.)
		String productAddToCartImage = loginToGetData();
		System.out.println("productAddToCartImage "+ productAddToCartImage);
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		ProductAddToCartPayload productPayload = new ProductAddToCartPayload();
		productPayload.set_id(productId);
		productPayload.setProductAddedBy(userId);
		productPayload.setProductName(prop.getProperty("productName"));
		productPayload.setProductCategory(prop.getProperty("productCategory"));
		productPayload.setProductSubCategory(prop.getProperty("productSubCategory"));
		double productPrice = Double.parseDouble(prop.getProperty("productPrice"));
		productPayload.setProductPrice(productPrice);
		productPayload.setProductDescription(prop.getProperty("productDescription"));
		productPayload.setProductRating(prop.getProperty("productRating"));
		productPayload.setProductTotalOrders(prop.getProperty("productTotalOrders"));
		boolean productStatus = Boolean.parseBoolean(prop.getProperty("productStatus"));
		productPayload.setProductStatus(productStatus);
		productPayload.setProductFor(prop.getProperty("productFor"));
//		int __v = Integer.parseInt(prop.getProperty("__v"));
		productPayload.set__v(prop.getProperty("__v"));
		productPayload.setProductImage(productAddToCartImage);
		

		addToCartPayload add = new addToCartPayload();
		add.set_id(userId);
		add.setProduct(productPayload);
		String response3 = given().log().all().header("Authorization", token)
				.header("Content-type","application/json").body(add)
		.when().post("api/ecom/user/add-to-cart")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		driver.findElement(By.xpath("//button[@routerlink='/dashboard/cart']")).click();
		JsonPath data3 = parser(response3);
		String message3 = data3.getString("message");
		System.out.println("Message: " + message3);
	}
	
	
	@Test (dependsOnMethods = "deleteOrder")
	public void deleteCartItem() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response4 = given().log().all().header("Authorization", token)
		.when().delete("api/ecom/user/remove-from-cart/"+userId+"/"+productId+"")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath data4 = parser(response4);
		String message4 = data4.getString("message");
		System.out.println("Message: "+ message4);
	}
	
	@Test (dependsOnMethods = "addToCart")
	public void buyNow() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response5 = given().log().all().header("Authorization", token)
		.when().get("api/ecom/product/get-product-detail/"+productId+"")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		driver.findElement(By.xpath("//button[contains(text(),'Buy Now')]")).click();

		JsonPath data5 = parser(response5);
		String message5 = data5.getString("message");
	}
	
	@Test (dependsOnMethods = "buyNow")
	public void createOrder() {
		String country = "India";
		
//		createOrderPayload create = new createOrderPayload();
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		createOrderPayload response6= given().log().all().header("Authorization", token)
		.header("Content-type", "application/json")
		.body("{\r\n"
				+ "  \"orders\": [\r\n"
				+ "    {\r\n"
				+ "      \"country\": \""+country+"\",\r\n"
				+ "      \"productOrderedId\": \""+productId+"\"\r\n"
				+ "    }\r\n"
				+ "  ]\r\n"
				+ "}")
		.when().post("api/ecom/order/create-order")
		.then().log().all().assertThat().statusCode(201).extract().response().as(createOrderPayload.class);
		
		System.out.println("Message: " + response6.getMessage());
		orderId = response6.getOrders().toString().substring(1).split("]")[0];
		System.out.println("OrderId: " + orderId);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.findElement(By.xpath("//button[@routerlink='/dashboard/myorders']")).click();
		
		Assert.assertEquals(driver.findElement(By.xpath("//th[contains(text(),'"+orderId+"')]")).getText(), orderId);
		
	}
	
	@Test (dependsOnMethods = "createOrder")
	public void deleteOrder() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response7 = given().log().all().header("Authorization", token)
		.when().delete("api/ecom/order/delete-order/"+orderId+"")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		driver.findElement(By.xpath("//button[@routerlink='/dashboard/']")).click();
		driver.findElement(By.xpath("//button[@routerlink='/dashboard/myorders']")).click();
		JsonPath data7 = parser(response7);
		String message7 = data7.getString("message");
	}
	
	
	
	@AfterTest
	public void tearDown() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.close();
	}

	public String loginToGetData() {
		// TODO Auto-generated method stub
//		System.setProperty("webdriver.chrome.driver", "C:\\Maven\\WebDriver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://rahulshettyacademy.com/client");
		String productname= prop.getProperty("productName");
		LoginPage login = new LoginPage(driver);
		login.emailM().sendKeys("rajatan98@gmail.com");
		login.passwordM().sendKeys("Neelammanavar@24");
		login.loginM().click();
		
		
		HomePage home = new HomePage(driver);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		int size = home.itemsM().size();
//		List<WebElement> products= driver.findElements(By.tagName("h5"));
//		System.out.println(products);
//		int size = products.size();
		System.out.println("Size of items " + size);
		List<String> items = home.itemsM().stream().map(s -> s.getText()).collect(Collectors.toList());
//		List<String> items = products.stream().map(s -> s.getText()).collect(Collectors.toList());

		System.out.println(items);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		System.out.println(productname.toUpperCase());
		int i=items.indexOf(productname.toUpperCase());
		System.out.println(i);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		String vari= driver.findElement(By.xpath("//div["+(i+1)+"]/div[1]/img[1]")).getAttribute("src");
		System.out.println("Image src: "+vari);
		
		return vari;
	}

}
