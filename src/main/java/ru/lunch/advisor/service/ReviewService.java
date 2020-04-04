package ru.lunch.advisor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.model.ReviewModel;
import ru.lunch.advisor.persistence.model.UserModel;
import ru.lunch.advisor.persistence.query.ReviewQuery;
import ru.lunch.advisor.persistence.query.repository.MenuRepository;
import ru.lunch.advisor.persistence.query.repository.RestaurantRepository;
import ru.lunch.advisor.persistence.query.repository.ReviewRepository;
import ru.lunch.advisor.persistence.query.repository.UserRepository;
import ru.lunch.advisor.service.auth.SecurityUtil;
import ru.lunch.advisor.service.dto.ReviewDTO;
import ru.lunch.advisor.service.dto.ReviewUpdateDTO;
import ru.lunch.advisor.service.dto.ReviewUserDTO;
import ru.lunch.advisor.service.exeption.ApplicationException;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.service.mapper.ReviewMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.lunch.advisor.web.converter.DateTimeUtil.getEndDateTimeInclusive;
import static ru.lunch.advisor.web.converter.DateTimeUtil.getStartDateTimeInclusive;

@PropertySource("classpath:app.properties")
@Service
public class ReviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewService.class);

    @Value("${vote.time}")
    private int voteTime;

    private final ReviewRepository repository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final ReviewMapper mapper;
    private final ReviewQuery reviewQuery;
    private final RestaurantRepository restaurantRepository;

    public ReviewService(ReviewRepository repository, MenuRepository menuRepository, UserRepository userRepository,
                         ReviewMapper mapper, ReviewQuery reviewQuery,
                         RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.reviewQuery = reviewQuery;
        this.restaurantRepository = restaurantRepository;
    }

    public ReviewDTO get(Long id) {
        LOGGER.debug("Get review by id=[{}]", id);
        ReviewModel reviewModel = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
        return mapper.map(reviewModel);
    }

    public ReviewDTO getByMenuId(Long menuId) {
        LOGGER.debug("Get review by menuId=[{}]", menuId);
        UserModel user = userRepository.findById(SecurityUtil.get().getUserId())
                .orElseThrow(NotFoundException::new);
        ReviewModel reviewModel = repository.getByUserAndMenu(menuId, user.getId())
                .orElseThrow(() -> new NotFoundException(menuId));

        return mapper.map(reviewModel);
    }

    @Transactional
    public ReviewDTO create(ReviewUpdateDTO dto) {
        LOGGER.debug("Create review");

        checkVotingTime();

        UserModel user = userRepository.findById(SecurityUtil.get().getUserId()).orElseThrow(NotFoundException::new);
        MenuModel menuModel = menuRepository.findById(dto.getMenuId()).orElseThrow(NotFoundException::new);

        ReviewModel reviewModelNew = new ReviewModel();
        reviewModelNew.setUser(user);
        reviewModelNew.setMenu(menuModel);
        reviewModelNew.setDateTime(LocalDateTime.now());

        checkMenuTime(menuModel);
        updateState(reviewModelNew, dto);

        return mapper.map(repository.save(reviewModelNew));
    }

    @Transactional
    public void update(Long id, ReviewUpdateDTO dto) {
        LOGGER.debug("Update review by id=[{}]", id);

        checkVotingTime();
        ReviewModel reviewModelUpdate = repository.findById(id).orElseThrow(NotFoundException::new);
        checkMenuTime(reviewModelUpdate.getMenu());
        updateState(reviewModelUpdate, dto);

        repository.save(reviewModelUpdate);
    }

    public void remove(Long id) {
        LOGGER.debug("Remove review by id=[{}]", id);
        repository.deleteById(id);
    }

    public List<ReviewDTO> all() {
        LOGGER.debug("Get all reviews");
        return mapper.map(repository.findAll());
    }

    public List<ReviewUserDTO> getByUserId(Long id) {
        LOGGER.debug("Get by user id=[{}]", id);
        return mapper.mapToUser(repository.byUserId(id));
    }

    public void removeByUserId(Long id) {
        LOGGER.debug("Remove by user id=[{}]", id);
        repository.removeByUserId(id);
    }

    /**
     * Создание отзыва на текущую дату по идентификатору ресторана и пользователю из сессии
     *
     * @param id  идентификатор ресторана
     * @param dto данные для обновления
     */
    @Transactional
    public ReviewDTO createByRestaurant(Long id, ReviewUpdateDTO dto) {
        LOGGER.debug("Create review by restaurant id=[{}]", id);

        checkVotingTime();

        UserModel user = userRepository.findById(SecurityUtil.get().getUserId()).orElseThrow(NotFoundException::new);
        MenuModel menuModel = menuRepository.byDateAndRestaurant(LocalDate.now(), id)
                .orElseThrow(NotFoundException::new);

        ReviewModel reviewModelNew = new ReviewModel();
        reviewModelNew.setUser(user);
        reviewModelNew.setMenu(menuModel);
        reviewModelNew.setDateTime(LocalDateTime.now());

        updateState(reviewModelNew, dto);

        return mapper.map(repository.save(reviewModelNew));
    }

    /**
     * Обновление отзыва по идентификатору ресторана и пользователю из сессии
     *
     * @param id  идентификатор ресторана
     * @param dto данные для обновления
     */
    @Transactional
    public void updateByRestaurant(Long id, ReviewUpdateDTO dto) {
        LOGGER.debug("Update review by restaurant id=[{}]", id);

        checkVotingTime();

        ReviewModel reviewModelUpdate = reviewQuery.getByRestaurantId(restaurantRepository.getOne(id).getId(),
                SecurityUtil.authUserId(),
                dto.getDate() != null ? getStartDateTimeInclusive(dto.getDate()) :
                        getStartDateTimeInclusive(LocalDate.now()),
                dto.getDate() != null ? getEndDateTimeInclusive(dto.getDate()) :
                        getEndDateTimeInclusive(LocalDate.now()))
                .stream()
                .findFirst()
                .orElseThrow(NotFoundException::new);

        checkMenuTime(reviewModelUpdate.getMenu());
        updateState(reviewModelUpdate, dto);

        repository.save(reviewModelUpdate);
    }

    public List<ReviewUserDTO> getByUserId(Long id, LocalDate date) {
        LOGGER.debug("Get by user id=[{}]", id);
        return mapper.mapToUser(reviewQuery.getByUserId(id, date));
    }

    private void updateState(ReviewModel reviewModel, ReviewUpdateDTO dto) {
        List<ReviewModel> reviewModels = repository.getByUserIdAndBetween(LocalDate.now().atTime(LocalTime.MIN),
                LocalDate.now().atTime(LocalTime.MAX), SecurityUtil.get().getUserId());

        if (!CollectionUtils.isEmpty(reviewModels))
            repository.deleteVote(reviewModels.stream()
                    .map(ReviewModel::getId)
                    .collect(Collectors.toList()));

        if (dto.getVote()) {
            reviewModel.setState(State.ACTIVE);
        } else {
            reviewModel.setState(State.DELETED);
        }
    }

    /**
     * Проверка времени голосования
     */
    private void checkVotingTime() {
        if (LocalDateTime.now().getHour() >= voteTime) throw new ApplicationException("Voting time before 11");
    }

    /**
     * Проверка меню голосования
     * Нет смысла менять статус голосвания в прошлом
     *
     * @param menu меню
     */
    private void checkMenuTime(MenuModel menu) {
        if (menu.getDate().isBefore(LocalDate.now())) throw new ApplicationException("Menu in the past");
    }
}
