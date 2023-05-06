package cart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.controller.dto.CartResponse;
import cart.service.dto.CartDto;
import cart.service.dto.CartInfoDto;
import cart.service.exception.DuplicateCartException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    private CartService cartService;

    private long productId = 1L;
    private long customerId = 1L;

    @DisplayName("장바구니 상품을 저장하고 조회할 수 있다.")
    @Test
    @Sql("/cart_initialize.sql")
    void saveAndFindCartItems() {
        // given
        long cartId = cartService.save(new CartDto(productId), customerId);

        // when
        List<CartInfoDto> cartItems = cartService.findAllByCustomerId(customerId);

        // then
        List<CartResponse> expectedItems = List.of(new CartResponse(cartId, "baron", "tempUrl", 2000));
        assertThat(cartItems).usingRecursiveComparison()
                .isEqualTo(expectedItems);
    }

    @DisplayName("장바구니에 있는 상품을 추가하면 중복 예외가 발생한다.")
    @Test
    @Sql("/cart_initialize.sql")
    void exceptionWhenDuplicateProductInCart() {
        // given
        long cartId = cartService.save(new CartDto(productId), customerId);

        // when, then
        assertThatThrownBy(() -> cartService.save(new CartDto(productId), customerId))
                .isInstanceOf(DuplicateCartException.class);
    }

    @DisplayName("장바구니 상품을 삭제할 수 있다.")
    @Test
    @Sql("/cart_initialize.sql")
    void deleteCartItem() {
        // given
        long cartId = cartService.save(new CartDto(productId), customerId);

        // when
        cartService.deleteById(cartId);

        // then
        assertThat(cartService.findAllByCustomerId(customerId)).isEmpty();
    }

}