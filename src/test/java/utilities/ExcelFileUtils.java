package utilities;

import java.io.FileInputStream; 
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileUtils {

	XSSFWorkbook wb;
	//Constructor for reading excel path
	public ExcelFileUtils(String excelpath) throws Throwable
	{
		FileInputStream fi = new FileInputStream(excelpath);
		wb = new XSSFWorkbook(fi);
	}
	
	// method for counting row in a sheet
	
	public int rowcount(String sheetName)
	{
		return wb.getSheet(sheetName).getLastRowNum();
	}
	
	// method for reading data form cell
	
	public String getCellData(String sheetname,int row,int col)
	{
		String data="";
		if(wb.getSheet(sheetname).getRow(row).getCell(col).getCellType()==CellType.NUMERIC)
		{
			int celldata = (int) wb.getSheet(sheetname).getRow(row).getCell(col).getNumericCellValue();
			 data = String.valueOf(celldata);
			 
			 
		}
		else
		{
			data =wb.getSheet(sheetname).getRow(row).getCell(col).getStringCellValue();
		}
		return data;
	}
	
	
	//method for writing status in workbook 
	
	public void setCellData(String sheetname,int row,int column,String status,String writeExcel) throws Throwable
	{
		// get sheet from  workbook
		
		XSSFSheet ws = wb.getSheet(sheetname);
		//get row from sheet
		XSSFRow rownum = ws.getRow(row);
		//create cell in row
		XSSFCell cell = rownum.createCell(column);
		//write status into cell
		cell.setCellValue(status);
		
		if (status.equalsIgnoreCase("pass")) 
		{
			XSSFCellStyle style  = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			font.setColor(IndexedColors.GREEN.getIndex());
			font.setBold(true);
			style.setFont(font);
			rownum.getCell(column).setCellStyle(style);
			
		
		}
		else if (status.equalsIgnoreCase("fail"))
		{
			XSSFCellStyle style  = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			font.setColor(IndexedColors.RED.getIndex());
			font.setBold(true);
			style.setFont(font);
			rownum.getCell(column).setCellStyle(style);
		}
		else if (status.equalsIgnoreCase("blocked"))
		{
			XSSFCellStyle style  = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			font.setColor(IndexedColors.BLUE.getIndex());
			font.setBold(true);
			style.setFont(font);
			rownum.getCell(column).setCellStyle(style);
		}
		
		FileOutputStream fo  = new FileOutputStream(writeExcel);
		wb.write(fo);
		
	}
	
	
	
}
