package com.joomak.backend.model.product.entity;

import com.joomak.backend.model.common.BaseEntity;
import com.joomak.backend.model.product.enums.ProductState;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Product")
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", updatable = false)
    @NotNull
    private Long id;

    @NotNull
    @Column(name = "product_name")
    private String productName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "product_state")
    private ProductState productState;

    @Column(name = "product_description")
    private String productDescription;

    @NotNull
    @Column(name = "product_price")
    private Integer productPrice;

    @Column(name = "product_image_path")
    private String productImagePath;

    @NotNull
    @Column(name = "is_sale")
    private Boolean isSale;

    @NotNull
    @Column(name = "is_show")
    private Boolean isShow;

    @Column(name = "origin_place")
    private String originPlace;

    @NotNull
    @Column(name = "stock")
    private Integer stock;

    @Column(name = "min_order_quantity")
    private Integer minOrderQuantity;

    @Column(name = "max_order_quantity")
    private Integer maxOrderQuantity;

    // 확장성 ?
    @Builder
    public Product(Long id, String productName, ProductState productState,
                   String productDescription, Integer productPrice, String productImagePath,
                   Boolean isSale, Boolean isShow, String originPlace, Integer stock,
                   Integer minOrderQuantity, Integer maxOrderQuantity) {
        this.id = id;
        this.productName = productName;
        this.productState = productState;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImagePath = productImagePath;
        this.isSale = isSale;
        this.isShow = isShow;
        this.originPlace = originPlace;
        this.stock = stock;
        this.minOrderQuantity = minOrderQuantity;
        this.maxOrderQuantity = maxOrderQuantity;
    }
}