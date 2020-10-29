package se.frihak.album;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Albums {
	private String pathToAlbums;

	public Albums(String pathToAlbums) {
		this.pathToAlbums = pathToAlbums;
	}

	public void createNewAlbumWithName(String albumname) {
		Path path = Paths.get(pathToAlbums, albumname);
		
		try {
			Files.createDirectories(path);
			Files.createDirectories(Paths.get(path.toString(),"pictures"));
			Files.createDirectories(Paths.get(path.toString(),"pictureIndex"));
			Files.createDirectories(Paths.get(path.toString(),"wordIndex"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Album> getAlbumlist() {
		List<Album> lista = new ArrayList<Album>();
		Path path = Paths.get(pathToAlbums);
		File folder = path.toFile();
		File[] filer = folder.listFiles();
		for (File file : filer) {
			if (file.isDirectory()) {
				lista.add(new Album(file));
			}
		}
		Collections.sort(lista, new SortByName());

		return lista;
	}

	public Album getAlbum(String albumName) {
		List<Album> list = getAlbumlist();
		for (Album album : list) {
			if (album.getNamn().equals(albumName)) {
				return album;
			}
		}
		return null;
	}

}
