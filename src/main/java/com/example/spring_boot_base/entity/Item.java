package com.example.spring_boot_base.entity;

import com.example.spring_boot_base.constant.ItemSellStatus;
import com.example.spring_boot_base.dto.ItemFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="t_item")
@Getter @Setter
@ToString
public class Item extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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

    public void updateItem(ItemFormDto itemFormDto){
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }
}