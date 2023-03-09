package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.request.category.CategoryRequest;

import java.util.List;

public interface ICategoryService {
    CategoryDto getCategory(Long id);

    List<CategoryDto> getAllCategories();
    List<SubjectDto> getSubjectByCategory(Long id);

    Long createCategory(CategoryRequest categoryRequest);

    Long updateCategory(Long id, CategoryRequest categoryRequest);

    Long deleteCategory(Long id);
}
