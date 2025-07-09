package com.example.spring_boot_base.service;

import com.example.spring_boot_base.constant.ItemSellStatus;
import com.example.spring_boot_base.dto.ItemFormDto;
import com.example.spring_boot_base.entity.Item;
import com.example.spring_boot_base.entity.ItemImg;
import com.example.spring_boot_base.repository.ItemImgRepository;
import com.example.spring_boot_base.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {

    @Autowired ItemService       itemService;
    @Autowired ItemRepository    itemRepository;
    @Autowired ItemImgRepository itemImgRepository;


    private List<MultipartFile> createMultipartFiles() {
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String path      = "/shop/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile mf =
                    new MockMultipartFile("itemImgFile",
                            path + imageName,
                            "image/jpg",
                            new byte[]{1, 2, 3, 4});
            multipartFileList.add(mf);
        }
        return multipartFileList;
    }

    /* ---------- 상품 등록 로직 테스트 ---------- */
    @Test
    @DisplayName("상품 등록 테스트")
    //@WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {

        // given
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemName("테스트상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품 입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();

        // when
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);

        // then
        List<ItemImg> itemImgList =
                itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        assertThat(itemFormDto.getItemName()).isEqualTo(item.getItemName());
        assertThat(itemFormDto.getItemSellStatus()).isEqualTo(item.getItemSellStatus());
        assertThat(itemFormDto.getItemDetail()).isEqualTo(item.getItemDetail());
        assertThat(itemFormDto.getPrice()).isEqualTo(item.getPrice());
        assertThat(itemFormDto.getStockNumber()).isEqualTo(item.getStockNumber());
        assertThat(multipartFileList.get(0).getOriginalFilename())
                .isEqualTo(itemImgList.get(0).getOriImgName());
    }
}
