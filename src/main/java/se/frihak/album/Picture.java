package se.frihak.album;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import se.frihak.index.Filehandler;

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

	public void storeIndex(List<String> indexesToStore) throws IOException {
		//Filehandler filehandler = new Filehandler();
		File indexfile = getIndexfile();
		//List<String> indexesBeforeChange = filehandler.getContentAsList(album.getPictureIndexPath(), enBild.getPictureName()+".hfidx");
		
		Files.write(indexfile.toPath(), indexesToStore);
		
	}
	
	public List<String> getIndexes() throws IOException {
		List<String> allaIndex = new ArrayList<String>();
		File indexfile = getIndexfile();
		
		if (indexfile.exists()) {
			List<String> lines = Files.readAllLines(Paths.get(indexfile.toString()));

			allaIndex.addAll(lines);
		}
		return allaIndex;
	}

	private File getIndexfile() {
		Path parent = picturePath.getParent().getParent();
		File pictureIndexPath = new File(parent.toFile(), "pictureIndex");
		File indexfile = new File(pictureIndexPath, picturePath.getFileName()+".hfidx");
		return indexfile;
	}

}
