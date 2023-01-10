package com.nhnacademy.bookpubshop.category.service;

import com.nhnacademy.bookpubshop.category.dto.request.CreateCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.bookpubshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CategoryService 구현체 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * {@inheritDoc}
     *
     * @throws CategoryAlreadyExistsException 카테고리가 이미 존재 했을 때 발생하는 에러입니다.
     */
    @Override
    public void addCategory(CreateCategoryRequestDto createCategoryRequestDto) {
        checkCategoryNameIsDuplicated(createCategoryRequestDto.getCategoryName());

        Category parentCategory = tryGetParentCategory(
                createCategoryRequestDto.getParentCategoryNo());

        categoryRepository.save(new Category(null, parentCategory,
                createCategoryRequestDto.getCategoryName(),
                createCategoryRequestDto.getCategoryPriority(),
                createCategoryRequestDto.isCategoryDisplayed()));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCategory(ModifyCategoryRequestDto modifyCategoryRequestDto) {

        Category category = categoryRepository.findById(modifyCategoryRequestDto.getCategoryNo())
                .orElseThrow(CategoryNotFoundException::new);

        if(!category.getCategoryName().equals(modifyCategoryRequestDto.getCategoryName())){
            checkCategoryNameIsDuplicated(modifyCategoryRequestDto.getCategoryName());
        }

        Category parentCategory = tryGetParentCategory(
                modifyCategoryRequestDto.getParentCategoryNo());

        category.modifyCategory(modifyCategoryRequestDto.getCategoryName(),
                parentCategory,
                modifyCategoryRequestDto.getCategoryPriority(),
                modifyCategoryRequestDto.isCategoryDisplayed());
    }

    /**
     * {@inheritDoc}
     *
     * @throws CategoryNotFoundException 카테고리가 없을 때 발생하는 에러입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public GetCategoryResponseDto getCategory(Integer categoryNo) {
        return categoryRepository.findCategory(categoryNo)
                .orElseThrow(CategoryNotFoundException::new);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetCategoryResponseDto> getCategories() {
        return categoryRepository.findCategories();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetCategoryResponseDto> getCategoriesDisplayedTrue() {
        return categoryRepository.findCategoriesDisplayedTrue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetCategoryResponseDto> getParentCategories() {
        return categoryRepository.findParentCategories();
    }


    /**
     * 부모카테고리 반환.
     *
     * @param parentCategoryNo 부모카테고리 번호.
     * @return 부모카테고리 반환.
     * @throws CategoryNotFoundException 존재하지않은 부모카테고리번호로 조회시 예외 발생.
     */
    private Category tryGetParentCategory(Integer parentCategoryNo) {
        if (Objects.isNull(parentCategoryNo)) {
            return null;
        }
        return categoryRepository.findById(parentCategoryNo)
                .orElseThrow(CategoryNotFoundException::new);
    }

    /**
     * 카테고리명 중복 확인.
     *
     * @param categoryName 카테고리명.
     * @throws CategoryAlreadyExistsException 카테고리명 중복 시 예외 발생.
     */
    private void checkCategoryNameIsDuplicated(String categoryName) {
        if (categoryRepository.existsByCategoryName(categoryName)) {
            throw new CategoryAlreadyExistsException(categoryName);
        }
    }
}
