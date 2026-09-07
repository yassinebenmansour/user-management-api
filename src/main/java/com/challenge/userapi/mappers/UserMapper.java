package com.challenge.userapi.mappers;

import com.challenge.userapi.dtos.UserDto;
import com.challenge.userapi.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(UserDto userDto){
        if(userDto == null){
            return null;
        }
        return UserEntity.builder()
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .birthDate(userDto.birthDate())
                .city(userDto.city())
                .country(userDto.country())
                .avatar(userDto.avatar())
                .company(userDto.company())
                .jobPosition(userDto.jobPosition())
                .mobile(userDto.mobile())
                .username(userDto.username())
                .email(userDto.email())
                .password(userDto.password())
                .role(userDto.role())
                .build();
    }

    public UserDto toDto(UserEntity userEntity){
        if(userEntity == null){
            return null;
        }
        return UserDto.builder()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .birthDate(userEntity.getBirthDate())
                .city(userEntity.getCity())
                .country(userEntity.getCountry())
                .avatar(userEntity.getAvatar())
                .company(userEntity.getCompany())
                .jobPosition(userEntity.getJobPosition())
                .mobile(userEntity.getMobile())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .build();
    }
}
