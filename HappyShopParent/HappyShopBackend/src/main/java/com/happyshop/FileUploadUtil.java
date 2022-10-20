package com.happyshop;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
		public static void saveFile(MultipartFile multipartfile, String fileName,
				String fileDir) throws IOException {
			
			Path uploadPath = Paths.get(fileDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectory(uploadPath);
			}
			try (InputStream inputStream = multipartfile.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				throw new IOException("Could not save file!", e);
			}
		}
		
		public static void cleanDir(String dir) throws IOException {
			Path fileDir = Paths.get(dir);
			if (!Files.exists(fileDir)) {
				Files.createDirectory(fileDir);
			}
			Files.list(fileDir).forEach(file->{
				if(!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.getMessage();
					}
				}
			});
		
		}
}
