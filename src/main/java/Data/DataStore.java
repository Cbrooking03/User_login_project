package Data;

import Authentication.Authentication;
import DataBase.DataBase;

import java.util.Objects;

public class DataStore {
    private final DataBase db;
    private final Authentication authentication;
    private final IDGenerator idGen;

    /**
     * DataStore constructor
     * @param db a DB instance which will provide us with persistence
     * @param authentication an Authentication instance that will assist with user authentication
     * @param idGen an id generator
     */

    public DataStore(DataBase db, Authentication authentication, IDGenerator idGen){
        this.db = Objects.requireNonNull(db);
        this.authentication = Objects.requireNonNull(authentication);
        this.idGen = Objects.requireNonNull(idGen);
    }

    /**
     * Retrieves a user by user name
     *
     * @param userName the user name of the desired user
     * @return the desired user, or null if the user does not exist
     */
    public User getUser(final String userName){
        return db.getUserByUserName(userName);
    }

    /**
     * Adds a new user to the data store.
     *
     * @param userName the user's user name
     * @param password the user's plaintext password
     * @param admin a flag indicating if the user should be an admin
     * @return the new user that is being added
     */
    public User addUser(final String userName, final String password, final boolean admin){
        String userSalt = authentication.generateUserSalt(userName);
        String userHash = authentication.hash(password,userSalt);
        long userID = idGen.nextID();
        db.insertUser(userID,userName,userHash,userSalt,admin);
        return new User(userID, userName, admin);
    }

    /**
     * Convenience method for adding admins
     *
     * @param userName the user's user name
     * @param password the user's plain text password
     * @return the new admin user
     */
    public User addAdmin(final String userName, final String password){
        return addUser(userName,password,true);
    }

    /**
     * Convenience method for adding normal (non-admin) user
     *
     * @param userName the user's user name
     * @param password the user's plain text password
     * @return the new user
     */
    public User addNormalUser(final String userName, final String password){
        return addUser(userName,password,false);
    }

    /**
     * Retrieves a user by name and, in addition checks to see if the user
     * can be authenticated with the given password.
     *
     * @param userName the user name of the user to authenticate
     * @param password the plain text password of the user
     * @return the user if it exists and authenticates, or null otherwise
     */
    public User getAndAuthenticateUser(final String userName, final String password){
        User user = getUser(userName);
        String userSalt = db.getUserSalt(user);
        String userHash = db.getUserHash(user);
        if(authentication.authenticated(password,userSalt,userHash)){
            return user;
        }
        return null;

    }
}
