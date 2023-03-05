package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.request.category.CategoryRequest;
import fpt.project.bsmart.service.ICategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final ICategoryService iCategoryService;
    public CategoryController (ICategoryService iCategoryService){
        this.iCategoryService = iCategoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories(){
        return ResponseEntity.ok(ApiResponse.success(iCategoryService.getAllCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategory(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(iCategoryService.getCategory(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.ok(ApiResponse.success(iCategoryService.createCategory(categoryRequest)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.ok(ApiResponse.success(iCategoryService.updateCategory(id, categoryRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteCategory(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(iCategoryService.deleteCategory(id)));
    }
}
