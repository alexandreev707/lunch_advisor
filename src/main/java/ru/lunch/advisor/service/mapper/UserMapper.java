package ru.lunch.advisor.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lunch.advisor.persistence.model.UserModel;
import ru.lunch.advisor.service.dto.UserDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "roles", target = "roles")
    UserDTO map(UserModel user);

    List<UserDTO> map(List<UserModel> user);
}
