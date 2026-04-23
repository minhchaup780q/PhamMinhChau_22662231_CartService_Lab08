package edu.vn.iuh.fit.cartservice.controllers;

import edu.vn.iuh.fit.cartservice.dtos.CartRequestDTO;
import edu.vn.iuh.fit.cartservice.dtos.CartResponseDTO;
import edu.vn.iuh.fit.cartservice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody CartRequestDTO request){
        try{
            String message = cartService.save(request);
            return ResponseEntity.ok(message);
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body("Không thể giảm sản phẩm chưa tồn tại");
        }

    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponseDTO>> getAll(@PathVariable String userId){
        List<CartResponseDTO> result = cartService.getAll(userId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> deleteByUserId(@PathVariable String userId,@PathVariable String productId){
        cartService.deleteById(userId, productId);
        return ResponseEntity.ok("Delete item success");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clear(@PathVariable String userId){
        cartService.clear(userId);
        return ResponseEntity.ok("Clear cart success");
    }
}
