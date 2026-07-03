package shop.dontouch.dontouch_be.domain.finance.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.finance.dto.request.CategoryRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.CategoryResponse;
import shop.dontouch.dontouch_be.domain.finance.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDocs {

  private final CategoryService categoryService;

  @LogMonitoring
  @PostMapping
  public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
    CategoryResponse response = categoryService.createCategory(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @LogMonitoring
  @GetMapping("/{category-id}")
  public ResponseEntity<CategoryResponse> getCategoryByCategoryId(@PathVariable(name = "category-id") UUID categoryId) {
    CategoryResponse response = categoryService.getCategoryByCategoryId(categoryId);
    return ResponseEntity.ok(response);
  }

  @LogMonitoring
  @GetMapping
  public ResponseEntity<List<CategoryResponse>> getAllCategories() {
    List<CategoryResponse> responses = categoryService.getAllCategories();
    return ResponseEntity.ok(responses);
  }

  @LogMonitoring
  @PatchMapping("/{category-id}")
  public ResponseEntity<CategoryResponse> updateCategory(
      @PathVariable(name = "category-id") UUID categoryId,
      @Valid @RequestBody CategoryRequest request) {
    CategoryResponse response = categoryService.updateCategory(categoryId, request);
    return ResponseEntity.ok(response);
  }

  @LogMonitoring
  @DeleteMapping("/{category-id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable(name = "category-id") UUID categoryId) {
    categoryService.deleteCategory(categoryId);
    return ResponseEntity.noContent().build();
  }
}

