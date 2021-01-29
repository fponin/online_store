package com.jm.online_store.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jm.online_store.model.Role;
import com.jm.online_store.model.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description =  "DTO для данных юзера")
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private User.Gender userGender;
    private LocalDate birthdayDate;
    private LocalDate registerDate;
    private String profilePicture;
    private Set<Role> roles;
    Collection<? extends GrantedAuthority> authorities;

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRegisterDate(user.getRegisterDate());
        userDto.setProfilePicture(user.getProfilePicture());
        userDto.setRoles(user.getRoles());
        userDto.setAuthorities(user.getAuthorities());
        return userDto;
    }
}