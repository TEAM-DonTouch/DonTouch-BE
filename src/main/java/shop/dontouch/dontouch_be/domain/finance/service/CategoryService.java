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
import shop.dontouch.dontouch_be.domain.finance.repository.TransactionRepository;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;
import java.sql.SQLException;
import org.springframework.dao.DataIntegrityViolationException;
import shop.dontouch.dontouch_be.domain.user.constant.UserRole;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final TransactionRepository transactionRepository;
  private final UserRepository userRepository;

  @Transactional
  public CategoryResponse createGlobalCategory(CategoryRequest request) {
    String name = request.getCategoryName();

    if (categoryRepository.existsByNameAndUserIsNull(name)) {
      log.warn("createGlobalCategory: 이미 존재하는 이름입니다. {}", name);

      throw new CustomException(
        ErrorCode.CATEGORY_NAME_DUPLICATE
      );
    }

    Category category = Category.builder()
      .name(name)
      .build();

    Category savedCategory = saveAndFlushCategory(category, "createGlobalCategory");

    return CategoryResponse.from(savedCategory);
  }

  public CategoryResponse getCategoryByCategoryId(
    UUID categoryId,
    User currentUser
  ) {
    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> {
        log.warn(
          "getCategoryByCategoryId: 유효하지 않은 category id {}",
          categoryId
        );

        return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
      });

    if (category.isCustom()) {
      boolean isOwner = currentUser != null && category.getUser().getId().equals(currentUser.getId());
      boolean isAdmin = currentUser != null && currentUser.getRole() == UserRole.ADMIN;

      if (!isOwner && !isAdmin) {
        log.warn(
          "getCategoryByCategoryId: 커스텀 카테고리 접근 거부 userId={} categoryId={}",
          currentUser == null ? null : currentUser.getId(),
          categoryId
        );

        throw new CustomException(ErrorCode.ACCESS_DENIED);
      }
    }

    return CategoryResponse.from(category);
  }

  public List<CategoryResponse> getAllGlobalCategories() {
    return categoryRepository.findAllByUserIsNull().stream()
        .map(CategoryResponse::from)
        .toList();
  }

  @Transactional
  public CategoryResponse updateGlobalCategory(UUID categoryId, CategoryRequest request) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> {
          log.warn("updateGlobalCategory: 유효하지 않은 category id {}", categoryId);
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    if (category.isCustom()) {
      log.warn("updateGlobalCategory: 전역 카테고리가 아닙니다. categoryId {}", categoryId);
      throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    if (categoryRepository.existsByNameAndUserIsNullAndIdNot(request.getCategoryName(), categoryId)) {
      log.warn("updateGlobalCategory: 이미 존재하는 이름입니다. {}", request.getCategoryName());
      throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATE);
    }

    category.updateName(request.getCategoryName());
    Category updatedCategory = saveAndFlushCategory(category, "updateGlobalCategory");

    return CategoryResponse.from(updatedCategory);
  }

  @Transactional
  public void deleteCategory(UUID categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> {
          log.warn("deleteCategory: 유효하지 않은 category id  {}", categoryId);
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    if (transactionRepository.existsByCategoryId(categoryId)) {
      log.warn("deleteCategory: 거래에서 사용 중인 카테고리 삭제 시도 categoryId {}", categoryId);
      throw new CustomException(ErrorCode.CATEGORY_IN_USE);
    }

    category.delete();
  }

  @Transactional
  public CategoryResponse createCustomCategory(User user, CategoryRequest request) {

    String name = request.getCategoryName();

    if (categoryRepository.existsByNameAndUserIsNull(name)
        || categoryRepository.existsByNameAndUserId(name, user.getId())
    ) {
      log.warn("createCustomCategory: 이미 존재하는 이름입니다. userId {} name {}", user.getId(), name);
      throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATE);
    }

    Category category = Category.builder()
      .user(user)
      .name(name)
      .build();

    Category savedCategory = saveAndFlushCategory(category, "createCustomCategory");

    return CategoryResponse.from(savedCategory);
  }

  public List<CategoryResponse> getCategoriesForUser(UUID userId) {
    return categoryRepository.findAllVisibleToUser(userId).stream()
        .map(CategoryResponse::from)
        .toList();
  }

  @Transactional
  public void deleteCustomCategory(UUID userId, UUID categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> {
          log.warn("deleteCustomCategory: 유효하지 않은 category id {}", categoryId);
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    if (category.getUser() == null || !category.getUser().getId().equals(userId)) {
      log.warn("deleteCustomCategory: 본인 소유가 아닌 카테고리 삭제 시도 userId {} categoryId {}", userId, categoryId);
      throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    if (transactionRepository.existsByCategoryId(categoryId)) {
      log.warn("deleteCustomCategory: 거래에서 사용 중인 카테고리 삭제 시도 userId {} categoryId {}", userId, categoryId);
      throw new CustomException(ErrorCode.CATEGORY_IN_USE);
    }

    category.delete();
  }

  public List<CategoryResponse> getCategoriesByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      log.warn("getCategoriesByUserId: 유효하지 않은 userId {}", userId);
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    return categoryRepository.findAllVisibleToUser(userId).stream()
        .map(CategoryResponse::from)
        .toList();
  }

  public List<CategoryResponse> getAllCategories() {
    return categoryRepository.findAll().stream()
        .map(CategoryResponse::from)
        .toList();
  }

  private Category saveAndFlushCategory(
    Category category,
    String operation
  ) {
    try {
      return categoryRepository.saveAndFlush(category);
    } catch (DataIntegrityViolationException e) {
      if (isUniqueConstraintViolation(e)) {
        log.warn("{}: 카테고리 이름 DB 중복 name={}", operation, category.getName());
        throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATE);
      }

      throw e;
    }
  }

  private boolean isUniqueConstraintViolation(Throwable throwable) {
    Throwable cause = throwable;

    while (cause != null) {
      if (cause instanceof SQLException sqlException && "23505".equals(sqlException.getSQLState())) {
        return true;
      }

      cause = cause.getCause();
    }

    return false;
  }
}
