package DataBase;

import Data.User;

import java.sql.PreparedStatement;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DataBase extends AutoCloseable {
    /**
     * A generic method for querying a database. The user
     * must provide a sql query and a function which can
     * transform a PreparedStatement to an object of type T,
     * and this method will perform the boiler plate actions
     * of getting a connection and preparing a statement.
     *
     * @param sql the sql query to execute
     * @param f the logic that needs to be used to process the prepared statement
     * @param <T> the type of object which will be returned
     * @return the result of applying f to the prepared statement for the given sql query
     */
    <T> T usingStatement(final String sql, final Function<PreparedStatement,T> f);

    /**
     * Another helper method which eliminates some of the boiler
     * plate code necessary for executing a database update.
     *
     * @param sql the sql to be executed
     * @param f the logic needed to process the prepared statement
     */
    void updateUsingStatement(final String sql, final Consumer<PreparedStatement> f);

    /**
     * A method for adding a new user to our database.
     *
     * @param id the id of the user to be added
     * @param userName the user name of the user to be added
     * @param hash the hash of the user's password and salts
     * @param salt the individual salt for this specific user
     * @param admin a flag indicating whether or not this user is an admin
     */
    void insertUser(final long id, final String userName, final String hash, final String salt, final boolean admin);

    /**
     * A convenience method for finding a User by user name.
     *
     * @param userName the user name of the User we are looking for
     * @return the desired User if it exists, or null otherwise
     */
    User getUserByUserName(final String userName);

    /**
     * A method for retrieving a given User's password hash.
     * This method is needed, because the User object does not
     * contain this information.
     *
     * @param user the User for whom we need the hash
     * @return the given User's hash
     */
    String getUserHash(final User user);

    /**
     * A method for retrieving a given User's password salt.
     * This method is needed, because the User object does not
     * contain this information.
     *
     * @param user the User for whom we need the salt
     * @return the given User's salt
     */
    String getUserSalt(final User user);
}
