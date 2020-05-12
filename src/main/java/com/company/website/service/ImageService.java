package com.company.website.service;

import com.company.website.dto.ImageDTO;
import com.company.website.dto.ProjectDTO;
import com.company.website.exception.NotAnImageException;
import com.company.website.model.Image;
import com.company.website.model.Project;
import com.company.website.repository.ImageRepository;
import com.company.website.repository.ProjectRepository;
import com.company.website.service.mapping.ImageMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Dmitry Matrizaev
 * @since 27.04.2020
 */
@Service
@Transactional
@AllArgsConstructor
public class ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
    private static final int MAX_DIMENSION = 1600;
    private static final int MAX_FILE_SIZE = (int) (1.5 * 1024 * 1024);
    private static final String GIF = "gif";
    private static final String STORAGE_PATH = "src/main/resources/images/";

    private final ImageRepository imageRepository;
    private final ProjectRepository projectRepository;
    private final ImageMapper imageMapper;

    public void processImageOnWrite(MultipartFile file, ProjectDTO projectDTO) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setTitle(adviceImageName(file));
        String projectTitle = projectDTO.getTitle();
        saveToStorage(imageDTO, file, projectTitle);
        Project project = projectRepository.findByTitle(projectTitle);
        Image image = imageMapper.copyFromDTO(imageDTO, new Image());
        image.setProject(project);
        imageRepository.save(image);
    }

    public void processImagesOnWrite(MultipartFile[] files, ProjectDTO project) {
        if (!files[0].isEmpty()) {
            Arrays.stream(files).forEach(file -> processImageOnWrite(file, project));
        }
    }

    public List<ImageDTO> serveImagesOnRead(ProjectDTO project) {
        return findAllByProject(project).stream()
                .peek(imageDTO -> imageDTO.setData(applyDataOnRead(imageDTO, project.getTitle())))
                .collect(Collectors.toList());
    }

    public String applyDataOnRead(ImageDTO image, String projectTitle) {
        Path path = Paths.get(STORAGE_PATH + projectTitle + "/" + image.getTitle());
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

    private void saveToStorage(ImageDTO image, MultipartFile input, String projectName) {
        String fileName = image.getTitle();
        Path path = Paths.get(STORAGE_PATH + projectName + "/" + fileName);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, processImageData(input));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void removeImage(String imageTitle) {
        Image image = imageRepository.findByTitle(imageTitle);
        String fileName = image.getTitle();
        String projectName = image.getProject().getTitle();
        Path path = Paths.get(STORAGE_PATH + projectName + "/" + fileName);
        try {
            Files.deleteIfExists(path);
            imageRepository.deleteByTitle(imageTitle);
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
            throw new NotAnImageException();
        }
        return output;
    }

    private String adviceImageName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String name = originalFileName.split("\\.")[0];
        String fileExtension = originalFileName.split("\\.")[1];
        String result = originalFileName;
        if (imageRepository.existsByTitle(originalFileName)) {
            result = String.format("%s%s.%s", name, RandomStringUtils.randomAlphabetic(8), fileExtension);
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

    public List<ImageDTO> findAllByProject(ProjectDTO project) {
        return imageRepository.findAllByProjectTitle(project.getTitle()).stream()
                .map(imageMapper::map)
                .collect(Collectors.toList());
    }

}
