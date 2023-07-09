package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.request.category.CategoryRequest;

import java.util.List;

public interface ICategoryService {
    CategoryDto getCategory(Long id);

    List<CategoryDto> getAllCategories();

    Long createCategory(CategoryRequest categoryRequest);

    Long updateCategory(Long id, CategoryRequest categoryRequest);

    Long deleteCategory(Long id);

    List<CategoryDto> getCategoryByMentorSkill();
}
