package com.example.springs3demo.controller;

import com.example.springs3demo.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

@RestController
public class S3Contoller {


    @Autowired
    private S3Service s3Service;;
//
//    @PostMapping("/upload")
//    public String upload(@RequestParam("file") MultipartFile file){
//       return s3Service.saveFile(file);
//    }
    // upload
    @PostMapping("/upload")
	public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
		String publicURL =  s3Service.uploadFile(file);
		Map<String, String> response = new HashMap<>();
		response.put("publicURL", publicURL);
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.CREATED);
	}
   //download
    @GetMapping("download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable("filename") String filename){
        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-type", MediaType.ALL_VALUE);
        headers.add("Content-Disposition", "attachment; filename="+filename);
        byte[] bytes = s3Service.downloadFile(filename);
        return  ResponseEntity.status(HTTP_OK).headers(headers).body(bytes);
    }


    @DeleteMapping("{filename}")
    public  String deleteFile(@PathVariable("filename") String filename){
       return s3Service.deleteFile(filename);
    }

    @GetMapping("list")
    public List<String> getAllFiles(){

        return s3Service.listAllFiles();

    }
}
