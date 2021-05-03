package Authentication;

import Authentication.Authentication;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class DefaultAuthentication implements Authentication {

    private final String universalSalt;
    private final Random random;

    public DefaultAuthentication(Random random, String universalSalt){
        this.random = Objects.requireNonNull(random);
        this.universalSalt = Objects.requireNonNull(universalSalt);
    }

    @Override
    public boolean authenticated(String password, String userSalt, String userHash) {
        String hashedPassword = hash(password, userSalt);
        return hashedPassword.equals(userHash);
    }

    @Override
    public String hash(String password, String salt) {
        try{

            MessageDigest md = MessageDigest.getInstance("MD5");
            String plaintext = password + salt + universalSalt;
            md.update(plaintext.getBytes());
            byte[] digest = md.digest();
            return new BigInteger(1, digest).toString(16).toUpperCase(Locale.ROOT);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String generateUserSalt(String userName) {
        String unHashedSalt = userName + random.nextLong();
        try{

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(unHashedSalt.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return new BigInteger(1, digest).toString(16).toUpperCase(Locale.ROOT);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
