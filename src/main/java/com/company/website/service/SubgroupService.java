package com.company.website.service;

import com.company.website.model.Category;
import com.company.website.model.Subgroup;
import com.company.website.repository.SubgroupRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
public class SubgroupService {

    private final SubgroupRepository repository;

    public SubgroupService(SubgroupRepository repository) {
        this.repository = repository;
    }

    public Iterable<Subgroup> findAllByCategory(Category category) {
        return repository.findAllByCategory(category);
    }

    public Subgroup save(Subgroup subgroup) {
        return repository.save(subgroup);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Subgroup findByUrl(String url) {
        return repository.findByUrl(url);
    }

    public Optional<Subgroup> findById(Integer id) {
        return repository.findById(id);
    }
}
