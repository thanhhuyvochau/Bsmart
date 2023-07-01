package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.AddCartItemRequest;
import fpt.project.bsmart.entity.request.DeleteCartItemRequest;
import fpt.project.bsmart.entity.response.CartResponse;
import fpt.project.bsmart.service.ICartService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {
//    private final ICartService cartService;

//    public CartController(ICartService cartService) {
//        this.cartService = cartService;
//    }

//    @Operation(summary = "Lấy thông tin giỏ hàng")
//    @GetMapping
//    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
//        return ResponseEntity.ok(ApiResponse.success(cartService.getCarts()));
//    }
//
//    @Operation(summary = "Thêm hàng vào giỏ")
//    @PostMapping
//    public ResponseEntity<ApiResponse<Integer>> addCourseToCart(@Valid @RequestBody AddCartItemRequest request) {
//        return ResponseEntity.ok(ApiResponse.success(cartService.addCourseToCart(request)));
//    }
    //TODO: Tạm thời sẽ không cho cập nhật giỏ hàng vì mỗi hàng trong giỏ hàng sẽ là 1 sub course riêng biệt không được trùng nhau
//    @Operation(summary = "Cập nhật hàng trong giỏ")
//    @PutMapping
//    public ResponseEntity<ApiResponse<Integer>> updateCartItem(@Valid @RequestBody UpdateCartItemRequest request) {
//        return ResponseEntity.ok(ApiResponse.success(cartService.updateCourseInCart(request)));
//    }

//    @Operation(summary = "Xóa hàng trong giỏ")
//    @DeleteMapping
//    public ResponseEntity<ApiResponse<Integer>> deleteCartItem(@RequestBody DeleteCartItemRequest request) {
//        return ResponseEntity.ok(ApiResponse.success(cartService.removeCourseToCart(request)));
//    }
}
