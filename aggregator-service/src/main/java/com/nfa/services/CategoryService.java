package com.nfa.services;

import com.nfa.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(int theId);

    Category save(Category theCategory);

    void deleteById(int theId);

    public Category getByNameOrSave(String name);
}
