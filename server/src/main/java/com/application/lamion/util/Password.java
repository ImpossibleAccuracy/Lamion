package com.application.lamion.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Password {
    private static final int COST = 12;

    public static String hash(String plain) {
        return BCrypt.withDefaults().hashToString(12, plain.toCharArray());
    }

    public static boolean verify(String plain, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(plain.toCharArray(), hash);
        return result.verified;
    }
}
