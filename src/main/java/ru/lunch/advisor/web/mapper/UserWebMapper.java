package ru.lunch.advisor.web.mapper;

import org.mapstruct.Mapper;
import ru.lunch.advisor.service.dto.UserDTO;
import ru.lunch.advisor.web.request.UserCreateRequest;
import ru.lunch.advisor.web.request.UserUpdateRequest;

@Mapper(componentModel = "spring")
public interface UserWebMapper {

    UserDTO map(UserUpdateRequest user);

    UserDTO map(UserCreateRequest user);
}
