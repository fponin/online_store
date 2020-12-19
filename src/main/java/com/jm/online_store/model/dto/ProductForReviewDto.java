package com.jm.online_store.model.dto;

import com.jm.online_store.model.Product;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductForReviewDto {

    private Long id;
    private List<ReviewDto> reviews;

    public static ProductForReviewDto productToDto(Product product) {
        ProductForReviewDto productForReviewDto = new ProductForReviewDto();
        productForReviewDto.setId(product.getId());
        productForReviewDto.setReviews(product.getReviews()
                .stream()
                .map(ReviewDto::reviewEntityToDto)
                .collect(Collectors.toList()));
        return productForReviewDto;
    }
}