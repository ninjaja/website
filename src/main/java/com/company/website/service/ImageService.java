package com.company.website.service;

import com.company.website.exception.CustomException;
import com.company.website.model.Image;
import com.company.website.model.Project;
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

    public Image processImage(Image image, MultipartFile file, Project project) {
        image.setData(processImageData(file));
        processImageName(image, file);
        image.setProject(project);
        return image;
    }

    private byte[] processImageData(MultipartFile file) {
        byte[] output = null;
        try {
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
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return output;
    }

    private void processImageName(Image image, MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (StringUtils.isEmpty(image.getTitle())) {
            image.setTitle(originalFileName);
        } else {
            String fileExtension = originalFileName.split("\\.")[1];
            image.setTitle(image.getTitle() + "." + fileExtension);
        }
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
