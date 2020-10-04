package se.frihak.album;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Album {

	final private File file;

	public Album(File file) {
		this.file = file;
	}

	public String getNamn() {
		return file.getName();
	}

	@Override
	public String toString() {
		return "Album [namn=" + getNamn() + "]";
	}

	public List<Soktraff> importPicturesFrom(String pathToPicturesToImport) throws IOException {
		List<Path> importedPictures = new ArrayList<Path>();
		// TODO Auto-generated method stub
		//Nu kopierar vi filer
		System.out.println("kopiera från " + pathToPicturesToImport);
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.equalsIgnoreCase("pictures");
			}
		};
		File picFolder = file.listFiles(filter )[0];
		System.out.println(picFolder);
		
		FilenameFilter pictureFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".mov") || name.endsWith(".jpg");
			}
		};
		File[] filer = Paths.get(pathToPicturesToImport).toFile().listFiles(pictureFilter );
		for (File file : filer) {
			if ( ! file.isDirectory()) {
				System.out.println(file.toString());
				Path newPath = Paths.get(picFolder.toPath().toString(), file.getName());
				Files.copy(file.toPath(), newPath);
				importedPictures.add(newPath);
			}
		}
		
		List<Soktraff> soktraffar = createSearchResultsFrom(importedPictures);
		
		return soktraffar;

	}

	private List<Soktraff> createSearchResultsFrom(List<Path> importedPictures) {
		List<Soktraff> list = new ArrayList<Soktraff>();
		for (Path picturePath : importedPictures) {
			Soktraff traff = Soktraff.getInstance(picturePath, isPicture(picturePath));
			list.add(traff);
		}

		return list;
	}

	private boolean isPicture(Path picturePath) {
		Path name = picturePath.getFileName();
		return name.toString().endsWith(".jpg");
	}

}
