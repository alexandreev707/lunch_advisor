package ru.lunch.advisor.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.lunch.advisor.service.MenuService;
import ru.lunch.advisor.service.RestaurantService;
import ru.lunch.advisor.service.auth.SecurityUtil;
import ru.lunch.advisor.web.response.MenuMainView;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
public class RootController {

    private final RestaurantService restaurantService;
    private final MenuService menuService;

    public RootController(RestaurantService restaurantService, MenuService menuService) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
    }

    @GetMapping("/")
    public String root() {
        if (SecurityUtil.get().isAdmin())
            return "redirect:admin";
        return "redirect:user";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("menu", menuService.byDateWithItems(LocalDate.now())
                .stream()
                .map(MenuMainView::new)
                .collect(Collectors.toList()));
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("restaurants", restaurantService.all());
        return "menu";
    }

    @GetMapping("/users")
    public String users() {
        return "userList";
    }
}