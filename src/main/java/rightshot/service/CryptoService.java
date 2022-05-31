package rightshot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import static io.swagger.v3.oas.integration.StringOpenApiConfigurationLoader.LOGGER;

@Service
public class CryptoService {

    @Value("${api.crypto.secret}")
    String secretCrypto;

    public String decryptText(String textToDencrypt) {
        String decryptedText = null;
        byte[] cipherData = java.util.Base64.getDecoder().decode(textToDencrypt);
        byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secretCrypto.getBytes(StandardCharsets.UTF_8), md5);
            SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
            IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

            byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
            Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decryptedData = aesCBC.doFinal(encrypted);
            decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
            LOGGER.info("decryptedText success");
            return decryptedText;
        } catch (Exception ex) {
            LOGGER.error("error on decrypt: " + ex.getMessage());
            return decryptedText;
        }
    }

    //TODO: Arrumar essa bagunça
    private static String encript(String textToEncrypt, String secretCrypto){
        String SALT = "ssshhhhhhhhhhh!!!!";
        byte[] cipherData = java.util.Base64.getEncoder().encodeToString(textToEncrypt.getBytes()).getBytes();
        byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

        try {
            //byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            //IvParameterSpec ivspec = new IvParameterSpec(iv);

            //SecretKeyFactory factory = SecretKeyFactory.getInstance("MD5");
            //KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            //SecretKey tmp = factory.generateSecret(spec);
            //SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secretCrypto.getBytes(StandardCharsets.UTF_8), md5);
            SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
            IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);



            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
        //return textToEncrypt;
    }


    private static byte[][] GenerateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {

        int digestLength = md.getDigestLength();
        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
        byte[] generatedData = new byte[requiredLength];
        int generatedLength = 0;

        try {
            md.reset();

            // Repeat process until sufficient data has been generated
            while (generatedLength < keyLength + ivLength) {

                // Digest data (last digest if available, password data, salt if available)
                if (generatedLength > 0)
                    md.update(generatedData, generatedLength - digestLength, digestLength);
                md.update(password);
                if (salt != null)
                    md.update(salt, 0, 8);
                md.digest(generatedData, generatedLength, digestLength);

                // additional rounds
                for (int i = 1; i < iterations; i++) {
                    md.update(generatedData, generatedLength, digestLength);
                    md.digest(generatedData, generatedLength, digestLength);
                }

                generatedLength += digestLength;
            }

            // Copy key and IV into separate byte arrays
            byte[][] result = new byte[2][];
            result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
            if (ivLength > 0)
                result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);

            return result;

        } catch (DigestException e) {

            throw new RuntimeException(e);

        } finally {
            // Clean out temporary data
            Arrays.fill(generatedData, (byte) 0);
        }
    }

/*    public static void main(String a[]) throws Exception {
        final String secretKey = "RightShotClub";

        String originalString = "senha";
        String encryptedString = "U2FsdGVkX19Lc4Rjee+FTT16MVuASAhV0E1pvevGREA=";
        String decryptedString = CryptoJs.decryptText(encryptedString);

        System.out.println(originalString);
        System.out.println(CryptoJs.encript("senha","RightShotClub"));
        System.out.println(encryptedString);
        System.out.println(decryptedString);
    }*/
}


