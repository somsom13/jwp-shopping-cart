package cart.controller;

import cart.service.ProductService;
import cart.controller.dto.ProductRequest;
import cart.controller.dto.ProductResponse;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;

    public AdminController(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showAllProducts(Model model) {
        List<ProductResponse> allProducts = productService.findAllProducts()
                .stream()
                .map(ProductResponse::fromDto)
                .collect(Collectors.toList());
        model.addAttribute("products", allProducts);
        return "admin";
    }

    @PostMapping("/product")
    public ResponseEntity<Void> registerProduct(@RequestBody @Valid ProductRequest productRequest) {
        long savedId = productService.save(productRequest.toProductDto());
        return ResponseEntity.created(URI.create("/admin/product/" + savedId)).build();
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Void> modifyProduct(@RequestBody @Valid ProductRequest productRequest,
                                              @PathVariable long id) {
        productService.modifyById(productRequest.toProductDto(), id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable long id) {
        productService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
