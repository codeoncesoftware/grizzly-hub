package fr.codeonce.grizzlyhub.auth.service.user;

import org.mapstruct.Mapper;

import fr.codeonce.grizzlyhub.auth.domain.user.User;


@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDto mapToDto(User entity);

	User mapToDomain(UserDto dto);
	
}
