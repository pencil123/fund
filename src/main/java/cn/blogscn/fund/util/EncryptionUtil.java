package cn.blogscn.fund.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtil {

    /**
     * Base64 encode
     */
    public static String base64Encode(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    /**
     * Base64 decode
     */
    public static String base64Decode(String data) {
        return new String(Base64.getDecoder().decode(data.getBytes()), StandardCharsets.UTF_8);
    }
}
