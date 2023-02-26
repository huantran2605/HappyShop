package com.happyshop.admin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AmazonS3UtilTest {

    @Test
    public void testListFolder() {
        String folderName = "site-logo";
        List<String> listKeys = AmazonS3Util.listFolder(folderName);
        listKeys.forEach(System.out::println);
    }
    
    @Test
    public void testUploadFile() throws FileNotFoundException {
        String folderName = "test-upload";
        String fileName = "testPic.jpg";
        String filePath = "E:\\" + fileName;
        
        InputStream inputStream = new FileInputStream(filePath);
        
        AmazonS3Util.uploadFile(folderName, fileName, inputStream);
    }
    
    @Test
    public void testDeleteFile() {
        String fileName = "test-upload/testPic.jpg";
        AmazonS3Util.deleteFile(fileName);
    }
//    
    @Test
    public void testRemoveFolder() {
        String folderName = "test-upload";
        AmazonS3Util.removeFolder(folderName);
    }
}
