package com.wannaverse.users.jwt;

public class Base64 {

    private Base64() {
        throw new AssertionError();
    }

    public static String encode(String message) {
        return java.util.Base64.getEncoder().withoutPadding().encodeToString(message.getBytes());
    }

    public static String decode(String encoded) {
        return new String(java.util.Base64.getDecoder().decode(encoded));
    }
}
