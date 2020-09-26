package com.example.servingwebcontent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

	@GetMapping("/image")
	public String image(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		System.out.println(name);
		return "image";
	}

	
    @RequestMapping(value = "/sid", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@RequestParam(name="name", required=false, defaultValue="e16_e17") String name) throws IOException {

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
	
	@GetMapping(value = "/videosrc", produces = "video/mp4")
	@ResponseBody
	public FileSystemResource videoSource(@RequestParam(value="id", required=true) int id) {
		System.out.println("id: " + id);
		if (id == 4)  {
			return new FileSystemResource(new File("/Users/inger/gitRepos/gs-serving-web-content/complete/src/main/resources/2020-08-10 16.30.28.mp4"));
			
		} else {
			return new FileSystemResource(new File("/Users/inger/gitRepos/gs-serving-web-content/complete/src/main/resources/2020-08-11 08.45.02.mov"));
				
		}
	}
	
	
}
