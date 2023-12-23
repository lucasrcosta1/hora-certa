package com.horacerta.api.auxiliary.hashPwd;

import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Data
public class HashPwd {

    private MessageDigest digest;
    private byte[] hashbytes;

    public HashPwd(String pwdToBeHashed) throws NoSuchAlgorithmException {
        this.digest = MessageDigest.getInstance("SHA3-256");
        this.hashbytes = digest.digest(pwdToBeHashed.getBytes(StandardCharsets.UTF_8));
    }

    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
