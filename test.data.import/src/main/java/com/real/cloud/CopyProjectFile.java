package com.real.cloud;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CopyProjectFile {
	public static void main(String[] args) {
		try {
			// POIFSFileSystem fs=new POIFSFileSystem();
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("D:\\GitHub\\demo\\test.data.import\\代码目录调整.xlsx"));
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowNum = sheet.getLastRowNum();
			String srcDir = null;
			String descDir = null;
			String copyDir = null;
			for (int i = 1; i < rowNum+1; i++) {

				XSSFRow row = sheet.getRow(i);
				XSSFCell cell = row.getCell(0);
				if (cell != null) {
					descDir = cell.getStringCellValue();
				} else {
					descDir = null;
				}

				cell = row.getCell(1);
				if (cell != null) {
					srcDir = cell.getStringCellValue();
				} else {
					srcDir = null;
				}

				cell = row.getCell(2);
				if (cell != null) {
					copyDir = cell.getStringCellValue();
				} else {
					copyDir = null;
				}
				 srcDir=srcDir+"\\"+copyDir;
				 descDir=descDir+"\\"+copyDir;
				 if(srcDir.indexOf(".loantimer.slice")<0) {
					 FileTools.cp(srcDir, descDir,true,false);
				 }
				 
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
