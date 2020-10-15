package se.frihak.servingwebcontent;

import java.io.File;
import java.io.FileInputStream;
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
import se.frihak.album.Soktraff;

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
		model.addAttribute("name", name);
		LocalDateTime idag = LocalDateTime.now();
		model.addAttribute("serverTime", idag.toString());
		System.out.println("i startsidan");
		Albums albums = new Albums();
		List<Album> minaAlbum = albums.getAlbumlist();
		model.addAttribute("album", minaAlbum);
		System.out.println(model);
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
		//String pathToPicturesToImport = "/Users/inger/gitRepos/HFPhoto/src/main/resources/";
		String pathToPicturesToImport = "/Users/inger/Downloads/Camera Uploads/";
		System.out.println(pathToPicturesToImport);
		
		//flytta bilder till rätt album
		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);
		List<Soktraff> importedPictures = album.importPicturesFrom(pathToPicturesToImport);
		
		//redirect till sökresultat med sökta bilder som saknar attribut
		model.addAttribute("albumName", albumName);
		model.addAttribute("importedPictures", importedPictures);
		return "searchResults";
	}

	@GetMapping("/searchAllWithoutIndex")
	public String sokAllaUtanIndex(@RequestParam(name="albumName", defaultValue="hittasInte") String albumName, Model model) throws IOException {
		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);
		List<Soktraff> foundPictures = album.searchAllWithoutIndex();
		
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

	
    @RequestMapping(value = "/sidTABORTDENNA", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@RequestParam(name="name", required=false, defaultValue="e16_e17") String name) throws IOException {

    		//TODO Denna kan nog tas bort
    	
        ClassPathResource imgFile = new ClassPathResource("1965075.jpg");
//        FileInputStream fil = new FileInputStream("/Users/inger/gitRepos/gs-serving-web-content/complete/src/main/resources/1965075.jpg");
//        FileInputStream fil = new FileInputStream("/Users/inger/Downloads/e16_e17.jpg");
        FileInputStream fil = new FileInputStream("/Users/inger/Downloads/"+name+".jpg");
//        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
        byte[] bytes = StreamUtils.copyToByteArray(fil);
        System.out.println("test");

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }

	
	@GetMapping("/album")
	public String getAlbum(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("albumName", name);

		return "album";
	}
	
	@GetMapping("/film")
	public String film(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		System.out.println("film: " + name);
		return "film";
	}
	
	
	@GetMapping("/videos")
	public ResponseEntity<ClassPathResource>  getFullVideo(@RequestParam(name="name", required=false, defaultValue="2020-08-10 16.30.28.mp4") String name) {
        ClassPathResource video = new ClassPathResource(name);
		System.out.println("videos: " + name);
//		video = UrlResource("file:$videoLocation/$name");
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
	
	@GetMapping(value = "/videosrcTABORT", produces = "video/mp4")
	@ResponseBody
	public FileSystemResource videoSourceTABORT(@RequestParam(value="id", required=true) String id) {
		System.out.println("id: " + id);
		
		if (id.equalsIgnoreCase("fff"))  {
			return new FileSystemResource(new File("/Users/inger/gitRepos/HFPhoto/src/main/resources/2020-09-26 22.53.18.mov"));
			
		} else {
			return new FileSystemResource(new File("/Users/inger/gitRepos/HFPhoto/src/main/resources/2020-09-26 22.53.28.mov"));
				
		}
	}
	
	@GetMapping(value = "/editIndexes")
	public String editIndexes(@RequestParam(value="albumName", required=true) String albumName, @RequestParam(value="pictureName", required=true) String pictureName, Model model) {
		Albums albums = new Albums();
		Album album = albums.getAlbum(albumName);

		Soktraff enBild = album.getPicture(pictureName);
		PictureInfoForm picInfoForm = new PictureInfoForm();
		picInfoForm.setAllaIndex(new String[] { "One12", "Two", "Three" });
		picInfoForm.setValdaIndex(new String[] { "Two", "Three" });
		
		model.addAttribute("albumName", albumName);
		model.addAttribute("enTraff", enBild);
		model.addAttribute("picInfoForm", picInfoForm);

		return "editindexes";
	}
	
	@PostMapping("/updateIndexes")
	public String newAlbum(@ModelAttribute PictureInfoForm picInfoForm, Model model) {
		System.out.println(model);
		System.out.println(picInfoForm);

		return "editindexes";
	}
}
