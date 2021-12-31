package com.example.socialtpygui.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashStringSHA_256 {

    public static String hashString(String string) throws NoSuchAlgorithmException {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
            BigInteger noHash = new BigInteger(1, hash);
            return noHash.toString(16);
    }
}
