package se.frihak.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.frihak.album.Album;
import se.frihak.album.Picture;

public class IndexHandler {

	private Album album;

	public IndexHandler(Album album) {
		this.album = album;
	}

	public void updateIndexes(Picture enBild, String[] valdaIndex, String newIndex) throws IOException {
		List<String> gamlaIndex = enBild.getIndexes();
		List<String> indexesAfterChange = createListOfIndexes(valdaIndex, newIndex);
		enBild.storeIndex(indexesAfterChange);
		/*
		 * ändra wordindex
		 */
	}

	private List<String> createListOfIndexes(String[] valdaIndex, String newIndex) {
		/*
		 * skapa lista över nytt indexutseende
		 */
		List<String> alla = Arrays.asList(newIndex.split("\\s*,\\s*"));
		List<String> indexesAfterChange = new ArrayList<>();
		for (String string : alla) {
			if ( ! string.isEmpty()) {
				indexesAfterChange.add(string);
			}
		}
		indexesAfterChange.addAll(Arrays.asList(valdaIndex));
		return indexesAfterChange;
	}

}
