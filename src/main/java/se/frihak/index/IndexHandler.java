package se.frihak.index;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		storeWordindex(enBild, gamlaIndex, indexesAfterChange);
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

	private void storeWordindex(Picture enBild, List<String> gamlaIndex, List<String> indexesAfterChange) throws IOException {
		System.out.println("storeWordindex");
		File wordIdxPath = album.getWordIndexPath();
		
		List<String> indexesToRemove = new ArrayList<>(gamlaIndex);
		List<String> indexesToAdd = new ArrayList<>(indexesAfterChange);
		
		for (String ettGammaltIndex : gamlaIndex) {
			if (indexesAfterChange.contains(ettGammaltIndex)) {
				//Uppdatering behövs ej
				indexesToRemove.remove(ettGammaltIndex);
				indexesToAdd.remove(ettGammaltIndex);
			}
		}
		
		//Lägg till nya index
		for (String idx : indexesToAdd) {
			Path path = Paths.get(album.getWordIndexPath().toPath().toString(), idx, enBild.getPictureName()+".hfidx");
			//File wordIndexfile1 = new File(wordIdxPath, idx);
			//File wordIndexfile2 = new File(wordIndexfile1, enBild.getPictureName()+"hfidx");
			Path parentDir = path.getParent();
			if (!Files.exists(parentDir)) {
				Files.createDirectories(parentDir);
			}
			
			Files.write(path, Arrays.asList("hej"), StandardCharsets.UTF_8);
		}
		
		//TODO Ta bort index som inte används mer
		
	}
}
