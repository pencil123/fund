package cn.blogscn.fund.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.apache.tomcat.util.codec.binary.Base64;

public class EncryptionUtil {

    /**
     * Base64 encode
     */
    public static String base64Encode(String data) {
        return Base64.encodeBase64String(data.getBytes());
    }

    /**
     * Base64 decode
     */
    public static String base64Decode(String data) throws UnsupportedEncodingException {
        return new String(Base64.decodeBase64(data.getBytes()), StandardCharsets.UTF_8);
    }
}
