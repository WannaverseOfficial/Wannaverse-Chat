package com.wannaverse.users.jwt;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Jwt {
    private final Map<String, Object> claims = new HashMap<>();
    private final JwtHeader jwtHeader = new JwtHeader();
    private long start;
    private long expiration;
    private String algorithm;
    private byte[] secret;

    public Jwt addClaim(String key, Serializable value) {
        claims.put(key, value);
        return this;
    }

    public Jwt setStartDate(long date) {
        this.start = date;
        return this;
    }

    public Jwt setExpirationDate(long expiration) {
        this.expiration = expiration;
        return this;
    }

    public Jwt setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public Jwt setSecret(byte[] secret) {
        this.secret = secret;
        return this;
    }

    private JSONObject buildHeader() {
        jwtHeader.setAlgo(algorithm);
        jwtHeader.setTyp("jwt");
        jwtHeader.setIss("wc-s");
        return new JSONObject(jwtHeader);
    }

    private JSONObject buildPayload() {
        claims.put("start", start);
        claims.put("exp", expiration);
        return new JSONObject(claims);
    }

    private String buildSignature(String base64Header, String base64Payload) {
        return encode(algorithm, secret, base64Header + "." + base64Payload);
    }

    public String buildToken() {
        String base64Header = Base64.encode(buildHeader().toString());
        String base64Payload = Base64.encode(buildPayload().toString());
        String signature = buildSignature(base64Header, base64Payload);
        return Base64.encode(base64Header + "." + base64Payload + "." + signature);
    }

    private static String encode(String algorithm, byte[] key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance(algorithm);
            SecretKeySpec secret_key = new SecretKeySpec(key, algorithm);
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes()));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean authenticate(String algorithm, byte[] secret, String token) {
        String decoded = Base64.decode(token);
        String[] parts = decoded.split("\\.");
        if (parts.length == 3) {
            String signature = parts[2];
            String headerPayload = parts[0] + "." + parts[1];
            String encoded = encode(algorithm, secret, headerPayload);
            return encoded != null && encoded.equals(signature);
        }
        return false;
    }

    public static String getPayload(String token) {
        String decoded = Base64.decode(token);
        String[] parts = decoded.split("\\.");
        if (parts.length == 3) {
            return Base64.decode(parts[1]);
        }
        return "{}";
    }
}
