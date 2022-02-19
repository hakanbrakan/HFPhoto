package se.frihak.picture;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

public class PicturedateHelper {
	private static Pattern pattern1 = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
	private static Pattern pattern2 = Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}");

	public static String guessDateFromFilename(String filename) {
		String filenameWithoutExtension = FilenameUtils.removeExtension(filename);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd");

		String dateFromFilename;
		if (filenameWithoutExtension.length() < 10) {
			dateFromFilename = "";
		} else {
			dateFromFilename = filenameWithoutExtension.substring(0, 10);
		}
		
		
		if (pattern1.matcher(dateFromFilename).find()) {
			try {
				LocalDate datum = LocalDate.parse(dateFromFilename, formatter);
				return datum.toString();
			} catch (Exception e) {
			}
			
		}

		dateFromFilename = filenameWithoutExtension.substring(0, 8);
		if (pattern2.matcher(dateFromFilename).find()) {
			try {
				LocalDate datum = LocalDate.parse(dateFromFilename, formatter2);
				return datum.toString();
			} catch (Exception e) {
				return "<datum saknas>";
			}
			
		}

		return "<datum saknas>";
	}

	public static boolean isInvalidDate(String picturedate) {
		return ! isValidDate(picturedate);
	}

	private static boolean isValidDate(String picturedate) {
		try {
			LocalDate.parse(picturedate);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

}
