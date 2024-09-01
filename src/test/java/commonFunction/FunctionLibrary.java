package commonFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import io.opentelemetry.sdk.autoconfigure.spi.internal.ConditionalResourceProvider;

public class FunctionLibrary
{
	public static WebDriver driver;
	public static Properties conpro ;

	// method for launch browser
	public static WebDriver startBrowser() throws Throwable
	{
		conpro = new Properties();
		conpro.load(new FileInputStream("./PropertyFiles/Enviornment.properties"));
		if(conpro.getProperty("Browser").equalsIgnoreCase("Chrome"))
		{
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}

		else if(conpro.getProperty("Browser").equalsIgnoreCase("fireFox"))
		{
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
		}
		return driver;
	}

	// launch the url in the browser

	public static void openUrl()
	{
		driver.get(conpro.getProperty("Url"));
	}

	// wait for any web element

	public static void waitForElement(String LocatorType,String LocatorValue,String TestData)
	{
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));

		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));

		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));

		}
	}

	//method for   enter data into any  textbox

	public static void typeAction(String LocatorType,String LocatorValue,String TestData)

	{
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}

	}

	// method for any element like button,chekbox, radio button,images,links
	public static void clickAction(String LocatorType,String LocatorValue) throws Throwable
	{
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			Thread.sleep(2000);
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
	}

	// method for validate title
	public static void validateTitle(String Expected_Title)
	{
		String Actual_Title = driver.getTitle();
		try {
			Assert.assertEquals(Actual_Title, Expected_Title,"Title is not matching");
		}
		catch (AssertionError a)
		{
			System.out.println(a.getMessage());
		}

	}

	// method for close the browser
	public static void closeBrowser()
	{
		driver.quit();
	}

	// method for date generate
	public static String genarateDate()
	{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY-mm-DD - hh-mm");
		return df.format(date);
	}
	// method for select data from dropdown list

	public static void dropDownAction(String LocatorType,String LocatorValue, String TestData)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			int value = Integer.parseInt(TestData);
			Select dr = new Select(driver.findElement(By.xpath(LocatorValue)));
			dr.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			int value = Integer.parseInt(TestData);
			Select dr = new Select(driver.findElement(By.name(LocatorValue)));
			dr.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			int value = Integer.parseInt(TestData);
			Select dr = new Select(driver.findElement(By.id(LocatorValue)));
			dr.selectByIndex(value);
		}
	}

	// method for capturing data (stock number) & store into notepad
	public static void CcaptureStock(String LocatorType,String LocatorValue) throws Throwable
	{
		String stocknum="";
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			stocknum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			stocknum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			stocknum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}

		// create notepad file into capture data folder
		FileWriter fw = new FileWriter("./CaptureData/StockNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stocknum);
		bw.flush();
		bw.close();

	}

	// method for verify stock number in stock table
	public static void stockTable() throws Throwable
	{
		//read stock number from above note pad
		FileReader fr = new FileReader("./CaptureData/StockNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_data =br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(5000);
		
		String Act_Data = driver.findElement(By.xpath("//table[@id='tbl_a_stock_itemslist']/tbody/tr/td[8]/div/span/span")).getText();
		System.out.println(Act_Data);
		Reporter.log(Exp_data+"      "+Act_Data,true);
		try {
			Assert.assertEquals(Act_Data, Exp_data,"Stock number Not Matching");
		}catch (AssertionError a) 
		{
			System.out.println(a.getMessage());

		}
	}

// capture supplier number into notepad
	public static void captureSupplier(String LocatorType,String LocatorValue) throws Throwable
	{
		String supppliernum="";
		if(LocatorType.equalsIgnoreCase("name"))
		{
			supppliernum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			supppliernum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			supppliernum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		
		FileWriter fr = new FileWriter("./CaptureData/SupplierNumber.txt");
		BufferedWriter bw = new BufferedWriter(fr);
		bw.write(supppliernum);
		bw.flush();
		bw.close();
	}
	
	// method for verify supplier table
	public static void supplierTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/SupplierNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		 String Exp_data= br.readLine();
		 if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
				driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
			driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
			driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_data);
			driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
			Thread.sleep(3000);
			
			String Act_data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr/td[6]/div/span/span")).getText();
			Reporter.log(Act_data + "   "+ Exp_data,true);
			try 
			{
			Assert.assertEquals(Act_data,Exp_data,"Actual and expected data not matching" );	
			} catch (AssertionError a) 
			{
				System.out.println(a.getMessage());
			}
			
	}
	
	// method for capture customer number
	public static void captureCustomer(String LocatorType, String LocatorValue) throws Throwable
	{
		String Customer_num="";
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			Customer_num = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");	
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			Customer_num = driver.findElement(By.name(LocatorValue)).getAttribute("value");	
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			Customer_num = driver.findElement(By.id(LocatorValue)).getAttribute("value");	
		}
		
		FileWriter fw = new FileWriter("./CaptureData/CustomerNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(Customer_num);
		bw.flush();
		bw.close();
	}
	
	// method for verify the customer table
	public static void customerTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/CustomerNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_data = br.readLine();
		
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		
		String Act_data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr/td[5]/div/span/span")).getText();
		Reporter.log(Exp_data+"      "+Act_data,true);
		try 
		{
		Assert.assertEquals(Act_data, Exp_data,"Customer number not matching");
			
		} catch (AssertionError e) 
		{
			System.out.println(e.getMessage());
		}
	}
}

