package com.example.springs3demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

@Service
public class S3Service implements FileServiceImpl{

    @Value("${bucketName}")
    private String bucketName;

    private  final AmazonS3 s3;

    @Autowired
    private AmazonS3Client awsS3Client;
    
    
    public S3Service(AmazonS3 s3) {
        this.s3 = s3;
    }

//    @Override
//    public String saveFile(MultipartFile file) {
//        String originalFilename = file.getOriginalFilename();
//        try {
//            File file1 = convertMultiPartToFile(file);
//            PutObjectResult putObjectResult = s3.putObject(bucketName, originalFilename, file1);
//            return putObjectResult.getContentMd5();
//        } catch (IOException e) {
//            throw  new RuntimeException(e);
//        }
//
//    }
    
    

	@Override
	public String uploadFile(MultipartFile file) {
		
		String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
		
		String key = UUID.randomUUID().toString() + "." +filenameExtension;
		
		ObjectMetadata metaData = new ObjectMetadata();
		metaData.setContentLength(file.getSize());
		metaData.setContentType(file.getContentType());
		
		try {
			awsS3Client.putObject("doctorite-ai-apis-dev-serverlessdeploymentbucket-hqaepdhdfb0l", key, file.getInputStream(), metaData);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An exception occured while uploading the file");
		}
		
		awsS3Client.setObjectAcl("doctorite-ai-apis-dev-serverlessdeploymentbucket-hqaepdhdfb0l", key, CannedAccessControlList.PublicRead);
		
		return awsS3Client.getResourceUrl("doctorite-ai-apis-dev-serverlessdeploymentbucket-hqaepdhdfb0l", key);
	}
	

    @Override
    public byte[] downloadFile(String filename) {
        S3Object object = s3.getObject(bucketName, filename);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
           return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }


    }

    @Override
    public String deleteFile(String filename) {

        s3.deleteObject(bucketName,filename);
        return "File deleted";
    }

    @Override
    public List<String> listAllFiles() {

        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);
      return  listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());

    }


    private File convertMultiPartToFile(MultipartFile file ) throws IOException
    {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }
}
