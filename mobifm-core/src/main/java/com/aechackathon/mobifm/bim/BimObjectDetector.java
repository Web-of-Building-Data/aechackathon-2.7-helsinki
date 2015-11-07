package com.aechackathon.mobifm.bim;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class BimObjectDetector {
	
	public static final String COLUMN_GUID = "GUID";
	public static final String COLUMN_X1 = "x1";
	public static final String COLUMN_Y1 = "y1";
	public static final String COLUMN_X2 = "x2";
	public static final String COLUMN_Y2 = "y2";
	
	private final CSVParser csvParser;
	
	public BimObjectDetector(String csvFilePath) throws IOException {
		
		Reader reader = new FileReader(csvFilePath);
		csvParser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
		
	}
	
	public String getObject(int x, int y) {
		for (CSVRecord record : csvParser) {
			
			int x1 = Integer.parseInt(record.get(COLUMN_X1));
			int y1 = Integer.parseInt(record.get(COLUMN_Y1));
			int x2 = Integer.parseInt(record.get(COLUMN_X2));
			int y2 = Integer.parseInt(record.get(COLUMN_Y2));
			
			if (isInRectArea(x, y, x1, y1, x2, y2)) {
				return record.get(COLUMN_GUID);
			}
			
		}
		
		return null;
	}
	
	public static boolean isInRectArea(int x, int y, int x1, int y1, int x2, int y2) {
		
		return x1 <= x && x <= x2 && y1 <= y && y2 <= y2;
		
	}
	

}
