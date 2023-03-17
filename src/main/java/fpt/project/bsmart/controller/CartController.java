package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.AddCartItemRequest;
import fpt.project.bsmart.entity.response.CartResponse;
import fpt.project.bsmart.service.ICartService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Lấy thông tin giỏ hàng")
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        return ResponseEntity.ok(ApiResponse.success(cartService.getCarts()));
    }

    @Operation(summary = "Thêm hàng vào giỏ")
    @PostMapping
    public ResponseEntity<ApiResponse<Integer>> addCourseToCart(@RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(cartService.addCourseToCart(request)));
    }
}
