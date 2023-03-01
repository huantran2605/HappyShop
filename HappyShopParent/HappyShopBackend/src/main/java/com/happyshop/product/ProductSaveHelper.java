package com.happyshop.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.happyshop.FileUploadUtil;
import com.happyshop.admin.AmazonS3Util;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.product.ProductImage;

public class ProductSaveHelper {
    
    static void saveUploadedNewExsitingExtraImages(MultipartFile[] exsitingExtraImageMultipartFile,
            Product savedProduct) throws IOException {
        if (exsitingExtraImageMultipartFile.length > 0) {
            String fileDir = "product-images/" + savedProduct.getId() + "/extras";
            for (MultipartFile multipartFile : exsitingExtraImageMultipartFile) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    AmazonS3Util.uploadFile(fileDir, fileName, multipartFile.getInputStream());
                }
            }
        }

    }

    static void deleteExtraImagesWeredRemovedOnForm(Product product) throws IOException {
        String extraImageDir = "product-images/" + product.getId() + "/extras";
        List<String> objectKeys = AmazonS3Util.listFolder(extraImageDir);
        
        for (String key : objectKeys) {
            int indexLastSlash = key.lastIndexOf("/");
            String extraFileName = key.substring(indexLastSlash+1, key.length());
            if(!product.containsImageName(extraFileName)) {
                AmazonS3Util.deleteFile(key);
            }           
   
        }
        
    }

    static void setExistingExtraImageName(String[] imageIDs,
            String[] imageNames, Product product) {
        
        Set<ProductImage> set = new HashSet<>();
        if(imageIDs.length > 0 && imageNames.length > 0) {
            for (int j = 0; j < imageNames.length; j++) {
                int idImage = Integer.parseInt(imageIDs[j]);      
                String nameImage = imageNames[j];
                set.add(new ProductImage(idImage, nameImage, product));
            }
        }
        product.setImages(set);     
    }

    static void setDetailsProduct(String[] detailIDs, String[] nameDetails, String[] valueDetails, Product product) {
        if(nameDetails.length > 0 && valueDetails.length > 0) {
            for (int i = 0; i < nameDetails.length; i++) {
                if(!nameDetails[i].isEmpty() && !valueDetails[i].isEmpty()) {
                    int idDetail = Integer.parseInt(detailIDs[i]);
                    if(idDetail != 0) {
                        product.addDetail(idDetail, nameDetails[i] , valueDetails[i]);   
                    }
                    else {
                        product.addDetail(nameDetails[i], valueDetails[i]);            
                    }            
                }
            }
        }
    }

    static void setNewExtraImageName(MultipartFile[] extraImageMultipartFile, Product product) {
        if (extraImageMultipartFile.length > 0) {
            for (MultipartFile multipartFile : extraImageMultipartFile) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    if(!product.containsImageName(fileName)) {
                        product.addExtraImage(fileName);          
                    }
                }
            }
        }  
    }       
    
    static void saveUploadedImages (MultipartFile mainImageMultipartFile,
            MultipartFile[] extraImageMultipartFile,
            Product savedProduct) throws IOException {
 
        if (!mainImageMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());
            String uploadDir = "product-images/" + savedProduct.getId();
            // delete old photos if have
            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, mainImageMultipartFile.getInputStream());               
        }

        if(extraImageMultipartFile.length > 0) {
            String extraDir = "product-images/" + savedProduct.getId() + "/extras";
            for (MultipartFile multipartFile : extraImageMultipartFile) {
                if(!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    
                    AmazonS3Util.uploadFile(extraDir, fileName, multipartFile.getInputStream());
                }
            }
        }
        
        
    }
}
