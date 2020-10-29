package se.frihak.servingwebcontent;

import java.util.Comparator;

import se.frihak.album.Picture;

public class SortPicturesByName implements Comparator<Picture> {

	@Override
	public int compare(Picture pic1, Picture pic2) {
		return pic1.getPictureName().compareTo(pic2.getPictureName());
	}

}
