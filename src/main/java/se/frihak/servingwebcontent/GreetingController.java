package se.frihak.servingwebcontent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import se.frihak.album.Album;
import se.frihak.album.Albums;
import se.frihak.album.Picture;
import se.frihak.index.IndexHandler;
import se.frihak.picture.PicturedateHelper;

@Controller
public class GreetingController {
	private static final String EDIT_INDEXES_PAGE = "editIndexes";
	private static final String SEARCH_RESULTS_PAGE = "searchResults";
	private static final int ANTAL_SPALTER = 5;
	private Albums albums;
	
	public GreetingController(@Value("${pathToAlbums}") String pathToAlbums) {
		albums = new Albums(getFirstExistingPath(pathToAlbums));
	}

	@GetMapping("/")
	public String startpage(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		LocalDateTime idag = LocalDateTime.now();
		List<Album> minaAlbum = albums.getAlbumlist();

		model.addAttribute("name", name);
		model.addAttribute("serverTime", idag.toString());
		model.addAttribute("album", minaAlbum);
		return "index";
	}

	@PostMapping("/newAlbum")
	public String newAlbum(@RequestParam(name="albumname", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("albumName", name);
		System.out.println(model);
		albums.createNewAlbumWithName(name);
		return "redirect:/";
	}

	@GetMapping("/import")
	public String importera(HttpSession session, @RequestParam(name="fileToUpload", required=false, defaultValue="defaultFileToUpload") File name, @RequestParam(name="albumName", defaultValue="hittasInte") String albumName, Model model) throws IOException {
		String pathToPicturesToImport = "/Users/inger/Downloads/Camera Uploads/";
		System.out.println(pathToPicturesToImport);
		
		//flytta bilder till rätt album
		Album album = albums.getAlbum(albumName);
		List<Picture> importedPictures = album.importPicturesFrom(pathToPicturesToImport);
		
		session.setAttribute("foundPictures", importedPictures);

		//redirect till sökresultat med sökta bilder som saknar attribut
		model.addAttribute("albumName", albumName);
		model.addAttribute("importedPictures", importedPictures);
		return SEARCH_RESULTS_PAGE;
	}

	@GetMapping("/searchAllWithoutIndex")
	public String sokAllaUtanIndex(HttpSession session, @RequestParam(name="albumName", defaultValue="hittasInte") String albumName, Model model) throws IOException {
		Album album = albums.getAlbum(albumName);
		List<Picture> foundPictures = album.searchAllWithoutIndex();
		Collections.sort(foundPictures, new SortPicturesByName());
		
		session.removeAttribute("foundPictures");
		session.setAttribute("foundPictures", foundPictures);

		model.addAttribute("albumName", albumName);
		model.addAttribute("importedPictures", foundPictures);
		return SEARCH_RESULTS_PAGE;
	}

	@GetMapping("/image")
	public String image(@RequestParam(name="name", required=false, defaultValue="NamnSaknas") String name, Model model) {
		model.addAttribute("name", name);

		return "image";
	}

	
    @RequestMapping(value = "/picture", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPicture(@RequestParam(name="albumName") String albumName, @RequestParam(name="pictureName") String pictureName) throws IOException {
		Album album = albums.getAlbum(albumName);

        byte[] bytes = StreamUtils.copyToByteArray(album.getFileInputStream(pictureName));

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }

	
	@GetMapping("/album")
	public String getAlbum(@RequestParam(name="name", required=false, defaultValue="World") String albumName, Model model) {
		Album album = albums.getAlbum(albumName);

		List<String> allIndexes = album.getAllIndexes();
		List<List<String>> spalter = album.skapaSpalter(allIndexes, ANTAL_SPALTER);

		PictureInfoForm picInfoForm = new PictureInfoForm();
		picInfoForm.setAllaIndex(allIndexes.toArray(new String[] {}));
		picInfoForm.setValdaIndex(new String[] {});
		picInfoForm.setPictureName("test");
		picInfoForm.setFromDate("0001-01-01");
		picInfoForm.setTomDate("9999-12-31");;
		
		model.addAttribute("picInfoForm", picInfoForm);
		model.addAttribute("allaIndex", allIndexes);
		model.addAttribute("spalter", spalter);
		model.addAttribute("albumName", albumName);
		model.addAttribute("numOfPictures", album.getNumberOfPictures());
		return "album";
	}
	
	@GetMapping("/videos")
	public ResponseEntity<ClassPathResource>  getFullVideo(@RequestParam(name="name", required=false, defaultValue="missingPic.jpg") String name) {
        ClassPathResource video = new ClassPathResource(name);

		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaTypeFactory
						.getMediaType(video)
						.orElse(MediaType.APPLICATION_OCTET_STREAM))
				.body(video);
	}
	
	@GetMapping(value = "/video", produces = "video/mp4")
	@ResponseBody
	public FileSystemResource videoSource(@RequestParam(value="albumName", required=true) String albumName, @RequestParam(value="id", required=true) String id) {
		Album album = albums.getAlbum(albumName);

		return new FileSystemResource(album.getFile(id));
	}
	
	@GetMapping(value = "/editIndexes")
	public String editIndexes(HttpSession session, @RequestParam(value="albumName", required=true) String albumName, @RequestParam(value="pictureName", required=true) String pictureName, Model model) throws IOException {
		Album album = albums.getAlbum(albumName);

		Picture enBild = album.getPicture(pictureName);
		PictureInfoForm picInfoForm = new PictureInfoForm();
		picInfoForm.setAllaIndex(album.getAllIndexes().toArray(new String[] {}));
		picInfoForm.setValdaIndex(enBild.getIndexes().toArray(new String[] {}));
		picInfoForm.setPictureName(pictureName);
		picInfoForm.setPicturedate(enBild.getDate());
		
		model.addAttribute("albumName", albumName);
		model.addAttribute("enTraff", enBild);
		model.addAttribute("picInfoForm", picInfoForm);

		return EDIT_INDEXES_PAGE;
	}
	
	@PostMapping("/updateIndexes")
	public String uppdateraIndex(HttpSession session, @ModelAttribute PictureInfoForm picInfoForm, @RequestParam(value="albumName", required=true) String albumName, Model model) throws IOException {
		Album album = albums.getAlbum(albumName);
		Picture enBild = album.getPicture(picInfoForm.getPictureName());

		if(PicturedateHelper.isInvalidDate(picInfoForm.getPicturedate())) {
			String felaktigtDatum = picInfoForm.getPicturedate();
			picInfoForm = new PictureInfoForm();
			picInfoForm.setAllaIndex(album.getAllIndexes().toArray(new String[] {}));
			picInfoForm.setValdaIndex(enBild.getIndexes().toArray(new String[] {}));
			picInfoForm.setPictureName(enBild.getPictureName());
			picInfoForm.setPicturedate(felaktigtDatum);
			picInfoForm.setInvalidDate(true);
			
			model.addAttribute("albumName", albumName);
			model.addAttribute("enTraff", enBild);
			model.addAttribute("picInfoForm", picInfoForm);

			return EDIT_INDEXES_PAGE;
		}
		
		
		@SuppressWarnings("unchecked") List<Picture> foundPictures = (List<Picture>) session.getAttribute("foundPictures");

		
		IndexHandler idxhanteraren = new IndexHandler(album);
		idxhanteraren.updateIndexes(enBild, picInfoForm.getValdaIndex(), picInfoForm.getNewIndex());
		idxhanteraren.updateDate(enBild, picInfoForm.getPicturedate());
		
		model.addAttribute("albumName", albumName);
		model.addAttribute("importedPictures", foundPictures);
		return SEARCH_RESULTS_PAGE;
	}
	
	@PostMapping("/search")
	public String search(HttpSession session, @ModelAttribute PictureInfoForm picInfoForm, @RequestParam(value="albumName", required=true) String albumName, Model model) throws IOException {
		Album album = albums.getAlbum(albumName);

		List<Picture> indexfilteredPictures = album.searchWithIndex(Arrays.asList(picInfoForm.getValdaIndex()));
		List<Picture> foundPictures = album.filterOnDates(indexfilteredPictures, picInfoForm);
		Collections.sort(foundPictures, new SortPicturesByName());
		
		session.setAttribute("foundPictures", foundPictures);
		
		model.addAttribute("albumName", albumName);
		model.addAttribute("importedPictures", foundPictures);
		return SEARCH_RESULTS_PAGE;
	}
	
	private String getFirstExistingPath(String path) {
		List<String> alla = Arrays.asList(path.split(";"));
		for (String enPath : alla) {
			if(Paths.get(enPath).toFile().exists()) {
				return enPath;
			}
		}
		return null;
	}
	
	@GetMapping(value = "/deletePicture")
	public String deletePicture(HttpSession session, @RequestParam(value="albumName", required=true) String albumName, @RequestParam(value="pictureName", required=true) String pictureName, Model model) throws IOException {
		Album album = albums.getAlbum(albumName);
		Picture enBild = album.getPicture(pictureName);

		@SuppressWarnings("unchecked") List<Picture> foundPictures = (List<Picture>) session.getAttribute("foundPictures");
		removePictureFromList(foundPictures, enBild);
		List<Picture> showPictures = new ArrayList<>(foundPictures);
		session.setAttribute("foundPictures", showPictures);
		enBild.remove();
		IndexHandler idxHandler = new IndexHandler(album);
		idxHandler.removePicturedateIndex(enBild);

		model.addAttribute("albumName", albumName);
		model.addAttribute("importedPictures", showPictures);
		return SEARCH_RESULTS_PAGE;
	}

	private void removePictureFromList(List<Picture> foundPictures, Picture enBild) {
		List<Picture> tempList = new ArrayList<>(foundPictures);
		
		for (Picture picture : tempList) {
			if (picture.getPicturePath().equals(enBild.getPicturePath())) {
				foundPictures.remove(picture);
			}
		}
		
	}
}
