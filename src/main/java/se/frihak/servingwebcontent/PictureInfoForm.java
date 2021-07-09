package se.frihak.servingwebcontent;

import java.util.Arrays;

public class PictureInfoForm {
	public String newIndex;
	private String[] allaIndex;
	private String[] valdaIndex;
	private String pictureName;
	private String picturedate;
	private boolean invalidDate;
	private String fromDate;
	private String tomDate;

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public String[] getAllaIndex() {
		return allaIndex;
	}

	public void setAllaIndex(String[] allaIndex) {
		this.allaIndex = allaIndex;
	}

	public String[] getValdaIndex() {
		return valdaIndex;
	}

	public void setValdaIndex(String[] valdaIndex) {
		this.valdaIndex = valdaIndex;
	}

	public String getNewIndex() {
		return newIndex;
	}

	public void setNewIndex(String newIndex) {
		this.newIndex = newIndex;
	}
	
	@Override
	public String toString() {
		return "PictureInfoForm [newIndex=" + newIndex + ", allaIndex=" + Arrays.toString(allaIndex) + ", valdaIndex=" + Arrays.toString(valdaIndex) + ", pictureName=" + pictureName + "]";
	}

	public void setPicturedate(String picdate) {
		this.picturedate = picdate;
	}
	
	public String getPicturedate() {
		return this.picturedate;
	}

	public boolean isInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(boolean invalidDate) {
		this.invalidDate = invalidDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getTomDate() {
		return tomDate;
	}

	public void setTomDate(String tomDate) {
		this.tomDate = tomDate;
	}
}
