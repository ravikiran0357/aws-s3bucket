package com.example.springs3demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.springs3demo.model.ContentDetails;

import java.util.List;

public interface FileServiceImpl {


	 //String saveFile(MultipartFile file);
    //String uploadFile(MultipartFile file);
    public String uploadFile(MultipartFile file,ContentDetails contentdetails);

    byte[] downloadFile(String filename);

    String deleteFile(String filename);


    List<String> listAllFiles();
}
