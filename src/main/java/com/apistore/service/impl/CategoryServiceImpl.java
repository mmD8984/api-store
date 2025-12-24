package com.apistore.service.impl;

import com.apistore.model.dto.CategoryDTO;
import com.apistore.repository.CategoryRepository;
import com.apistore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new CategoryDTO(
                        c.getId(),
                        c.getName(),
                        c.getSlug()
                ))
                .toList();
    }
}

