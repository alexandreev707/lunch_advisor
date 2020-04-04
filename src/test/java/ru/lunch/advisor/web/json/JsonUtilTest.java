package ru.lunch.advisor.web.json;

import org.junit.jupiter.api.Test;
import ru.lunch.advisor.web.response.ItemView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static ru.lunch.advisor.TestUtils.assertMatch;

class JsonUtilTest {

    @Test
    void readWriteValue() {
        String json = JsonUtil.writeValue(stubItemView());
        System.out.println(json);
        ItemView itemView = JsonUtil.readValue(json, ItemView.class);
        assertMatch(stubItemView(), itemView);
    }

    @Test
    void readWriteValues() {
        String json = JsonUtil.writeValue(stubItemsView());
        System.out.println(json);
        List<ItemView> itemsView = JsonUtil.readValues(json, ItemView.class);
        assertMatch(stubItemsView(), itemsView);
    }

    private ItemView stubItemView() {
        ItemView itemView = new ItemView();
        itemView.setId(50L);
        itemView.setName("Crispy Honey Mustard Chicken Salad");
        itemView.setPrice(BigDecimal.valueOf(14).setScale(2, RoundingMode.CEILING));

        return itemView;
    }

    private List<ItemView> stubItemsView() {
        ItemView itemView1 = new ItemView();
        itemView1.setId(50L);
        itemView1.setName("Crispy Honey Mustard Chicken Salad");
        itemView1.setPrice(BigDecimal.valueOf(14).setScale(2, RoundingMode.CEILING));

        ItemView itemView2 = new ItemView();
        itemView2.setId(51L);
        itemView2.setName("Soup and House Salad");
        itemView2.setPrice(BigDecimal.valueOf(5).setScale(2, RoundingMode.CEILING));

        ItemView itemView3 = new ItemView();
        itemView3.setId(52L);
        itemView3.setName("TEA");
        itemView3.setPrice(BigDecimal.valueOf(5).setScale(2, RoundingMode.CEILING));

        return Arrays.asList(itemView1, itemView2, itemView3);
    }
}