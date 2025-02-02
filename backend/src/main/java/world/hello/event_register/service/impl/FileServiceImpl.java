package world.hello.event_register.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import world.hello.event_register.config.ResourceConfig;
import world.hello.event_register.exception.InvalidFileException;
import world.hello.event_register.service.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final ResourceConfig resourceConfig;

    @Autowired
    public FileServiceImpl(final ResourceConfig resourceConfig) {
        this.resourceConfig = resourceConfig;
    }


    @Override
    public MultipartFile validateFile(MultipartFile file) {
        log.info("Validating file {}", file.getOriginalFilename());

        String originalFilename = file.getOriginalFilename();
        String extension = Objects.requireNonNull(StringUtils.getFilenameExtension(originalFilename)).toLowerCase();

        log.info("File extension: {}", extension);
        // Validate file extension
        if (!resourceConfig.getAllowedExtensions().contains(extension)) {
            log.error("File extension not allowed: {}", extension);
            throw new InvalidFileException("Invalid file type. Only PNG, JPEG, JPG are allowed.");
        }

        // Validate file size
        long fileSize = file.getSize();
        if (fileSize > resourceConfig.getMaxFileSize()) {
            log.info("File is too large to be saved");
            throw new InvalidFileException("File size exceeds the maximum limit of 5MB.");
        }
        log.info("File is valid");
        return file;
    }


    @Override
    public String uploadFile(MultipartFile file) {
        log.info("Uploading file {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            log.info("File is empty");
            throw new InvalidFileException("File is empty.");
        }

        // Get the original filename and its extension
        String originalFilename = file.getOriginalFilename();
        String extension = Objects.requireNonNull(StringUtils.getFilenameExtension(originalFilename)).toLowerCase();

        // Create a filename based on the current timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        String finalFilename = timestamp + "." + extension;
        log.info("Filename to be saved: {}", finalFilename);

        try {
            // Define the path where the file will be saved
            Path filePath = resourceConfig.getDirectory().resolve(finalFilename);

            // Write the file bytes to the specified path
            Files.write(filePath, file.getBytes());

            log.info("File uploaded successfully");
            // Return the file path with the extension
            return finalFilename;
        } catch (IOException ex) {
            log.info("File upload failed", ex);
            throw new InvalidFileException("Failure to save the file.");
        }
    }

    @Override
    public void deleteFile(String filename) {
        try {
            Path filePath = resourceConfig.getDirectory().resolve(filename);
            Files.deleteIfExists(filePath);
            log.info("Successfully deleted file: {}", filename);
        } catch (IOException ex) {
            log.error("Failed to delete file: {}", filename, ex);
            throw new InvalidFileException("Failure to rollback the file.");
        }
    }


    @Override
    public Resource downloadFile(String filename) {
        try {
            Path filePath = getFilePath(filename);

            if (!Files.exists(filePath)) {
                throw new IOException("File not found");
            }

            // Check if the file exists
            if (!Files.exists(filePath)) {
                throw new IOException("File not found"); // Return null if the file does not exist
            }

            // Load the file as a Resource
            Resource fileResource = new UrlResource(filePath.toUri());

            // Check if the file is readable
            if (!fileResource.exists() || !fileResource.isReadable()) {
                throw new IOException("File is not readable.");
            }

            // Return the file resource
            return fileResource;
        } catch (IOException ex) {
            throw new InvalidFileException(ex.getMessage());
        }
    }

    private Path getFilePath(String filename) throws IOException {
        String sanitizedFilename = filename.replaceAll("[^a-zA-Z0-9._-]", "");

        // Ensure the file has a valid extension (timestamp.extension)
        String extension = Objects.requireNonNull(StringUtils.getFilenameExtension(sanitizedFilename)).toLowerCase();
        if (extension.isEmpty()) {
            throw new IOException("Invalid filename extension");
        }

        // Resolve the file path in the uploads directory
        return resourceConfig.getDirectory().resolve(sanitizedFilename);

    }
}
