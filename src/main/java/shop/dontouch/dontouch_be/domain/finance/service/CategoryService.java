package shop.dontouch.dontouch_be.domain.finance.service;

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

}
