package com.nhnacademy.bookpubshop.category.service;

import com.nhnacademy.bookpubshop.category.dto.request.CreateCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.bookpubshop.category.dto.response.GetCategoryResponseDto;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.bookpubshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import java.util.List;
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
        if (categoryRepository.existsByCategoryName(createCategoryRequestDto.getCategoryName())) {
            throw new CategoryAlreadyExistsException(createCategoryRequestDto.getCategoryName());
        }
        categoryRepository.save(new Category(null, createCategoryRequestDto.getCategory(),
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

        if (categoryRepository.existsByCategoryName(modifyCategoryRequestDto.getCategoryName())) {
            throw new CategoryAlreadyExistsException(modifyCategoryRequestDto.getCategoryName());
        }

        category.modifyCategory(modifyCategoryRequestDto.getCategoryName(),
                modifyCategoryRequestDto.getParentCategory(),
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

}
