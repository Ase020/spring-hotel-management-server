package com.asedesign.HotelServer.dto;

import com.asedesign.HotelServer.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private UserRole userRole;
}
