package com.surya.easyshop.service;

import com.surya.easyshop.dto.UserDto;
import com.surya.easyshop.exception.AlreadyExistsException;
import com.surya.easyshop.exception.ResourceNotFoundException;
import com.surya.easyshop.model.User;
import com.surya.easyshop.repository.UserRepository;
import com.surya.easyshop.request.CreateUserRequest;
import com.surya.easyshop.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(req.getEmail());
                    user.setPassword(passwordEncoder.encode(req.getPassword()));
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    return userRepository.save(user);
                })
                .orElseThrow(()-> new AlreadyExistsException("Oops! "+ request.getEmail()+" Already Exists!"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {

        return userRepository.findById(userId).map(existingUser-> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(user -> userRepository.delete(user) , ()-> {
                    throw new ResourceNotFoundException("User Not Found");
                });
    }

    @Override
    public UserDto convertUserToDto(User user)
    {
        return modelMapper.map(user , UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
