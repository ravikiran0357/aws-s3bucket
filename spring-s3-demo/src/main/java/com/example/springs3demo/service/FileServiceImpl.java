package com.example.springs3demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.Bucket;
import com.example.springs3demo.model.ContentDetails;

import java.util.List;

public interface FileServiceImpl {


	 //String saveFile(MultipartFile file);
    //String uploadFile(MultipartFile file);
    public String uploadFile(MultipartFile file,ContentDetails contentdetails);

    byte[] downloadFile(String filename);

    String deleteFile(String filename);


    
    List<String> listAllFiles();
    List<ContentDetails> listAllFile();
    
    //bucket
    public String createBucket(String buckettName);
    public String deleteBucket(String bucket_Name);
    public List<Bucket> listBuckets();
    
    //acl
    public String getBucketAcl(String bucket_name);
    public String setBucketAcl(String bucket_name, String email, String access);
    //objects acl
    public String getObjectAcl(String bucket_name, String object_key);
    public String setObjectAcl(String bucket_name, String object_key, String email, String access);
    //policy
    public String setBucketPolicy(String bucket_name, String policy_text);
    public String getBucketPolicy(String bucket_name);
    public String deleteBucketPolicy(String bucket_name);
}
