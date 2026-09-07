package com.challenge.userapi.services;

import com.challenge.userapi.dtos.UserDto;

import com.challenge.userapi.enums.UserRoleEnum;
import com.challenge.userapi.mappers.UserMapper;
import com.challenge.userapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserGeneratorService {

    private final Faker userFaker = new Faker();
    private UserRepository userRepository;
    private UserMapper userMapper;

    private UserDto generateUser() {
        return new UserDto(
                userFaker.name().firstName(),
                userFaker.name().lastName(),
                userFaker.timeAndDate().birthday(),
                userFaker.address().city(),
                userFaker.address().countryCode(),
                userFaker.avatar().image(),
                userFaker.company().name(),
                userFaker.job().title(),
                userFaker.phoneNumber().cellPhone(),
                userFaker.credentials().username(),
                userFaker.internet().emailAddress(),
                userFaker.credentials().password(6, 10),
                userFaker.options().nextElement(UserRoleEnum.values())
        );
    }

    public List<UserDto> generateUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateUser())
                .toList();
    }

    public void addUser(UserDto user) {
        userRepository.save(userMapper.toEntity(user));
    }
}
