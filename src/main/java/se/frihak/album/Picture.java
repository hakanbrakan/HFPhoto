package se.frihak.album;

import java.nio.file.Path;

public class Picture {
	private Path picturePath;
	private boolean isPicture;
	

	private Picture(Path picturePath, boolean isPicture) {
		this.picturePath = picturePath;
		this.isPicture = isPicture;
	}
	
	public static Picture getInstance(Path picturePath, boolean isPicture) {
		return new Picture(picturePath, isPicture);
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
