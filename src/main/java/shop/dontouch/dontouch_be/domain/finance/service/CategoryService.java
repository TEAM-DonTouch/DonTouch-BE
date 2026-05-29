package shop.dontouch.dontouch_be.domain.finance.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.finance.dto.request.CategoryRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.CategoryResponse;
import shop.dontouch.dontouch_be.domain.finance.entity.Category;
import shop.dontouch.dontouch_be.domain.finance.repository.CategoryRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

  private final CategoryRepository categoryRepository;

  @Transactional
  public CategoryResponse createCategory(CategoryRequest request) {
    if (categoryRepository.existsByName(request.getCategoryName())) {
      log.warn("createCategory: 이미 존재하는 이름입니다. {}", request.getCategoryName());
      throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATE);
    }
    Category category = categoryRepository.save(
        Category.builder()
            .name(request.getCategoryName())
            .build()
    );
    return CategoryResponse.from(category);
  }

  public CategoryResponse getCategoryByCategoryId(UUID categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> {
          log.warn("getCategory: 유효하지 않은 category id {}", categoryId);
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });
    return CategoryResponse.from(category);
  }

  public List<CategoryResponse> getAllCategories() {
    return categoryRepository.findAll().stream()
        .map(CategoryResponse::from)
        .toList();
  }

  @Transactional
  public CategoryResponse updateCategory(UUID categoryId, CategoryRequest request) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> {
          log.warn("updateCategory: 유효하지 않은 category id {}", categoryId);
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    if (categoryRepository.existsByNameAndIdNot(request.getCategoryName(), categoryId)) {
      log.warn("updateCategory: 이미 존재하는 이름입니다. {}", request.getCategoryName());
      throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATE);
    }

    category.updateName(request.getCategoryName());

    return CategoryResponse.from(category);
  }

  @Transactional
  public void deleteCategory(UUID categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> {
          log.warn("deleteCategory: 유효하지 않은 category id  {}", categoryId);
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });
    category.delete();
  }

}
