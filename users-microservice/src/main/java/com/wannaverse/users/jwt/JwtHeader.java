package com.wannaverse.users.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtHeader {
    private String algo;
    private String typ;
    private String iss;
}
