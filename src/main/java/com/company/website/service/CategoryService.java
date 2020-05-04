package com.company.website.service;

import com.company.website.model.Category;
import com.company.website.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Iterable<Category> findAll() {
         return repository.findAll();
    }

    public Category save(Category category) {
        return repository.save(category);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Category findByUrl(String url) {
        return repository.findByUrl(url);
    }

    public Optional<Category> findById(Integer id) {
        return repository.findById(id);
    }
}
