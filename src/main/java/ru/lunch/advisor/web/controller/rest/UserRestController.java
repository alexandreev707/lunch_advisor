package ru.lunch.advisor.web.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.lunch.advisor.service.UserService;
import ru.lunch.advisor.service.auth.SecurityUtil;
import ru.lunch.advisor.service.dto.UserDTO;
import ru.lunch.advisor.web.mapper.UserWebMapper;
import ru.lunch.advisor.web.request.UserCreateRequest;
import ru.lunch.advisor.web.request.UserUpdateRequest;
import ru.lunch.advisor.web.response.UserMenuView;
import ru.lunch.advisor.web.response.UserView;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = UserRestController.USER_URL)
public class UserRestController {

    public static final String USER_URL = "/api/user";

    private final UserService userService;
    private final UserWebMapper userMapper;

    public UserRestController(UserService userService, UserWebMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public UserView get(@PathVariable Long id) {
        return new UserView(userService.get(id));
    }

    @GetMapping
    public List<UserView> all() {
        return userService.all()
                .stream()
                .map(UserView::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserMenuView> menu(@RequestParam @Nullable LocalDate startDate,
                                    @RequestParam @Nullable LocalDate endDate) {
        return userService.getMenuByUser(SecurityUtil.get().getUserId(), startDate, endDate)
                .stream()
                .map(UserMenuView::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/register")
    public ResponseEntity<UserView> create(@RequestBody UserCreateRequest user) {
        UserDTO created = userService.create(userMapper.map(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserView(created));
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id, @RequestBody UserUpdateRequest user) {
        userService.update(id, userMapper.map(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.remove(id);
    }
}
