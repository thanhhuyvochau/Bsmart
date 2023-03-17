package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.AddCartItemRequest;
import fpt.project.bsmart.entity.request.UpdateCartItemRequest;
import fpt.project.bsmart.entity.response.CartResponse;
import fpt.project.bsmart.entity.response.CourseCartResponse;

import java.util.List;

public interface ICartService {
    CartResponse getCarts();

    Integer addCourseToCart(AddCartItemRequest request);

    Integer removeCourseToCart(Long courseId);

    Integer updateCourseInCart(UpdateCartItemRequest request);
}
