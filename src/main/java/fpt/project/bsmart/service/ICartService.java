package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.AddCartItemRequest;
import fpt.project.bsmart.entity.request.DeleteCartItemRequest;
import fpt.project.bsmart.entity.request.UpdateCartItemRequest;
import fpt.project.bsmart.entity.response.CartResponse;
import fpt.project.bsmart.entity.response.CourseCartResponse;

import java.util.List;

public interface ICartService {
    CartResponse getCarts();

    Integer addCourseToCart(AddCartItemRequest request);

    Integer removeCourseToCart(DeleteCartItemRequest request);

    Integer updateCourseInCart(UpdateCartItemRequest request);
}
