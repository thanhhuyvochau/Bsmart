//package fpt.project.bsmart.service.Impl;
//
//import fpt.project.bsmart.entity.Cart;
//import fpt.project.bsmart.entity.CartItem;
//import fpt.project.bsmart.entity.Course;
//import fpt.project.bsmart.entity.common.ApiException;
//import fpt.project.bsmart.entity.constant.ECourseStatus;
//import fpt.project.bsmart.entity.request.AddCartItemRequest;
//import fpt.project.bsmart.entity.request.DeleteCartItemRequest;
//import fpt.project.bsmart.entity.request.UpdateCartItemRequest;
//import fpt.project.bsmart.entity.response.CartResponse;
//import fpt.project.bsmart.repository.CartItemRepository;
//import fpt.project.bsmart.repository.CartRepository;
//import fpt.project.bsmart.repository.CourseRepository;
////import fpt.project.bsmart.repository.SubCourseRepository;
//import fpt.project.bsmart.service.ICartService;
//import fpt.project.bsmart.util.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.Objects;
//
//@Service
//@Transactional
//public class CartServiceImpl implements ICartService {
//    private final CourseRepository courseRepository;
//    private final SubCourseRepository subCourseRepository;
//
//    private final CartRepository cartRepository;
//    private final CartItemRepository cartItemRepository;
//    private final MessageUtil messageUtil;
//
//    public CartServiceImpl(CourseRepository courseRepository, SubCourseRepository subCourseRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, MessageUtil messageUtil) {
//        this.courseRepository = courseRepository;
//        this.subCourseRepository = subCourseRepository;
//        this.cartRepository = cartRepository;
//        this.cartItemRepository = cartItemRepository;
//        this.messageUtil = messageUtil;
//    }
//
//    @Override
//    public CartResponse getCarts() {
//        Cart cart = SecurityUtil.getCurrentUserCart();
//        return ConvertUtil.convertCartToCartResponse(cart);
//    }
//
//    @Override
//    public Integer addCourseToCart(AddCartItemRequest request) {
//        Cart cart = SecurityUtil.getCurrentUserCart();
//        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SUB_COURSE_NOT_FOUND_BY_ID) + request.getSubCourseId()));
//
//        Course course = subCourse.getCourse();
////        ECourseStatus status = course.getStatus();
//        if (!Objects.equals(subCourse.getStatus(), ECourseStatus.NOTSTART)) {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SUB_COURSE_NOT_FOUND_BY_ID) + request.getSubCourseId());
//        }
//        boolean anyMatch = cart.getCartItems().stream().anyMatch(cartItem -> {
//            SubCourse existingSubCourse = cartItem.getSubCourse();
//            return Objects.equals(existingSubCourse.getId(), subCourse.getId());
//        });
//        if (anyMatch) {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.DUPLICATE_SUB_COURSE_IN_CART_ITEM));
//        }
//        CartItem cartItem = new CartItem();
//        cartItem.setPrice(subCourse.getPrice());
//        cartItem.setSubCourse(subCourse);
//
//        cart.addCartItem(cartItem);
//        return cart.getTotalItem();
//    }
//
//    @Override
//    public Integer removeCourseToCart(DeleteCartItemRequest request) {
//        Cart cart = SecurityUtil.getCurrentUserCart();
//        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.CART_ITEM_NOT_FOUND_BY_ID) + request.getCartItemId()));
//        cart.removeCartItem(cartItem);
//        return cart.getCartItems().size();
//    }
//
//    @Override
//    public Integer updateCourseInCart(UpdateCartItemRequest request) {
//        Cart cart = SecurityUtil.getCurrentUserCart();
//        SubCourse newSubCourse = subCourseRepository.findById(request.getSubCourseId())
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SUB_COURSE_NOT_FOUND_BY_ID) + request.getSubCourseId()));
//        CartItem cartItem = cartItemRepository.findById(request.getCartItemId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.CART_ITEM_NOT_FOUND_BY_ID) + request.getCartItemId()));
//        SubCourse oldSubCourse = cartItem.getSubCourse();
//
//        if (!Objects.equals(newSubCourse.getId(), oldSubCourse.getId()) && Objects.equals(newSubCourse.getCourse().getId(), oldSubCourse.getCourse().getId())) {
//            cartItem.setSubCourse(newSubCourse);
//            cart.setTotalPrice(CartUtil.calculateTotalPrice(cart));
//        }
//        return cart.getTotalItem();
//    }
//}
