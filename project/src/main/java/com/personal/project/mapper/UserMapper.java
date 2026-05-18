package com.personal.project.mapper;

import com.personal.project.dto.EditProfileRequest;
import com.personal.project.dto.RegisterRequest;
import com.personal.project.dto.UserDTO;
import com.personal.project.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface UserMapper {

    @Mapping(target = "address", source = "address")
    UserDTO toDto(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(RegisterRequest request);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfile(EditProfileRequest request, @MappingTarget User user);
}
