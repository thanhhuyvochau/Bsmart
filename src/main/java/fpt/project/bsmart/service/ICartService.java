package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.AddCartItemRequest;
import fpt.project.bsmart.entity.request.DeleteCartItemRequest;
import fpt.project.bsmart.entity.request.UpdateCartItemRequest;
import fpt.project.bsmart.entity.response.CartResponse;

public interface ICartService {
    CartResponse getCarts();

    Integer addCourseToCart(AddCartItemRequest request);

    Integer removeCourseToCart(DeleteCartItemRequest request);

//    Integer updateCourseInCart(UpdateCartItemRequest request);
}
