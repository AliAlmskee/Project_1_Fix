package com.project1.report.data;


import com.project1.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportWithUserDTO {
    private Long id;
    private String message;
    private UserDTO sender;
    private UserDTO recipient;
}
