package com.wannaverse.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String displayName;
}
