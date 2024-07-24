package com.asedesign.HotelServer.services.auth;

import com.asedesign.HotelServer.dto.SignupRequest;
import com.asedesign.HotelServer.dto.UserDTO;
import com.asedesign.HotelServer.entity.User;
import com.asedesign.HotelServer.enums.UserRole;
import com.asedesign.HotelServer.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final UserRepository userRepository;

    @PostConstruct
    public void createAdminAccount(){
        Optional<User> adminAccount = userRepository.findByUserRole(UserRole.ADMIN);
        if (adminAccount.isEmpty()) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("Admin");
            user.setUserRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);

            System.out.println("Admin account created successfully!");
        }else {
            System.out.println("Admin account already exist!");
        }
    }

    public UserDTO createUser(SignupRequest signupRequest){
        if (userRepository.findFirstByEmail(signupRequest.getEmail()).isPresent()) {
            throw new EntityExistsException("User the same email already exists! " + signupRequest.getEmail());
        }

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setUserRole(UserRole.CUSTOMER);
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));

        User createdUser = userRepository.save(user);

        return createdUser.getUserDTO();
    }
}
