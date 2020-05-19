package com.company.website.service;

import com.company.website.dto.CategoryDTO;
import com.company.website.dto.ImageDTO;
import com.company.website.dto.ProjectDTO;
import com.company.website.dto.SubgroupDTO;
import com.company.website.exception.NotAnImageException;
import com.company.website.model.Category;
import com.company.website.model.Image;
import com.company.website.model.Project;
import com.company.website.model.Subgroup;
import com.company.website.repository.ImageRepository;
import com.company.website.repository.ProjectRepository;
import com.company.website.service.mapping.CategoryMapper;
import com.company.website.service.mapping.ImageMapper;
import com.company.website.service.mapping.ProjectMapper;
import com.company.website.service.mapping.SubgroupMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ProjectMapper projectMapper;
    private final SubgroupMapper subgroupMapper;
    private final CategoryMapper categoryMapper;

    public void processImageOnWrite(final MultipartFile file, final ProjectDTO projectDTO) {
        final ImageDTO imageDTO = new ImageDTO();
        imageDTO.setTitle(adviceImageName(file));
        final int projectId = projectDTO.getId();
        saveToStorage(imageDTO, file, projectId);
        final Project project = projectRepository.findByTitle(projectDTO.getTitle());
        Image image = imageMapper.copyFromDTO(imageDTO, new Image());
        image.setProject(project);
        imageRepository.save(image);
    }

    public void processImagesOnWrite(final MultipartFile[] files, final ProjectDTO project) {
        if (!files[0].isEmpty()) {
            Arrays.stream(files).forEach(file -> processImageOnWrite(file, project));
        }
    }

    public List<ImageDTO> serveImagesOnRead(final ProjectDTO project) {
        return findAllByProject(project).stream()
                .peek(imageDTO -> imageDTO.setData(applyDataOnRead(imageDTO, project.getId())))
                .collect(Collectors.toList());
    }

    public Page<ImageDTO> serveImagesOnReadPaginated(final ProjectDTO project, final Pageable pageable) {
        final Page<ImageDTO> page = findAllByProjectPaginated(project, pageable);
        for (ImageDTO imageDTO : page) {
            imageDTO.setData(applyDataOnRead(imageDTO, project.getId()));
            setImageParents(imageDTO);
        }
        return page;
    }

    public void setImageParents(final ImageDTO imageDTO) {
        final Image image = imageRepository.findByTitle(imageDTO.getTitle());
        final Project project = image.getProject();
        final ProjectDTO projectDTO = projectMapper.map(project);
        setProjectParents(projectDTO, project);
        imageDTO.setProject(projectDTO);
    }

    public void setProjectParents(final ProjectDTO projectDTO, final Project project) {
        final Subgroup subgroup = project.getSubgroup();
        final SubgroupDTO subgroupDTO = subgroupMapper.map(subgroup);
        final Category category = subgroup.getCategory();
        final CategoryDTO categoryDTO = categoryMapper.map(category);
        subgroupDTO.setCategory(categoryDTO);
        projectDTO.setSubgroup(subgroupDTO);
    }

    public String applyDataOnRead(final ImageDTO image, final int projectId) {
        final Path path = Paths.get(STORAGE_PATH + projectId + "/" + image.getTitle());
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

    private void saveToStorage(final ImageDTO image, final MultipartFile input, final int projectId) {
        final String fileName = image.getTitle();
        final Path path = Paths.get(STORAGE_PATH + projectId + "/" + fileName);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, processImageData(input));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void removeImage(final String imageTitle) {
        final Image image = imageRepository.findByTitle(imageTitle);
        final String fileName = image.getTitle();
        final int projectId = image.getProject().getId();
        final Path path = Paths.get(STORAGE_PATH + projectId + "/" + fileName);
        try {
            Files.deleteIfExists(path);
            imageRepository.deleteByTitle(imageTitle);
            LOGGER.info("File: {} was deleted from project: {}", fileName, image.getProject().getTitle());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private byte[] processImageData(final MultipartFile file) throws IOException {
        byte[] output;
        final String format = Objects.requireNonNull(file.getContentType()).split("/")[1];
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

    private String adviceImageName(final MultipartFile file) {
        final String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        final String name = originalFileName.split("\\.")[0];
        final String fileExtension = originalFileName.split("\\.")[1];
        String result = originalFileName;
        if (imageRepository.existsByTitle(originalFileName)) {
            result = String.format("%s%s.%s", name, RandomStringUtils.randomAlphabetic(8), fileExtension);
        }
        return result;
    }

    private boolean isImageType(final MultipartFile file) {
        final String type = Objects.requireNonNull(file.getContentType()).split("/")[0];
        return type.equals("image");
    }

    private byte[] resize(final byte[] input, final String format) throws IOException {
        final InputStream is = new ByteArrayInputStream(input);
        final BufferedImage bufferedImage = ImageIO.read(is);
        final BufferedImage resizedImage = Scalr.resize(bufferedImage, MAX_DIMENSION);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, format, baos);
        return baos.toByteArray();
    }

    public List<ImageDTO> findAllByProject(ProjectDTO project) {
        return imageRepository.findAllByProjectTitle(project.getTitle()).stream()
                .map(imageMapper::map)
                .collect(Collectors.toList());
    }

    public Page<ImageDTO> findAllByProjectPaginated(ProjectDTO project, Pageable pageable) {
        return imageRepository.findAllByProjectTitle(project.getTitle(), pageable)
                .map(imageMapper::map);
    }

}
