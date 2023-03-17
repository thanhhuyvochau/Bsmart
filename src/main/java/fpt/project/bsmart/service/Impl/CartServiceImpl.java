package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Cart;
import fpt.project.bsmart.entity.CartItem;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.SubCourse;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.request.AddCartItemRequest;
import fpt.project.bsmart.entity.request.UpdateCartItemRequest;
import fpt.project.bsmart.entity.response.CartResponse;
import fpt.project.bsmart.repository.CartItemRepository;
import fpt.project.bsmart.repository.CartRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.repository.SubCourseRepository;
import fpt.project.bsmart.service.ICartService;
import fpt.project.bsmart.util.CartUtil;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
public class CartServiceImpl implements ICartService {
    private final CourseRepository courseRepository;
    private final SubCourseRepository subCourseRepository;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CourseRepository courseRepository, SubCourseRepository subCourseRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.courseRepository = courseRepository;
        this.subCourseRepository = subCourseRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartResponse getCarts() {
        Cart cart = SecurityUtil.getCurrentUserCart();
        return ConvertUtil.convertCartToCartResponse(cart);
    }

    @Override
    public Integer addCourseToCart(AddCartItemRequest request) {
        Cart cart = SecurityUtil.getCurrentUserCart();
        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy khóa học hoặc khóa học không còn hợp lệ!"));

        Course course = subCourse.getCourse();
        ECourseStatus status = course.getStatus();
        if (!Objects.equals(status, ECourseStatus.NOTSTART)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy khóa học hoặc khóa học không còn hợp lệ!");
        }

        CartItem cartItem = new CartItem();
        cartItem.setPrice(subCourse.getPrice());
        cartItem.setSubCourse(subCourse);

        cart.addCartItem(cartItem);
        cart.setTotalItem(cart.getCartItems().size());
        cart.setTotalPrice(CartUtil.calculateTotalPrice(cart));
        return cart.getTotalItem();
    }

    @Override
    public Integer removeCourseToCart(Long courseId) {
        return null;
    }

    @Override
    public Integer updateCourseInCart(UpdateCartItemRequest request) {
        return null;
    }
}