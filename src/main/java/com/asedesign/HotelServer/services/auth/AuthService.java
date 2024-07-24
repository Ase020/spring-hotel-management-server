package com.asedesign.HotelServer.services.auth;

import com.asedesign.HotelServer.dto.SignupRequest;
import com.asedesign.HotelServer.dto.UserDTO;
import org.springframework.stereotype.Service;


public interface AuthService {
    UserDTO createUser(SignupRequest signupRequest);
}
