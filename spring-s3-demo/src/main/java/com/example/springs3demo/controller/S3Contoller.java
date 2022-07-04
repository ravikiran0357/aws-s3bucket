package com.example.springs3demo.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.example.springs3demo.model.ContentDetails;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.net.HttpURLConnection.HTTP_OK;

@RestController
public class S3Contoller {
	
	ObjectMapper objectMapper=new ObjectMapper();


	@Autowired
    private S3Service s3Service;

//
//    @PostMapping("/upload")
//    public String upload(@RequestParam("file") MultipartFile file){
//       return s3Service.saveFile(file);
//    }


	@RequestMapping(value="/upload", method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam(required=true,value="json") String jsondata,@RequestParam(required=true,value="file") MultipartFile file) throws JsonMappingException, JsonProcessingException {

		ContentDetails contentdetails=objectMapper.readValue(jsondata, ContentDetails.class);
        String publicURL =  s3Service.uploadFile(file,contentdetails);
        Map<String, String> response = new HashMap<>();
        response.put("publicURL", publicURL);
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.CREATED);
    }
   //download from bucket
	//localhost:8080/download/ravi.png
    @GetMapping("download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable("filename") String filename){
        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-type", MediaType.ALL_VALUE);
        headers.add("Content-Disposition", "attachment; filename="+filename);
        byte[] bytes = s3Service.downloadFile(filename);
        return  ResponseEntity.status(HTTP_OK).headers(headers).body(bytes);
    }

    //delete file from bucket
    @DeleteMapping("{filename}")
    public  String deleteFile(@PathVariable("filename") String filename){
       return s3Service.deleteFile(filename);
    }
    //list of files in bucket
    @GetMapping("list")
    public List<String> getAllFiles(){

        return s3Service.listAllFiles();
    }
    //list from database
    @GetMapping("listAll")
    public List<ContentDetails> getAllFile(){

        return s3Service.listAllFile();

    }
    //creating bucket
    @GetMapping("createBucket/{buckettName}")
    public String createBucket(@PathVariable("buckettName") String buckettName) {

        return s3Service.createBucket(buckettName);
    }
    //listing the buckets
    @GetMapping("/listBuckets")
    public List<Bucket> getAllBuckets(){

        return s3Service.listBuckets();
    }
    // delete the bucket
     @DeleteMapping("deleteBucket/{bucket_Name}")
     public String deleteBucket(@PathVariable("bucket_Name") String bucket_Name) {
            return s3Service.deleteBucket(bucket_Name);
      }
     
     //getAcl
     @GetMapping("getAcl/{bucket_Name}")
     public String getBucketAcl(@PathVariable("bucket_Name") String bucket_Name)
     {
    	 return s3Service.getBucketAcl(bucket_Name);
     }
     //setAcl
     @GetMapping("setAcl")
     public String setBucketAcl(@RequestParam("bucket_Name") String bucket_Name,@RequestParam("email")String email,@RequestParam("access")String access)
     {
    	 return s3Service.setBucketAcl(bucket_Name,email,access);
     }
     //getObjectAcl
     @GetMapping("getObjectAcl")
     public String getObjectAcl(@RequestParam("bucket_Name")String bucket_Name,@RequestParam("object_key") String object_key)
     {
    	 return s3Service.getObjectAcl(bucket_Name, object_key);
     }
     //setObjectAcl
     @GetMapping("setObjectAcl")
     public String setObjectAcl(@RequestParam("bucket_Name")String bucket_Name,@RequestParam("object_key") String object_key, @RequestParam("email")String email,@RequestParam("access")String access)
     {
    	 return s3Service.setObjectAcl(bucket_Name, object_key, email, access); 
     }
     //setPolicy
     @GetMapping("setBucketPolicy")
     public String setBucketPolicy(@RequestParam("bucket_Name")String bucket_Name, @RequestParam("policy_text") String policy_text)
     {
    	 return s3Service.setBucketPolicy(bucket_Name, policy_text);
     }
     //getBucketPolicy
     @GetMapping("getBucketPolicy")
     public String getBucketPolicy(@RequestParam("bucket_Name") String bucket_Name)
     {
    	 return s3Service.getBucketPolicy(bucket_Name);
     }
     //deletepolicy
     @DeleteMapping("deleteBucketPolicy")
     public String deleteBucketPolicy(@RequestParam("bucket_Name") String bucket_Name)
     {
    	 return s3Service.deleteBucketPolicy(bucket_Name);
     }
    
}
