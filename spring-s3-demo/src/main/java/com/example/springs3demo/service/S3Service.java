package com.example.springs3demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
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

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.example.springs3demo.model.ContentDetails;
import com.example.springs3demo.repo.Contentrepo;

@Service
public class S3Service implements FileServiceImpl{

	//@Value("${bucketName}")
    private String bucketName;
	
	//@Value("${accessKey}")
	private String accessKey="AKIAX5C37ZFUTAR7C2G";
	
    //@Value("${secret}")
	private String secret="5OzRu8+kHbIPQjtNcrHhUdiGST1tF0jhtHf6V4+1";

    public  final AmazonS3 s3;

    @Autowired
    private AmazonS3Client awsS3Client;

    @Autowired
    private Contentrepo crepo;

    String bucketNames = "selfhelp-bucket";


    public S3Service(AmazonS3 s3) {
        this.s3 = s3;
    }
    // creating bucket 
    AWSCredentials credentials = new BasicAWSCredentials(
//    		"AKIAX5C37ZFUTAR7C2G7", 
//    		"5OzRu8+kHbIPQjtNcrHhUdiGST1tF0jhtHf6V4+1"
    		accessKey,secret
    		);
    
    AmazonS3 s3client = AmazonS3ClientBuilder
    		  .standard()
    		  .withCredentials(new AWSStaticCredentialsProvider(credentials))
    		  .withRegion(Regions.US_EAST_2)
    		  .build();
    //create bucket
    @Override
    public String createBucket(String buckettName) {
        if (s3.doesBucketExistV2(buckettName)) {
            return "already bucket created!!!"+buckettName;
            //b = getBucket(buckettName);
        } else {
            try {
                s3.createBucket(buckettName);
                return "bucket created successfully";
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
                return e.getErrorMessage();
            }

        }	
     }
     //list of buckets
     @Override
     public List<Bucket> listBuckets() {
         List<Bucket> buckets = s3.listBuckets();
         System.out.println("Your {S3} buckets are:");
         for (Bucket b : buckets) {
            // System.out.println("* " + b.getName());
         }
         return buckets;
     }
    //delete bucket
     @Override
     public String deleteBucket(String bucket_Name) {
         System.out.println(" - removing objects from bucket");
         ObjectListing object_listing = s3.listObjects(bucket_Name);
         while (true) {
             for (Iterator<?> iterator =
                  object_listing.getObjectSummaries().iterator();
                  iterator.hasNext(); ) {
                 S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
                 s3.deleteObject(bucket_Name, summary.getKey());
             }

             // more object_listing to retrieve?
             if (object_listing.isTruncated()) {
                 object_listing = s3.listNextBatchOfObjects(object_listing);
             } else {
                 break;
             }
         }
         return "deleted successfully";
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
    public String uploadFile(MultipartFile file,ContentDetails contentdetails) {

        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        String key = UUID.randomUUID().toString() + "." +filenameExtension;

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(file.getSize());
        metaData.setContentType(file.getContentType());

        try {
            awsS3Client.putObject(bucketName, key, file.getInputStream(), metaData);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An exception occured while uploading the file");
        }

        awsS3Client.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);

        //return awsS3Client.getResourceUrl("elasticbeanstalk-us-west-2-543506286953", key);
        String url=awsS3Client.getResourceUrl(bucketName, key);
        contentdetails.setUrl(url);
        crepo.save(contentdetails);
        return "sucessed";
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

	@Override
	public List<ContentDetails> listAllFile() {
		
		return crepo.findAll();
	}


	
}
