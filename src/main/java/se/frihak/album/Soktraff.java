package se.frihak.album;

import java.nio.file.Path;

public class Soktraff { //TODO Kanske döpa om till typ pictureinfo
	private Path picturePath;
	private boolean isPicture;
	

	private Soktraff(Path picturePath, boolean isPicture) {
		this.picturePath = picturePath;
		this.isPicture = isPicture;
	}
	
	public static Soktraff getInstance(Path picturePath, boolean isPicture) {
		return new Soktraff(picturePath, isPicture);
	}
	
	public Path getPicturePath() {
		return picturePath;
	}

	public String getPictureName() {
		return getPicturePath().getFileName().toString();
	}

	public boolean isPicture() {
		return isPicture;
	}

	public boolean getIsPicture() {
		return isPicture();
	}

}
