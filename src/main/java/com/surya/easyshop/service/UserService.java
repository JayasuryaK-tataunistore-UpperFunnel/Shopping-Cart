package com.surya.easyshop.service;

import com.surya.easyshop.dto.UserDto;
import com.surya.easyshop.model.User;
import com.surya.easyshop.request.CreateUserRequest;
import com.surya.easyshop.request.UserUpdateRequest;

public interface UserService {
    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UserUpdateRequest request , Long userId);

    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
