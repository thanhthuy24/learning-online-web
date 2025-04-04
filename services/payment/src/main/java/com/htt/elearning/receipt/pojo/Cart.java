package com.htt.elearning.receipt.pojo;


import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Long id;
    private String name;
    private int quantity;
    private float discount;
    private float price;
}
