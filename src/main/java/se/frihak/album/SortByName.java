package se.frihak.album;

import java.util.Comparator;

public class SortByName implements Comparator<Album> {

	@Override
	public int compare(Album o1, Album o2) {
		return o1.getNamn().compareTo(o2.getNamn());
	}

}
