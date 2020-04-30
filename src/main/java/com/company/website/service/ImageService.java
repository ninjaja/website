package com.company.website.service;

import com.company.website.exception.CustomException;
import com.company.website.model.Image;
import com.company.website.model.Project;
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


    public Image processImage(Image image, MultipartFile file, Project project) {
        image.setTitle(adviceImageName(image, file));
        saveToStorage(image, file, project.getTitle());
        image.setProject(project);
        return image;
    }

    public static String applyDataToImage(Image image) {
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
            throw new CustomException("Uploaded file is not an image");
        }
        return output;
    }

    private String adviceImageName(Image image, MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String result;
        if (StringUtils.isEmpty(image.getTitle())) {
            result = originalFileName;
        } else {
            String fileExtension = originalFileName.split("\\.")[1];
            result = image.getTitle() + "." + fileExtension;
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

}
