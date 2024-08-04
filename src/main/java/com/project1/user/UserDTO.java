package com.project1.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private Integer points;
    private Status status;
    private String device_token;

}
