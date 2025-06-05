package utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtils {

    private static final String SECRET_KEY = "12345678901234567890123456789012"; // 32 bytes para AES-256

    public static String encrypt(String value) throws Exception {
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

        // Generar IV aleatorio
        byte[] ivBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));

        // Guardamos el IV + texto cifrado, separados por dos puntos, todo en base64
        String ivBase64 = Base64.getEncoder().encodeToString(ivBytes);
        String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);
        return ivBase64 + ":" + encryptedBase64;
    }

    public static String decrypt(String input) throws Exception {
        String[] parts = input.split(":");
        byte[] ivBytes = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);

        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] original = cipher.doFinal(encryptedBytes);

        return new String(original, "UTF-8");
    }
}


