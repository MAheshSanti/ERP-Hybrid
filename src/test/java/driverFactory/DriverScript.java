package driverFactory;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunction.FunctionLibrary;
import utilities.ExcelFileUtils;

public class DriverScript 
{
	 WebDriver driver;
	String inputpath="./FileInput\\Controler.xlsx";
	String outputpath="./FileOutput/Hybrid-Result.xlsx";
	String TCSheet="MasterTestCases";
	ExtentReports report;
	ExtentTest logger;

	public void startTest() throws Throwable
	{

		String Module_Status="";
		String Module_New="";

		// create onject for access excel file
		ExcelFileUtils xl = new ExcelFileUtils(inputpath);
		//ExcelFileUtils xl = new ExcelFileUtils(inputpath);
		//iterate for testcases

		for(int i=1;i<=xl.rowcount(TCSheet);i++)
		{
			if(xl.getCellData(TCSheet, i, 2).equalsIgnoreCase("Y"))
			{
			
				// read the sheet
				String TCModule= xl.getCellData(TCSheet, i, 1);
				//define path of html
				report = new ExtentReports("./target/Reports/"+TCModule+FunctionLibrary.genarateDate()+"html");
				logger = report.startTest(TCModule);
				//iterate all the row in TCModule

				for (int j=1;j<=xl.rowcount(TCModule);j++)
				{

					// read cell from tcmodule
					String description = xl.getCellData(TCModule, j, 0);
					String Object_Type = xl.getCellData(TCModule, j, 1);
					String LType = xl.getCellData(TCModule, j, 2);
					String LValue = xl.getCellData(TCModule, j, 3);
					String Test_Data = xl.getCellData(TCModule, j, 4);

					try 
					{

						if(Object_Type.equalsIgnoreCase("startBrowser"))
						{
							driver= FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, description);
						}

						if(Object_Type.equalsIgnoreCase("openUrl"))
						{
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, description);

						}

						if(Object_Type.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibrary.waitForElement(LType, LValue, Test_Data);
							logger.log(LogStatus.INFO, description);

						}

						if(Object_Type.equalsIgnoreCase("typeAction"))
						{
							Thread.sleep(2000);
							FunctionLibrary.typeAction(LType, LValue, Test_Data);
							logger.log(LogStatus.INFO, description);

						}

						if(Object_Type.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(LType, LValue);
							logger.log(LogStatus.INFO, description);

						}

						if(Object_Type.equalsIgnoreCase("validateTitle"))
						{
							FunctionLibrary.validateTitle(Test_Data);
							logger.log(LogStatus.INFO, description);

						}

						if(Object_Type.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, description);

						}
						
						if(Object_Type.equalsIgnoreCase("dropDownAction"))
						{
							FunctionLibrary.dropDownAction(LType, LValue, Test_Data);
							logger.log(LogStatus.INFO, description);

						}
						if(Object_Type.equalsIgnoreCase("CcaptureStock"))
						{
							FunctionLibrary.CcaptureStock(LType, LValue);
							logger.log(LogStatus.INFO, description);

						}
						if(Object_Type.equalsIgnoreCase("stockTable"))
						{
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, description);

						}
						
						if(Object_Type.equalsIgnoreCase("captureSupplier"))
						{
							FunctionLibrary.captureSupplier(LType, LValue);
							logger.log(LogStatus.INFO, description);

						}
						if(Object_Type.equalsIgnoreCase("supplierTable"))
						{
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, description);

						}
						
						if(Object_Type.equalsIgnoreCase("captureCustomer"))
						{
							FunctionLibrary.captureCustomer(LType, LValue);
							logger.log(LogStatus.INFO, description);

						}
						
						if(Object_Type.equalsIgnoreCase("customerTable"))
						{
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, description);

						}
						

						// write as pass into tcmodule sheet

						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						logger.log(LogStatus.PASS, description);
						Module_Status ="true";

					} 
					catch (Exception e)
					{
						System.out.println(e.getMessage());
						// write as fail into tcmodule sheet
						logger.log(LogStatus.FAIL, description);

						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						Module_New ="False";
						
						File screen = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screen, new File("./target/screenshot"+description+FunctionLibrary.genarateDate()+".png"));

					}
					

					if(Module_Status.equalsIgnoreCase("true"))
					{
						xl.setCellData(TCSheet, i, 3, "pass", outputpath);
					}

					report.endTest(logger);
					report.flush();
								
				}

				if(Module_New.equalsIgnoreCase("False"))
				{
					xl.setCellData(TCSheet, i, 3, "Fail", outputpath);
				}

			}
			else
			{
				// write blocked into status cell in test case sheet
				xl.setCellData(TCSheet, i, 3, "Blocked", outputpath);
			}
		}
	}

}
