package com.pws.usermanagement.utility;

import com.pws.usermanagement.config.ApiSuccess;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonUtils {

    final static Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
    private CommonUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String key = "aesEncryptionKey";
    private static final String initVector = "encryptionIntVec";

    // formatting of the success response
    public static ResponseEntity<Object> buildResponseEntity(ApiSuccess apiSuccess) {
        if (LOGGER.isDebugEnabled() || LOGGER.isInfoEnabled()) {
            LOGGER.info(ApplicationUtils.CLASSNAME + apiSuccess.toString());
        }
        return new ResponseEntity<>(apiSuccess, apiSuccess.getStatus());
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    public static Pageable getPageable(int page, int size, String sort, String order) {
        Pageable sorted;
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)
                && order.equalsIgnoreCase(ApplicationUtils.DESCENDING)) {
            sorted = PageRequest.of(page, size, Sort.by(sort).descending());
        } else if (!StringUtils.isEmpty(sort)) {
            sorted = PageRequest.of(page, size, Sort.by(sort));
        } else {
            sorted = PageRequest.of(page, size);
        }
        return sorted;
    }

    public static Pageable getPageableWithoutSorting(int page, int size) {
        Pageable sorted;
        sorted = PageRequest.of(page, size);
        return sorted;
    }

    public static String randomUUIDgenerator() {
        return UUID.randomUUID().toString();
    }


    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

}
