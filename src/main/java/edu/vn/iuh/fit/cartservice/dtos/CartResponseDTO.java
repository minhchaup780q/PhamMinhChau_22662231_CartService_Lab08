package edu.vn.iuh.fit.cartservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponseDTO {
        private String userId;
        private String productId;
        private int quantity;
}
