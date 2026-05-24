package product.productservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final List<Map<String, Object>> PRODUCTS = new ArrayList<>();

    static {
        PRODUCTS.add(Map.of("id", 1L, "name", "iPhone 15 Pro", "price", 1200));
        PRODUCTS.add(Map.of("id", 2L, "name", "MacBook Pro M3", "price", 2000));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllProducts() {
        return ResponseEntity.ok(PRODUCTS);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        boolean removed = PRODUCTS.removeIf(product -> product.get("id").equals(id));
        if (removed) {
            return ResponseEntity.ok("Xóa sản phẩm thành công với id: " + id);
        }
        return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm cần xóa.");
    }

}
