package ru.lunch.advisor.service;

import org.junit.jupiter.api.Assertions;
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
import ru.lunch.advisor.TestUtils;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.model.ReviewModel;
import ru.lunch.advisor.persistence.model.UserModel;
import ru.lunch.advisor.persistence.query.ReviewQuery;
import ru.lunch.advisor.persistence.query.repository.MenuRepository;
import ru.lunch.advisor.persistence.query.repository.ReviewRepository;
import ru.lunch.advisor.persistence.query.repository.UserRepository;
import ru.lunch.advisor.service.auth.AuthUser;
import ru.lunch.advisor.service.dto.ReviewDTO;
import ru.lunch.advisor.service.dto.ReviewUpdateDTO;
import ru.lunch.advisor.service.dto.ReviewUserDTO;
import ru.lunch.advisor.service.exeption.ApplicationException;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.service.mapper.ReviewMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository repository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewMapper mapper;
    @Mock
    private ReviewQuery reviewQuery;

    @BeforeEach
    void setUp() {
        initMocks(this);

        AuthUser authUser = Mockito.mock(AuthUser.class);
        Mockito.when(authUser.getUserId()).thenReturn(55L);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUser);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        TestUtils.injectField(reviewService, "voteTime", 25);
    }

    @Test
    public void get() {
        ReviewDTO expected = stubReviewDTO();
        Mockito.when(repository.findById(50L)).thenReturn(Optional.of(Mockito.mock(ReviewModel.class)));
        Mockito.when(mapper.map(Mockito.any(ReviewModel.class))).thenReturn(expected);

        ReviewDTO dto = reviewService.get(50L);

        Assertions.assertEquals(expected.getState(), dto.getState());
        assertEquals(expected.getDateTime(), dto.getDateTime());
        assertEquals(expected.getId(), dto.getId());
        assertEquals(expected.getMenuId(), dto.getMenuId());
        assertEquals(expected.getUserId(), dto.getUserId());

        Mockito.verify(repository, Mockito.times(1)).findById(50L);
        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.any(ReviewModel.class));
    }

    @Test
    public void getException() {
        assertThrows(NotFoundException.class, () -> reviewService.get(50L));
    }

    @Test
    public void getByMenuId() {
        UserModel userModel = Mockito.mock(UserModel.class);
        Mockito.when(userModel.getId()).thenReturn(55L);
        Mockito.when(userRepository.findById(55L)).thenReturn(Optional.of(userModel));
        Mockito.when(repository.getByUserAndMenu(50L, 55L))
                .thenReturn(Optional.of(Mockito.mock(ReviewModel.class)));
        Mockito.when(mapper.map(Mockito.any(ReviewModel.class))).thenReturn(new ReviewDTO());

        reviewService.getByMenuId(50L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(55L);
        Mockito.verify(repository, Mockito.times(1)).getByUserAndMenu(50L, 55L);
        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.any(ReviewModel.class));
    }

    @Test
    public void create() {
        UserModel userModel = Mockito.mock(UserModel.class);
        MenuModel menuModel = Mockito.mock(MenuModel.class);
        Mockito.when(menuModel.getDate()).thenReturn(LocalDate.now());
        ReviewModel reviewModel = Mockito.mock(ReviewModel.class);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userModel));
        Mockito.when(menuRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(menuModel));
        Mockito.when(repository.getByUserIdAndBetween(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class),
                Mockito.anyLong())).thenReturn(Collections.singletonList(reviewModel));

        reviewService.create(stubReviewUpdateDTO());

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(menuRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(repository, Mockito.times(1)).deleteVote(Mockito.any());
        Mockito.verify(repository).getByUserIdAndBetween(Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class), Mockito.anyLong());
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(ReviewModel.class));
    }

    public static Object[][] createExceptionData() {
        return new Object[][]{
                {Optional.of(Mockito.mock(UserModel.class)), Optional.empty(), 25},
                {Optional.empty(), Optional.empty(), 25},
                {Optional.empty(), Optional.empty(), 0}
        };
    }

    @ParameterizedTest
    @MethodSource("createExceptionData")
    public void createException(Optional<UserModel> user, Optional<MenuModel> menu, int value) {
        TestUtils.injectField(reviewService, "voteTime", value);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(user);
        Mockito.when(menuRepository.findById(Mockito.anyLong())).thenReturn(menu);

        if (value == 0) {
            assertThrows(ApplicationException.class, () -> reviewService.create(stubReviewUpdateDTO()));
        } else {
            assertThrows(NotFoundException.class, () -> reviewService.create(stubReviewUpdateDTO()));
        }
    }

    @Test
    public void update() {
        ReviewModel reviewModel = Mockito.mock(ReviewModel.class);
        MenuModel menuModel = Mockito.mock(MenuModel.class);
        Mockito.when(menuModel.getDate()).thenReturn(LocalDate.now());
        Mockito.when(reviewModel.getMenu()).thenReturn(menuModel);
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(reviewModel));
        Mockito.when(repository.getByUserIdAndBetween(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class),
                Mockito.anyLong())).thenReturn(Collections.singletonList(reviewModel));

        reviewService.update(55L, stubReviewUpdateDTO());

        Mockito.verify(repository, Mockito.times(1)).findById(55L);
        Mockito.verify(repository, Mockito.times(1)).getByUserIdAndBetween(
                Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.anyLong());
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(ReviewModel.class));
    }

    @Test
    public void updateException() {
        assertThrows(NotFoundException.class, () -> reviewService.update(55L, stubReviewUpdateDTO()));
    }

    @Test
    public void remove() {
        reviewService.remove(50L);
        Mockito.verify(repository, Mockito.times(1)).deleteById(50L);
    }

    @Test
    public void all() {
        reviewService.all();
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    public void getByUserId1() {
        Mockito.when(repository.byUserId(50L)).thenReturn(Collections.singletonList(Mockito.mock(ReviewModel.class)));
        reviewService.getByUserId(50L);

        Mockito.verify(repository, Mockito.times(1)).byUserId(50L);
        Mockito.verify(mapper, Mockito.times(1)).mapToUser(Mockito.anyList());
    }

    @Test
    public void getByUserId2() {
        ReviewUserDTO expected = stubReviewUserDTO();
        Mockito.when(reviewQuery.getByUserId(80L, LocalDate.now()))
                .thenReturn(Collections.singletonList(Mockito.mock(ReviewModel.class)));
        Mockito.when(mapper.mapToUser(Mockito.anyList())).thenReturn(Collections.singletonList(expected));

        List<ReviewUserDTO> reviews = reviewService.getByUserId(80L, LocalDate.now());

        assertEquals(1, reviews.size());
        ReviewUserDTO reviewUserDTO = reviews.get(0);
        assertEquals(expected.getDateTime(), reviewUserDTO.getDateTime());
        Assertions.assertEquals(expected.getState(), reviewUserDTO.getState());
        assertEquals(expected.getId(), reviewUserDTO.getId());
        assertEquals(expected.getMenu(), reviewUserDTO.getMenu());
        assertEquals(expected.getMenuId(), reviewUserDTO.getMenuId());

        Mockito.verify(reviewQuery, Mockito.times(1)).getByUserId(80L, LocalDate.now());
        Mockito.verify(mapper, Mockito.times(1)).mapToUser(Mockito.anyList());
    }

    @Test
    public void removeByUserId() {
        repository.removeByUserId(80L);
        Mockito.verify(repository, Mockito.times(1)).removeByUserId(80L);
    }

    private ReviewDTO stubReviewDTO() {
        ReviewDTO dto = new ReviewDTO();
        dto.setDateTime(LocalDateTime.now());
        dto.setId(77L);
        dto.setMenuId(55L);
        dto.setState(State.ACTIVE);
        dto.setUserId(56L);

        return dto;
    }

    private ReviewUserDTO stubReviewUserDTO() {
        ReviewUserDTO dto = new ReviewUserDTO();
        dto.setDateTime(LocalDateTime.now().plusDays(1));
        dto.setId(44L);
        dto.setMenu("Menu test");
        dto.setMenuId(17L);
        dto.setState(State.ACTIVE);

        return dto;
    }

    private ReviewUpdateDTO stubReviewUpdateDTO() {
        ReviewUpdateDTO dto = new ReviewUpdateDTO();
        dto.setMenuId(50L);
        dto.setVote(true);
        dto.setDate(LocalDate.now());

        return dto;
    }
}
