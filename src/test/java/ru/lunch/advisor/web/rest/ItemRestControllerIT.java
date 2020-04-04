package ru.lunch.advisor.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.lunch.advisor.TestUtils;
import ru.lunch.advisor.service.ItemService;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.web.controller.rest.ItemRestController;
import ru.lunch.advisor.web.json.JsonUtil;
import ru.lunch.advisor.web.request.ItemRequest;
import ru.lunch.advisor.web.response.ItemView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.lunch.advisor.TestUtils.*;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
@Transactional
class ItemRestControllerIT {

    private static final String REST_URL = ItemRestController.ITEM_URL + "/";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ItemService itemService;

    @BeforeEach
    void init() {
        initMocks(this);

        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(encodingFilter)
                .apply(springSecurity())
                .build();
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(stubItemView(), ItemView.class));
    }

    @Test
    void getItemsByMenuId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "menu/" + 52L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(stubItemsView(), ItemView.class));
    }

    @Rollback
    @Test
    void create() throws Exception {
        ItemRequest request = new ItemRequest("Coffee", BigDecimal.valueOf(17)
                .setScale(2, RoundingMode.CEILING));

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isCreated());

        ItemView created = readFromJson(action, ItemView.class);
        ItemView expected = new ItemView(created.getId(), "Coffee", BigDecimal.valueOf(17)
                .setScale(2, RoundingMode.CEILING));

        assertMatch(created, expected);
    }

    @Rollback
    @Test
    void update() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk());
        ItemView oldItem = readFromJson(response, ItemView.class);

        assertEquals(stubItemView(), oldItem);

        ItemRequest request = new ItemRequest("Tik-tak", BigDecimal.valueOf(10)
                .setScale(2, RoundingMode.CEILING));
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isNoContent());

        response = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk());
        ItemView updatedItem = readFromJson(response, ItemView.class);

        assertMatch(stubUpdateItem(), updatedItem);
    }

    @Rollback
    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> itemService.get(50L));
    }

    private ItemView stubUpdateItem() {
        ItemView itemView = new ItemView();
        itemView.setId(50L);
        itemView.setName("Tik-tak");
        itemView.setPrice(BigDecimal.TEN.setScale(2, RoundingMode.CEILING));

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

    private ItemView stubItemView() {
        ItemView itemView = new ItemView();
        itemView.setId(50L);
        itemView.setName("Crispy Honey Mustard Chicken Salad");
        itemView.setPrice(BigDecimal.valueOf(14).setScale(2, RoundingMode.CEILING));

        return itemView;
    }
}