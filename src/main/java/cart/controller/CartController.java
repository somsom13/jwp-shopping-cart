package cart.controller;

import cart.auth.Auth;
import cart.controller.dto.CartRequest;
import cart.controller.dto.CartResponse;
import cart.service.CartService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(final CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<Void> addProductToCart(@RequestBody @Valid CartRequest cartRequest, @Auth Long customerId) {
        long savedId = cartService.save(cartRequest.toCartDto(), customerId);
        return ResponseEntity.created(URI.create("/cart/" + savedId)).build();
    }

    @GetMapping
    public String viewCart() {
        return "cart";
    }

    @GetMapping("/products")
    public ResponseEntity<List<CartResponse>> viewAllCartOfCustomer(@Auth Long customerId) {
        List<CartResponse> cartResponses = cartService.findAllByCustomerId(customerId)
                .stream()
                .map(CartResponse::fromDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cartResponses);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCartProduct(@PathVariable long cartId) {
        cartService.deleteById(cartId);
        return ResponseEntity.noContent().build();
    }
}
