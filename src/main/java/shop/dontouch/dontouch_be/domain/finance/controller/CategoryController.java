package shop.dontouch.dontouch_be.domain.finance.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDocs {

  private final CategoryService categoryService;

  @LogMonitoring
  @PostMapping("/me")
  public ResponseEntity<CategoryResponse> createMyCategory(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody CategoryRequest request
  ) {
    CategoryResponse response = categoryService.createCustomCategory(currentUser.getUser(), request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @LogMonitoring
  @GetMapping("/me")
  public ResponseEntity<List<CategoryResponse>> getMyCategories(
      @AuthenticationPrincipal CustomUserDetails currentUser
  ) {
    return ResponseEntity.ok(categoryService.getCategoriesForUser(currentUser.getUserId()));
  }

  @LogMonitoring
  @DeleteMapping("/me/{category-id}")
  public ResponseEntity<Void> deleteMyCategory(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "category-id") UUID categoryId
  ) {
    categoryService.deleteCustomCategory(currentUser.getUserId(), categoryId);
    return ResponseEntity.noContent().build();
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping
  public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
    CategoryResponse response = categoryService.createGlobalCategory(request);
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
  public ResponseEntity<List<CategoryResponse>> getAllGlobalCategories() {
    List<CategoryResponse> responses = categoryService.getAllGlobalCategories();
    return ResponseEntity.ok(responses);
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/users/{user-id}")
  public ResponseEntity<List<CategoryResponse>> getCustomCategoriesByUserId(
      @PathVariable(name = "user-id") UUID userId) {
    return ResponseEntity.ok(categoryService.getCustomCategoriesByUserId(userId));
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/all")
  public ResponseEntity<List<CategoryResponse>> getAllCategories() {
    return ResponseEntity.ok(categoryService.getAllCategories());
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @PatchMapping("/{category-id}")
  public ResponseEntity<CategoryResponse> updateCategory(
      @PathVariable(name = "category-id") UUID categoryId,
      @Valid @RequestBody CategoryRequest request) {
    CategoryResponse response = categoryService.updateGlobalCategory(categoryId, request);
    return ResponseEntity.ok(response);
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/{category-id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable(name = "category-id") UUID categoryId) {
    categoryService.deleteGlobalCategory(categoryId);
    return ResponseEntity.noContent().build();
  }
}

