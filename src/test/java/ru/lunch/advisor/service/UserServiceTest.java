package ru.lunch.advisor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.lunch.advisor.persistence.model.Role;
import ru.lunch.advisor.persistence.model.UserModel;
import ru.lunch.advisor.persistence.query.repository.UserRepository;
import ru.lunch.advisor.service.auth.AuthUser;
import ru.lunch.advisor.service.dto.MenuDTO;
import ru.lunch.advisor.service.dto.ReviewUserDTO;
import ru.lunch.advisor.service.dto.UserDTO;
import ru.lunch.advisor.service.dto.UserMenuDTO;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.service.mapper.UserMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository repository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private UserMapper mapper;
    @Mock
    private MenuService menuService;
    @Mock
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        initMocks(this);

        AuthUser authUser = Mockito.mock(AuthUser.class);
        Mockito.when(authUser.getUserId()).thenReturn(50L);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUser);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void get() {
        UserDTO expected = stubUserDTO();
        Mockito.when(repository.findById(50L)).thenReturn(Optional.of(Mockito.mock(UserModel.class)));
        Mockito.when(mapper.map(Mockito.any(UserModel.class))).thenReturn(expected);

        UserDTO dto = userService.get(50L);
        assertEquals(expected.getName(), dto.getName());
        assertEquals(expected.getEmail(), dto.getEmail());
        assertEquals(expected.getId(), dto.getId());
        assertEquals(expected.getPassword(), dto.getPassword());
        assertEquals(expected.getRoles(), dto.getRoles());

        Mockito.verify(repository, Mockito.times(1)).findById(50L);
        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.any(UserModel.class));
    }

    @Test
    public void getException() {
        assertThrows(NotFoundException.class, () -> userService.get(50L));
    }

    @Test
    public void getByEmail() {
        Mockito.when(repository.getByEmail("test@test.ru")).thenReturn(Optional.of(Mockito.mock(UserModel.class)));
        Mockito.when(mapper.map(Mockito.any(UserModel.class))).thenReturn(stubUserDTO());

        userService.getByEmail("test@test.ru");

        Mockito.verify(repository, Mockito.times(1)).getByEmail("test@test.ru");
        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.any(UserModel.class));
    }

    @Test
    public void getByEmailException() {
        assertThrows(UsernameNotFoundException.class, () -> userService.getByEmail("test@test.ru"));
    }

    @Test
    public void create() {
        userService.create(stubUserDTO());
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(UserModel.class));
    }

    @Test
    public void update() {
        UserModel model = Mockito.mock(UserModel.class);
        Mockito.when(repository.findById(50L)).thenReturn(Optional.of(model));
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("T34s@D3/.");

        userService.update(50L, stubUserDTO());

        Mockito.verify(model, Mockito.times(1)).setEmail(Mockito.any());
        Mockito.verify(model, Mockito.times(1)).setName(Mockito.any());
        Mockito.verify(model, Mockito.times(1)).setEnabled(Mockito.anyBoolean());
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void updateException() {
        assertThrows(NotFoundException.class, () -> userService.update(50L, stubUserDTO()));
    }

    public static Object[][] removeData() {
        return new Object[][]{
                {50L, 0},
                {55L, 1},
                {null, 0}
        };
    }

    @ParameterizedTest
    @MethodSource("removeData")
    public void remove(Long id, int expected) {
        userService.remove(id);
        Mockito.verify(reviewService, Mockito.times(expected)).removeByUserId(id);
        Mockito.verify(repository, Mockito.times(expected)).deleteById(id);
    }

    @Test
    public void all() {
        userService.all();
        Mockito.verify(repository, Mockito.times(1)).findAll();
        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.anyList());
    }

    public static Object[][] getMenuByUserData() {
        return new Object[][]{
                {77L, true},
                {55L, false}
        };
    }

    @ParameterizedTest
    @MethodSource("getMenuByUserData")
    public void getMenuByUser(Long menuId, boolean expected) {
        MenuDTO dto = stubMenuDTO();
        List<MenuDTO> menus = Collections.singletonList(dto);
        ReviewUserDTO reviewUser = new ReviewUserDTO();
        reviewUser.setMenuId(menuId);
        List<ReviewUserDTO> userMenus = Collections.singletonList(reviewUser);

        Mockito.when(menuService.byDateWithoutRelations(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(menus);
        Mockito.when(reviewService.getByUserId(Mockito.anyLong(), Mockito.any(LocalDate.class)))
                .thenReturn(userMenus);

        List<UserMenuDTO> menusDTO = userService.getMenuByUser(77L, LocalDate.now(), LocalDate.now());

        assertNotNull(menusDTO);

        UserMenuDTO menuDTO = menusDTO.get(0);
        assertEquals(dto.getId(), menuDTO.getId());
        assertEquals(dto.getDate(), menuDTO.getDate());
        assertEquals(dto.getName(), menuDTO.getName());
        assertEquals(dto.getRestaurant(), menuDTO.getRestaurant());
        assertEquals(expected, menuDTO.isVote());

        Mockito.verify(menuService, Mockito.times(1))
                .byDateWithoutRelations(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class));
        Mockito.verify(reviewService, Mockito.times(1))
                .getByUserId(Mockito.anyLong(), Mockito.any(LocalDate.class));
    }

    private MenuDTO stubMenuDTO() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setDate(LocalDate.now());
        menuDTO.setId(77L);
        menuDTO.setName("Lunch");
        menuDTO.setRestaurant("Sea");

        return menuDTO;
    }

    private UserDTO stubUserDTO() {
        UserDTO model = new UserDTO();
        model.setId(50L);
        model.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        model.setPassword("Password1");
        model.setName("Petrov");
        model.setEnabled(true);
        model.setEmail("test@test.ru");

        return model;
    }
}
