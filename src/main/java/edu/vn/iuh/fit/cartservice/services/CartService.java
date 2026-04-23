package edu.vn.iuh.fit.cartservice.services;

import edu.vn.iuh.fit.cartservice.dtos.CartRequestDTO;
import edu.vn.iuh.fit.cartservice.dtos.CartResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private RedisTemplate redisTemplate;

    public void deleteById(String userId, String productId ){
        redisTemplate.opsForHash().delete("cart:" + userId, productId);
    }

    public String save(CartRequestDTO request){

        Object current = redisTemplate.opsForHash().get("cart:" + request.getUserId(), request.getProductId());

        // nếu chưa có mà lại truyền số âm → reject
        if (current == null && request.getQuantity() < 0) {
            throw new RuntimeException("Không thể giảm sản phẩm chưa tồn tại");
        }

//        cộng hoặc trừ số lượng nếu đã tồn tại
        Long newQuantity = redisTemplate.opsForHash().increment("cart:" + request.getUserId(), request.getProductId(), request.getQuantity());
        System.out.println("In thử cái cart-item sẽ lưu vào Redis:");
        System.out.println("UserID: " + request.getUserId());
        System.out.println(redisTemplate.opsForHash().entries("cart:" + request.getUserId()));

//        nếu sau khi trừ mà số lượng <0 thì xóa luôn
        if (newQuantity != null && newQuantity <= 0) {
            deleteById(request.getUserId(), request.getProductId());
            return "Đã xóa sản phẩm khỏi giỏ hàng";
        }
        return "Cập nhật giỏ hàng thành công";
    }



    public List<CartResponseDTO> getAll(String userId){
        Map<Object, Object> entries = redisTemplate.opsForHash().entries("cart:" + userId);

        return entries.entrySet().stream()
                .map(entry -> {
                    CartResponseDTO dto = new CartResponseDTO();
                    dto.setUserId(userId);
                    dto.setProductId(entry.getKey().toString());
                    dto.setQuantity(Integer.parseInt(entry.getValue().toString()));
                    return dto;
                })
                .collect(Collectors.toList());

    }

    public void clear(String userId){
        redisTemplate.delete("cart:" + userId);
    }

}
