package Authentication;

public interface Authentication {

    /**
     * @param password the cleartext password provided by the user
     * @param userSalt the salt associated with the user we are authenticating
     * @param userHash the hash associated with the user we are authenticating
     * @return true or false indicating whether or not the provided credentials can be authenticated
     */

    boolean authenticated(final String password, final String userSalt, final String userHash);

    /**
     * @param password the cleartext password provided by the user
     * @param salt the salt associated with the user we are authenticating
     * @return a string of a hashed version of a provided user password
     */

    String hash(final String password, final String salt);

    /**
     * @param userName the user name of the user we need a salt for
     * @return a user specific salt which will be used for authentication
     */

    String generateUserSalt(final String userName);
}
