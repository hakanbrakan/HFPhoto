package se.frihak.servingwebcontent;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

@Controller
public class GreetingController {

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		LocalDateTime idag = LocalDateTime.now();
		model.addAttribute("serverTime", idag.toString());
		return "greeting";
	}

	@GetMapping("/")
	public String startpage(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		LocalDateTime idag = LocalDateTime.now();
		Albums albums = new Albums();
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
		Albums albums = new Albums();
		albums.createNewAlbumWithName(name);
		return "redirect:/";
	}

	@GetMapping("/import")
	public String importera(@RequestParam(name="fileToUpload", required=false, defaultValue="defaultFileToUpload") File name, @RequestParam(name="albumName", defaultValue="hittasInte") String albumName, Model model) throws IOException {
		String pathToPicturesToImport = "/Users/inger/Downloads/Camera Uploads/";
		System.out.println(pathToPicturesToImport);
		
		//flytta bilder till rätt album
		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);
		List<Picture> importedPictures = album.importPicturesFrom(pathToPicturesToImport);
		
		//redirect till sökresultat med sökta bilder som saknar attribut
		model.addAttribute("albumName", albumName);
		model.addAttribute("importedPictures", importedPictures);
		return "searchResults";
	}

	@GetMapping("/searchAllWithoutIndex")
	public String sokAllaUtanIndex(@RequestParam(name="albumName", defaultValue="hittasInte") String albumName, Model model) throws IOException {
		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);
		List<Picture> foundPictures = album.searchAllWithoutIndex();
		
		model.addAttribute("albumName", albumName);
		model.addAttribute("importedPictures", foundPictures);
		return "searchResults";
	}

	@GetMapping("/image")
	public String image(@RequestParam(name="name", required=false, defaultValue="NamnSaknas") String name, Model model) {
		model.addAttribute("name", name);

		return "image";
	}

	
    @RequestMapping(value = "/picture", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPicture(@RequestParam(name="albumName") String albumName, @RequestParam(name="pictureName") String pictureName) throws IOException {

		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);

        byte[] bytes = StreamUtils.copyToByteArray(album.getFileInputStream(pictureName));

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }

	
	@GetMapping("/album")
	public String getAlbum(@RequestParam(name="name", required=false, defaultValue="World") String albumName, Model model) {
		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);

		List<String> allIndexes = album.getAllIndexes();

		
		PictureInfoForm picInfoForm = new PictureInfoForm();
		picInfoForm.setAllaIndex(allIndexes.toArray(new String[] {}));
		picInfoForm.setValdaIndex(new String[] {});
		picInfoForm.setPictureName("test");
		
		model.addAttribute("picInfoForm", picInfoForm);

		
		

		model.addAttribute("allaIndex", allIndexes);
		model.addAttribute("albumName", albumName);
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
		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);

		return new FileSystemResource(album.getFile(id));
	}
	
	@GetMapping(value = "/editIndexes")
	public String editIndexes(@RequestParam(value="albumName", required=true) String albumName, @RequestParam(value="pictureName", required=true) String pictureName, Model model) throws IOException {
		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);

		Picture enBild = album.getPicture(pictureName);
		PictureInfoForm picInfoForm = new PictureInfoForm();
		picInfoForm.setAllaIndex(album.getAllIndexes().toArray(new String[] {}));
		picInfoForm.setValdaIndex(enBild.getIndexes().toArray(new String[] {}));
		picInfoForm.setPictureName(pictureName);
		
		model.addAttribute("albumName", albumName);
		model.addAttribute("enTraff", enBild);
		model.addAttribute("picInfoForm", picInfoForm);

		return "editindexes";
	}
	
	@PostMapping("/updateIndexes")
	public String uppdateraIndex(@ModelAttribute PictureInfoForm picInfoForm, @RequestParam(value="albumName", required=true) String albumName, Model model) throws IOException {
		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);

		Picture enBild = album.getPicture(picInfoForm.getPictureName());
		
		IndexHandler idxhanteraren = new IndexHandler(album);
		idxhanteraren.updateIndexes(enBild, picInfoForm.getValdaIndex(), picInfoForm.getNewIndex());
		

		return sokAllaUtanIndex(albumName, model);
	}
	
	@PostMapping("/search")
	public String search(@ModelAttribute PictureInfoForm picInfoForm, @RequestParam(value="albumName", required=true) String albumName, Model model) throws IOException {
		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);

		List<Picture> foundPictures = album.searchWithIndex(Arrays.asList(picInfoForm.getValdaIndex()));
		
		model.addAttribute("albumName", albumName);
		model.addAttribute("importedPictures", foundPictures);
		return "searchResults";
	}
}
