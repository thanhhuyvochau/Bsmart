package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Cart;
import fpt.project.bsmart.entity.CartItem;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.SubCourse;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.request.AddCartItemRequest;
import fpt.project.bsmart.entity.request.DeleteCartItemRequest;
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
        boolean anyMatch = cart.getCartItems().stream().anyMatch(cartItem -> {
            SubCourse existingSubCourse = cartItem.getSubCourse();
            return Objects.equals(existingSubCourse.getId(), subCourse.getId());
        });
        if (anyMatch) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khóa học đã tồn tại trong giỏ hàng và không thể thêm nữa");
        }
        CartItem cartItem = new CartItem();
        cartItem.setPrice(subCourse.getPrice());
        cartItem.setSubCourse(subCourse);

        cart.addCartItem(cartItem);
        return cart.getTotalItem();
    }

    @Override
    public Integer removeCourseToCart(DeleteCartItemRequest request) {
        Cart cart = SecurityUtil.getCurrentUserCart();
        CartItem cartItem = cartItemRepository.findById(request.getCartItemId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm item cần chình sửa, vui lòng thử lại!"));
        cart.removeCartItem(cartItem);
        return cart.getCartItems().size();
    }

    @Override
    public Integer updateCourseInCart(UpdateCartItemRequest request) {
        Cart cart = SecurityUtil.getCurrentUserCart();
        SubCourse newSubCourse = subCourseRepository.findById(request.getSubCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy khóa học hoặc khóa học không còn hợp lệ!"));
        CartItem cartItem = cartItemRepository.findById(request.getCartItemId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm item cần chình sửa, vui lòng thử lại!"));
        SubCourse oldSubCourse = cartItem.getSubCourse();

        if (!Objects.equals(newSubCourse.getId(), oldSubCourse.getId()) && Objects.equals(newSubCourse.getCourse().getId(), oldSubCourse.getCourse().getId())) {
            cartItem.setSubCourse(newSubCourse);
            cart.setTotalPrice(CartUtil.calculateTotalPrice(cart));
        }
        return cart.getTotalItem();
    }
}
