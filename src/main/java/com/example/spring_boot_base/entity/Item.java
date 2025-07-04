package com.example.spring_boot_base.entity;

import com.example.spring_boot_base.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="t_item")
@Getter
@Setter
@ToString
public class Item {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;
    @Column(nullable = false, length = 50)
    private String itemName;    //상품명
    @Column(name="price", nullable = false)
    private int price;          //가격
    @Column(nullable = false)
    private int stockNumber;    //재고수량
    @Lob
    @Column(nullable = false)
    private String itemDetail;  //상품상세설명
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //상품판매상태

    private LocalDateTime regTime;          //등록시간
    private LocalDateTime updateTime;       //수정시간
}