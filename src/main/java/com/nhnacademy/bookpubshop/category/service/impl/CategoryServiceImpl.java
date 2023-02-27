package com.nhnacademy.bookpubshop.category.service.impl;

import com.nhnacademy.bookpubshop.category.dto.request.CreateCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryInfoResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetParentCategoryWithChildrenResponseDto;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.bookpubshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.category.service.CategoryService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Integer parentCategoryNo = createCategoryRequestDto.getParentCategoryNo();
        String categoryName = createCategoryRequestDto.getCategoryName();

        Category parentCategory = tryGetParentCategory(parentCategoryNo);

        if (Objects.isNull(parentCategory)) {
            checkCategoryNameIsDuplicated(categoryName);
        } else {
            checkChildCategoryNameIsDuplicated(parentCategoryNo, categoryName);
        }

        categoryRepository.save(new Category(null, parentCategory,
                categoryName,
                createCategoryRequestDto.getCategoryPriority(),
                createCategoryRequestDto.isCategoryDisplayed()));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCategory(ModifyCategoryRequestDto modifyCategoryRequestDto) {
        String modifyCategoryName = modifyCategoryRequestDto.getCategoryName();
        Integer parentCategoryNo = modifyCategoryRequestDto.getParentCategoryNo();

        Category category = categoryRepository.findById(modifyCategoryRequestDto.getCategoryNo())
                .orElseThrow(CategoryNotFoundException::new);

        Category parentCategory = tryGetParentCategory(
                parentCategoryNo);

        if (!category.getCategoryName().equals(modifyCategoryName)) {
            if (Objects.isNull(parentCategoryNo)) {
                checkCategoryNameIsDuplicated(modifyCategoryName);
            } else {
                checkChildCategoryNameIsDuplicated(parentCategoryNo, modifyCategoryName);
            }
        }

        category.modifyCategory(modifyCategoryName,
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
        Category category = categoryRepository.findById(categoryNo)
                .orElseThrow(CategoryNotFoundException::new);

        return new GetCategoryResponseDto(category.getCategoryNo(), category.getCategoryName(),
                null, category.getCategoryPriority(), category.isCategoryDisplayed());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetCategoryResponseDto> getCategories(Pageable pageable) {
        return categoryRepository.findCategories(pageable);
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
     * {@inheritDoc}
     */
    @Override
    public List<GetParentCategoryWithChildrenResponseDto> getParentCategoryWithChildren() {
        return categoryRepository.findParentCategoryWithChildren();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetCategoryInfoResponseDto> getAllCategories() {
        return categoryRepository.findCategoriesInfo();
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


    /**
     * 상위 카테고리별 중복 확인.
     *
     * @param parentCategoryNo 부모 카테고리 번호.
     * @param categoryName     카테고리 이름
     * @throws CategoryAlreadyExistsException 카테고리명 중복 시 예외 발생.
     */
    private void checkChildCategoryNameIsDuplicated(Integer parentCategoryNo, String categoryName) {
        List<String> childList = categoryRepository.findChildNameByParentNo(
                parentCategoryNo);

        for (String name : childList) {
            if (name.equals(categoryName)) {
                throw new CategoryAlreadyExistsException(categoryName);
            }
        }

    }
}
