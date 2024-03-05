package br.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.config.FileStorageConfig;
import br.exceptions.FileStorageException;
import br.exceptions.MyFileNDException;

@Service
public class FileStorageService {
    
    private final Path fileStorageLocation;

	public FileStorageService(FileStorageConfig fileStorageConfig) {
		Path path = Paths.get(fileStorageConfig.getUploadDir())
			.toAbsolutePath().normalize();
		
		this.fileStorageLocation = path;
		
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException(
				"Could not create the directory where the uploaded files will be stored!", e);
		}
	}
	
	@SuppressWarnings("null")
    public String storeFile(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			// Filename..txt
			if (filename.contains("..")) {
				throw new FileStorageException(
					"Sorry! Filename contains invalid path sequence " + filename);
			}
			Path targetLocation = this.fileStorageLocation.resolve(filename);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return filename;
		} catch (Exception e) {
			throw new FileStorageException(
				"Could not store file " + filename + ". Please try again!", e);
		}
	}

	@SuppressWarnings("null")
	public Resource loadFileAResource(String fileName){
		try {
			Path filaPath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filaPath.toUri());

			if(resource.exists()) return resource;
			else{
				throw new MyFileNDException("File not found!");
			}
		} catch (Exception e) {
			throw new MyFileNDException("File not found!" + fileName, e);
		}
	}
}
