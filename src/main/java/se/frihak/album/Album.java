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
import java.util.Collections;
import java.util.List;

import se.frihak.servingwebcontent.PictureInfoForm;


public class Album {
	private static final int NUM_PICTURES_SEARCH_RESULT = 10;
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
		FileInputStream stream;
		try {
			stream = new FileInputStream(newPath.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(pictureName);
			return null;
		}

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
				
				if (list.size() >= NUM_PICTURES_SEARCH_RESULT) {
					break;
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
		
		traff = Picture.getInstance(picture.toPath(), isPicture(picture.toPath()));
		return traff;
	}

	public File getWordIndexPath() {
		return new File(file, "wordIndex");
	}

	public List<String> getAllIndexes() {
		List<String> allaIndex = new ArrayList<>();

		File[] allWordIndexes = getWordIndexPath().listFiles();
		for (File oneWordFile : allWordIndexes) {
			if (oneWordFile.isDirectory() && !oneWordFile.isHidden()) {
				String indexname = oneWordFile.getName();
				allaIndex.add(indexname);
			}
		}

		Collections.sort(allaIndex);
		return allaIndex;
	}

	public List<List<String>> skapaSpalter(List<String> allaIndex, int antalSpalter) {
		List<List<String>> attReturnera = new ArrayList<>();

		for (int i = 0; i < antalSpalter; i++) {
			List<String> enSpalt = new ArrayList<>();
			attReturnera.add(enSpalt);
		}
		
		int antalIEnSpalt = (allaIndex.size() / antalSpalter) + 1;
		int spaltraknare = 0;
		for (String ettIdx : allaIndex) {
			List<String> lista = attReturnera.get(spaltraknare);
			lista.add(ettIdx);
			if (lista.size() >= antalIEnSpalt) {
				spaltraknare++;
			}
		}
		return attReturnera;
	}

	public List<Picture> searchWithIndex(List<String> valdaIndex) {
		List<String> idxAttKontrollera = new ArrayList<>(valdaIndex);
		List<String> kandidater = new ArrayList<>();

		if ( ! idxAttKontrollera.isEmpty()) {
			String firstWordIndex = idxAttKontrollera.remove(0);
			kandidater = getBilderForWordindex(firstWordIndex);
			List<String> bildindexNamn = new ArrayList<>();
			
			for (String ettWordindex : idxAttKontrollera) {
				bildindexNamn.clear();

				for (String enKandidatbild : kandidater) {
					Path path = Paths.get(getWordIndexPath().getPath(), ettWordindex, enKandidatbild);
					if (path.toFile().exists()) {
						bildindexNamn.add(enKandidatbild);
					}
				}
				kandidater.clear();
				kandidater.addAll(bildindexNamn);
			}
			
		}
		List<Picture> bildkandidater = getPictures(kandidater);
		return bildkandidater;
	}

	private List<Picture> getPictures(List<String> kandidater) {
		List<Picture> funnaBilder = new ArrayList<>();
		
		for (String enKand : kandidater) {
			funnaBilder.add(getPicture(rensaindexnamn(enKand)));
		}

		return funnaBilder;
	}

	private String rensaindexnamn(String enKand) {
		return enKand.replace(".hfidx", "");
	}

	private List<String> getBilderForWordindex(String wordIndex) {
		List<String> kandidaterAttReturnera = new ArrayList<>();
		File wordIndexFolder = new File(getWordIndexPath(), wordIndex);
		
		File[] allPictureCandidates = wordIndexFolder.listFiles();
		for (File enKandidat : allPictureCandidates) {
			if (enKandidat.isFile() && !enKandidat.isHidden()) {
				String kandidatIndexNamn = enKandidat.getName();
				kandidaterAttReturnera.add(kandidatIndexNamn);
			}
		}

		return kandidaterAttReturnera;
	}

	public File getPictureDatePath() {
		return new File(file, "pictureDateIndex");
	}

	public File getDatePicturePath() {
		return new File(file, "datePictureIndex");
	}

	public List<Picture> filterOnDates(List<Picture> indexfilteredPictures, PictureInfoForm picInfoForm) {
		List<Picture> filteredPictures = new ArrayList<>();
		
		if ("0001-01-01".equalsIgnoreCase(picInfoForm.getFromDate()) && "9999-12-31".equalsIgnoreCase(picInfoForm.getTomDate())) {
			filteredPictures.addAll(indexfilteredPictures);
		} else {
			for (Picture onePicture : indexfilteredPictures) {
				if (onePicture.datumInom(picInfoForm.getFromDate(), picInfoForm.getTomDate())) {
					filteredPictures.add(onePicture);
				}
			}
		}


		return filteredPictures ;
	}

	public int getNumberOfPictures() {
		int antalFiler = getPictureFolder().list().length;
		return antalFiler;
	}

}
