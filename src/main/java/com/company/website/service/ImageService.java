package com.company.website.service;

import com.company.website.exception.CustomFormatException;
import com.company.website.model.Image;
import com.company.website.model.Project;
import com.company.website.repository.ImageRepository;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author Dmitry Matrizaev
 * @since 27.04.2020
 */
@Service
public class ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
    private static final int MAX_DIMENSION = 800;
    private static final int MAX_FILE_SIZE = (int) (1.5 * 1024 * 1024);
    private static final String GIF = "gif";
    private static final String STORAGE_PATH = "src/main/resources/images/";

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void processImageOnWrite(MultipartFile file, Project project) {
        Image image = new Image();
        image.setTitle(adviceImageName(file));
        saveToStorage(image, file, project.getTitle());
        image.setProject(project);
        imageRepository.save(image);
    }

    public static String applyDataOnRead(Image image) {
        Path path = Paths.get(STORAGE_PATH + image.getProject().getTitle() + "/" + image.getTitle());
        String output = null;
        try {
            InputStream is = Files.newInputStream(path);
            byte[] payload = IOUtils.toByteArray(is);
            output = Base64.encodeBase64String(payload);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return output;
    }

    private void saveToStorage(Image image, MultipartFile input, String projectName) {
        String fileName = image.getTitle();
        Path path = Paths.get(STORAGE_PATH + projectName + "/" + fileName);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, processImageData(input));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void removeImage(Integer imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(NoSuchElementException::new);
        String fileName = image.getTitle();
        String projectName = image.getProject().getTitle();
        Path path = Paths.get(STORAGE_PATH + projectName + "/" + fileName);
        try {
            Files.deleteIfExists(path);
            imageRepository.deleteById(imageId);
            LOGGER.info("File: {} was deleted", fileName);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private byte[] processImageData(MultipartFile file) throws IOException {
        byte[] output = null;
        String format = Objects.requireNonNull(file.getContentType()).split("/")[1];
        long fileSize = file.getSize();
        if (isImageType(file)) {
            if (fileSize > MAX_FILE_SIZE && !format.equals(GIF)) {
                output = resize(file.getBytes(), format);
            } else {
                output = file.getBytes();
            }
        } else {
            throw new CustomFormatException("Uploaded file is not an image");
        }
        return output;
    }

    private String adviceImageName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String name = originalFileName.split("\\.")[0];
        String fileExtension = originalFileName.split("\\.")[1];
        String result = originalFileName;
        if (imageRepository.existsByTitle(originalFileName)) {
            result = name.concat("1").concat(".").concat(fileExtension);
        }
        return result;
    }

    private boolean isImageType(MultipartFile file) {
        String type = Objects.requireNonNull(file.getContentType()).split("/")[0];
        return type.equals("image");
    }

    private byte[] resize(byte[] input, String format) throws IOException {
        InputStream is = new ByteArrayInputStream(input);
        BufferedImage bufferedImage = ImageIO.read(is);
        BufferedImage resizedImage = Scalr.resize(bufferedImage, MAX_DIMENSION);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, format, baos);
        return baos.toByteArray();
    }

    public List<Image> findAllByProject(Project project) {
        return imageRepository.findAllByProject(project);
    }

}
