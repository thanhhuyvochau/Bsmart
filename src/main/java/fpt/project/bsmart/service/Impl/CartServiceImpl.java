package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Cart;
import fpt.project.bsmart.entity.CartItem;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseClassStatus;
import fpt.project.bsmart.entity.request.AddCartItemRequest;
import fpt.project.bsmart.entity.request.DeleteCartItemRequest;
import fpt.project.bsmart.entity.response.CartResponse;
import fpt.project.bsmart.repository.CartItemRepository;
import fpt.project.bsmart.repository.CartRepository;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.service.ICartService;
import fpt.project.bsmart.util.Constants;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
public class CartServiceImpl implements ICartService {
    private final CourseRepository courseRepository;
    private final ClassRepository classRepository;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MessageUtil messageUtil;

    public CartServiceImpl(CourseRepository courseRepository, ClassRepository classRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, MessageUtil messageUtil) {
        this.courseRepository = courseRepository;
        this.classRepository = classRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.messageUtil = messageUtil;
    }

    @Override
    public CartResponse getCarts() {
        Cart cart = SecurityUtil.getCurrentUserCart();
        return ConvertUtil.convertCartToCartResponse(cart);
    }

    @Override
    public Integer addCourseToCart(AddCartItemRequest request) {
        Cart cart = SecurityUtil.getCurrentUserCart();
        Class clazz = classRepository.findById(request.getClassId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SUB_COURSE_NOT_FOUND_BY_ID) + request.getClassId()));

        Course course = clazz.getCourse();
        if (!Objects.equals(clazz.getStatus(), ECourseClassStatus.NOTSTART)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SUB_COURSE_NOT_FOUND_BY_ID) + request.getClassId());
        }
        boolean anyMatch = cart.getCartItems().stream().anyMatch(cartItem -> {
            Class existingSubCourse = cartItem.getClazz();
            return Objects.equals(existingSubCourse.getId(), clazz.getId());
        });
        if (anyMatch) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.DUPLICATE_SUB_COURSE_IN_CART_ITEM));
        }
        CartItem cartItem = new CartItem();
        cartItem.setPrice(clazz.getPrice());
        cartItem.setClazz(clazz);

        cart.addCartItem(cartItem);
        return cart.getTotalItem();
    }

    @Override
    public Integer removeCourseToCart(DeleteCartItemRequest request) {
        Cart cart = SecurityUtil.getCurrentUserCart();
        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.CART_ITEM_NOT_FOUND_BY_ID) + request.getCartItemId()));
        cart.removeCartItem(cartItem);
        return cart.getCartItems().size();
    }
}
