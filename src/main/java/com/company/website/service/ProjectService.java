package com.company.website.service;

import com.company.website.model.Project;
import com.company.website.model.Subgroup;
import com.company.website.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
public class ProjectService {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public Iterable<Project> findAllBySubgroup(Subgroup subgroup) {
        return repository.findAllBySubgroup(subgroup);
    }

    public Project save(Project project) {
        return repository.save(project);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Project findByUrl(String url) {
        return repository.findByUrl(url);
    }

    public Optional<Project> findById(Integer id) {
        return repository.findById(id);
    }

}
