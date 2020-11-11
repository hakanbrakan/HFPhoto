package se.frihak.picture;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

public class PicturedateHelper {
	private static Pattern pattern1 = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}\\.[0-9]{2}\\.[0-9]{2}");
	private static Pattern pattern2 = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}\\.[0-9]{2}\\.[0-9]{2}-[0-9]*");

	public static String guessDateFromFilename(String filename) {
		String filenameWithoutExtension = FilenameUtils.removeExtension(filename);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
		
		if (pattern1.matcher(filenameWithoutExtension).find()) {
			try {
				LocalDate datum = LocalDate.parse(filenameWithoutExtension, formatter);
				return datum.toString();
			} catch (Exception e) {
			}
			
		}

		if (pattern2.matcher(filenameWithoutExtension).find()) {
			try {
				String test = filenameWithoutExtension.substring(0, 19);
				LocalDate datum = LocalDate.parse(test, formatter);
				return datum.toString();
			} catch (Exception e) {
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
