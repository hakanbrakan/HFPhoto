package se.frihak.album;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Albums {

	public void createNewAlbumWithName(String albumname) {
		// TODO Auto-generated method stub
		System.out.println("Nu skapas ett nytt album: " + albumname);
		
		Path path = Paths.get("/Users/inger/Documents/MinaAlbum", albumname);
		try {
			Files.createDirectories(path);
			Files.createDirectories(Paths.get(path.toString(),"pictures"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Album> getAlbumlist() {
		List<Album> lista = new ArrayList<Album>();
		Path path = Paths.get("/Users/inger/Documents/MinaAlbum");
		File folder = path.toFile();
		File[] filer = folder.listFiles();
		for (File file : filer) {
			if (file.isDirectory()) {
				lista.add(new Album(file));
			}
		}

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
