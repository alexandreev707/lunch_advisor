package ru.lunch.advisor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.lunch.advisor.persistence.model.Role;
import ru.lunch.advisor.persistence.model.UserModel;
import ru.lunch.advisor.persistence.query.repository.UserRepository;
import ru.lunch.advisor.service.auth.SecurityUtil;
import ru.lunch.advisor.service.dto.MenuDTO;
import ru.lunch.advisor.service.dto.ReviewUserDTO;
import ru.lunch.advisor.service.dto.UserDTO;
import ru.lunch.advisor.service.dto.UserMenuDTO;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.service.mapper.UserMapper;
import ru.lunch.advisor.web.converter.DateTimeUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис по работе с пользователями
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final MenuService menuService;
    private final ReviewService reviewService;

    public UserService(UserRepository repository, BCryptPasswordEncoder passwordEncoder, UserMapper mapper,
                       MenuService menuService, ReviewService reviewService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.menuService = menuService;
        this.reviewService = reviewService;
    }

    public UserDTO get(Long id) {
        LOGGER.debug("Get user by id=[{}]", id);
        UserModel model = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
        return mapper.map(model);
    }

    public UserDTO getByEmail(String email) {
        LOGGER.debug("Get user by email=[{}]", email);
        UserModel model = repository.getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " is not found"));
        return mapper.map(model);
    }

    public UserDTO create(UserDTO dto) {
        LOGGER.debug("Create user name=[{}]", dto.getName());
        UserModel user = new UserModel();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(dto.isEnabled());
        user.setRoles(getRoles(dto));

        return mapper.map(repository.save(user));
    }

    public void update(Long id, UserDTO dto) {
        LOGGER.debug("Update user by id=[{}]", id);
        UserModel user = repository.findById(id).orElseThrow(NotFoundException::new);
        if (!StringUtils.isEmpty(dto.getEmail()))
            user.setEmail(dto.getEmail());
        if (!StringUtils.isEmpty(dto.getName()))
            user.setName(dto.getName());

        user.setEnabled(dto.isEnabled());
        user.setRoles(getRoles(dto));

        updatePassword(dto, user);

        repository.save(user);
    }

    @Transactional
    public void remove(Long id) {
        LOGGER.debug("Remove user by id=[{}]", id);
        if (id != null && !SecurityUtil.get().getUserId().equals(id)) {
            reviewService.removeByUserId(id);
            repository.deleteById(id);
        }
    }

    public List<UserDTO> all() {
        LOGGER.debug("Get all users");
        return mapper.map(repository.findAll());
    }

    public List<UserMenuDTO> getMenuByUser(Long id, LocalDate start, LocalDate end) {
        LOGGER.debug("Get menu by user id=[{}], start=[{}], end=[{}] ", id, start, end);
        List<MenuDTO> menus = menuService.byDateWithoutRelations(DateTimeUtil.getStartDateInclusive(start), DateTimeUtil.getEndDateExclusive(end));
        Set<Long> reviewMenu = reviewService.getByUserId(id, LocalDate.now())
                .stream().map(ReviewUserDTO::getMenuId)
                .collect(Collectors.toSet());

        return menus.stream()
                .map(dto -> new UserMenuDTO(dto, reviewMenu.contains(dto.getId())))
                .collect(Collectors.toList());
    }

    private Set<Role> getRoles(UserDTO dto) {
        return CollectionUtils.isEmpty(dto.getRoles()) ? new HashSet<>(Collections
                .singletonList(Role.ROLE_USER)) : new HashSet<>(dto.getRoles());
    }

    private void updatePassword(UserDTO dto, UserModel user) {
        if (!StringUtils.isEmpty(dto.getPassword())) {
            String psw = passwordEncoder.encode(dto.getPassword());
            if (!psw.equals(user.getPassword())) user.setPassword(psw);
        }
    }
}
