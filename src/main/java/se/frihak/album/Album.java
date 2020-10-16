package se.frihak.album;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
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

	public List<Picture> importPicturesFrom(String pathToPicturesToImport) throws IOException {
		List<Path> importedPictures = new ArrayList<Path>();

		//Nu kopierar vi filer
		System.out.println("kopiera från " + pathToPicturesToImport);
		File picFolder = getPictureFolder();
		
		FilenameFilter pictureFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".mov") || name.endsWith(".mp4") || name.endsWith(".jpg");
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
		
		List<Picture> soktraffar = createSearchResultsFrom(importedPictures);
		
		return soktraffar;

	}

	private File getPictureFolder() {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.equalsIgnoreCase("pictures");
			}
		};
		File picFolder = file.listFiles(filter )[0];
		return picFolder;
	}

	private List<Picture> createSearchResultsFrom(List<Path> importedPictures) {
		List<Picture> list = new ArrayList<Picture>();
		for (Path picturePath : importedPictures) {
			Picture traff = Picture.getInstance(picturePath, isPicture(picturePath));
			list.add(traff);
		}

		return list;
	}

	private boolean isPicture(Path picturePath) {
		Path name = picturePath.getFileName();
		return name.toString().endsWith(".jpg");
	}

	public InputStream getFileInputStream(String pictureName) throws FileNotFoundException {
		File picFolder = getPictureFolder();
		Path newPath = Paths.get(picFolder.toPath().toString(), pictureName);
		FileInputStream stream = new FileInputStream(newPath.toString());
		// TODO Auto-generated method stub
		return stream;
	}

	public File getFile(String id) {
		File picFolder = getPictureFolder();
		Path newPath = Paths.get(picFolder.toString(), id);
		return newPath.toFile();
	}

	public List<Picture> searchAllWithoutIndex() {
		List<Picture> list = new ArrayList<Picture>();
		File picFolder = getPictureFolder();
		File pictureIndexPath = getPictureIndexPath();
		
		File[] allPics = picFolder.listFiles();
		for (File onePic : allPics) {
			if (onePic.isFile() && !onePic.isHidden()) {
				String picFilename = onePic.getName();
				boolean exists = new File(pictureIndexPath, picFilename+".hfidx").exists();
				if ( ! exists) {
					Picture traff = Picture.getInstance(onePic.toPath(), isPicture(onePic.toPath()));
					list.add(traff);
				}
			}
		}

		return list;
	}

	private File getPictureIndexPath() {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.equalsIgnoreCase("pictureIndex");
			}
		};
		File picIndex = file.listFiles(filter )[0];
		return picIndex;
	}

	public Picture getPicture(String pictureName) {
		File picFolder = getPictureFolder();
		Picture traff = null;
		
		File picture = new File(picFolder, pictureName);
		List<PictureIndex> allaIndex = getPictureIndex(pictureName);
		
		traff = Picture.getInstance(picture.toPath(), isPicture(picture.toPath()));
		return traff;
	}

	private List<PictureIndex> getPictureIndex(String pictureName) {
		List<PictureIndex> allaIndex = new ArrayList<PictureIndex>();
		File pictureIndexPath = getPictureIndexPath();
		File indexfile = new File(pictureIndexPath, pictureName+".hfidx");
		
		if (indexfile.exists()) {
			if (indexfile.isFile() && !indexfile.isHidden()) {
				boolean exists = new File(pictureIndexPath, pictureName+".hfidx").exists();
				if (exists) {
					//TODO läs upp alla index
				}
			}
			
		}
		return allaIndex;
	}

}
