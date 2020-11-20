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
		List<String> alla = Arrays.asList(newIndex.split("[\\s,:;]+"));
		List<String> indexesAfterChange = new ArrayList<>();
		for (String string : alla) {
			if ( ! string.isEmpty()) {
				indexesAfterChange.add(string);
			}
		}
		indexesAfterChange.addAll(toArray(valdaIndex));
		return indexesAfterChange;
	}

	private List<String> toArray(String[] valdaIndex) {
		if (valdaIndex == null) {
			return new ArrayList<String>();
		} else {
			return Arrays.asList(valdaIndex);
		}
	}

	private void storeWordindex(Picture enBild, List<String> gamlaIndex, List<String> indexesAfterChange) throws IOException {
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
			Path path = Paths.get(wordIdxPath.toPath().toString(), idx, enBild.getPictureName()+".hfidx");
			//File wordIndexfile1 = new File(wordIdxPath, idx);
			//File wordIndexfile2 = new File(wordIndexfile1, enBild.getPictureName()+"hfidx");
			Path parentDir = path.getParent();
			if (!Files.exists(parentDir)) {
				Files.createDirectories(parentDir);
			}
			
			Files.write(path, Arrays.asList("hej"), StandardCharsets.UTF_8);
		}
		
		//Ta bort index som inte används mer
		for (String idx : indexesToRemove) {
			Path path = Paths.get(wordIdxPath.toPath().toString(), idx, enBild.getPictureName()+".hfidx");
			
			Files.deleteIfExists(path);
			Path parentDir = path.getParent();
			if (parentDir.toFile().listFiles().length <= 0) {
				Files.deleteIfExists(parentDir);
			}

		}
		
	}

	public void updateDate(Picture enBild, String picturedate) throws IOException {
		String indexToRemove = enBild.getDate();
		if ( ! indexToRemove.equalsIgnoreCase(picturedate)) {
			storePictureDateindex(enBild, picturedate, indexToRemove);
			storeDatePictureindex(enBild, picturedate, indexToRemove);
		}
	}

	private void storeDatePictureindex(Picture enBild, String picturedate, String indexToRemove) throws IOException {
		File picDatePath = album.getDatePicturePath();
		
//		String indexToRemove = enBild.getDate();
		String indexToAdd = picturedate;
		
		//Ta bort index som inte används mer
		Path path = Paths.get(picDatePath.toPath().toString(), indexToRemove, enBild.getPictureName()+".hfidx");
		
		Files.deleteIfExists(path);
		Path parentDir = path.getParent();
		if (parentDir.toFile().exists()) {
			if (parentDir.toFile().listFiles().length <= 0) {
				Files.deleteIfExists(parentDir);
			}
		}
		
		//Lägg till nya index
		Path path2 = Paths.get(picDatePath.toPath().toString(), indexToAdd, enBild.getPictureName()+".hfidx");
		Path parentDir2 = path2.getParent();
		if (!Files.exists(parentDir2)) {
			Files.createDirectories(parentDir2);
		}
		
		Files.write(path2, Arrays.asList("hej"), StandardCharsets.UTF_8);
	}

	private void storePictureDateindex(Picture enBild, String picturedate, String indexToRemove) throws IOException {
		File picDatePath = album.getPictureDatePath();
		
		//String indexToRemove = enBild.getDate();
		String indexToAdd = picturedate;
		
		//Ta bort index som inte används mer
		Path path = Paths.get(picDatePath.toPath().toString(), enBild.getPictureName(), indexToRemove+".hfidx");
		
		Files.deleteIfExists(path);
		Path parentDir = path.getParent();
		if (parentDir.toFile().exists()) {
			if (parentDir.toFile().listFiles().length <= 0) {
				Files.deleteIfExists(parentDir);
			}
		}
		
		//Lägg till nya index
		Path path2 = Paths.get(picDatePath.toPath().toString(), enBild.getPictureName(), indexToAdd+".hfidx");
		Path parentDir2 = path2.getParent();
		if (!Files.exists(parentDir2)) {
			Files.createDirectories(parentDir2);
		}
		
		Files.write(path2, Arrays.asList("hej"), StandardCharsets.UTF_8);
		
	}
}
