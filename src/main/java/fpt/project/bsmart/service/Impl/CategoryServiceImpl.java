package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.request.category.CategoryRequest;
import fpt.project.bsmart.repository.CategoryRepository;
import fpt.project.bsmart.service.ICategoryService;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.CATEGORY_NOT_FOUND_BY_ID;
import static fpt.project.bsmart.util.ConvertUtil.convertCategoryToCategoryDto;
import static fpt.project.bsmart.util.MentorUtil.checkIsMentor;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final MessageUtil messageUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MessageUtil messageUtil) {
        this.categoryRepository = categoryRepository;
        this.messageUtil = messageUtil;
    }

    private Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + id));
    }

    @Override
    public CategoryDto getCategory(Long id) {
        Category category = findById(id);
        return convertCategoryToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryDtoList.add(convertCategoryToCategoryDto(category));
        }
        return categoryDtoList;
    }

    @Override
    public Long createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setCode(categoryRequest.getCode());
        category.setName(categoryRequest.getName());
        return categoryRepository.save(category).getId();
    }

    @Override
    public Long updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = findById(id);
        category.setCode(categoryRequest.getCode());
        category.setName(categoryRequest.getName());
        return categoryRepository.save(category).getId();
    }

    @Override
    public Long deleteCategory(Long id) {
        Category category = findById(id);
        category.remove();
        return id;
    }

    @Override
    public List<CategoryDto> getCategoryByMentorSkill() {
        User user = checkIsMentor();

        MentorProfile mentorProfile = user.getMentorProfile();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        List<Long> idCategoryDup = new ArrayList<>();

        List<MentorSkill> skills = mentorProfile.getSkills();
        List<Subject> skillList = skills.stream().map(MentorSkill::getSkill).collect(Collectors.toList());

        for (Subject subject : skillList) {
            Set<Category> categories = subject.getCategories();
            for (Category category : categories) {
                CategoryDto categoryDto = convertCategoryToCategoryDto(category);
                if (!idCategoryDup.contains(categoryDto.getId())) {
                    idCategoryDup.add(categoryDto.getId());
                    categoryDtoList.add(categoryDto);
                }
            }
        }
        return categoryDtoList;
    }
}
