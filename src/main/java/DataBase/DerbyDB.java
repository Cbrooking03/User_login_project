package DataBase;

import Data.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class DerbyDB implements DataBase {
    private final static String connectionStringTemplate = "jdbc:derby:memory:%s;create=true";
    private final static String shutdownStringTemplate = "jdbc:derby:memory:%s;drop=true";
    private final static String userTableSchema = "CREATE TABLE USERS(ID INTEGER NOT NULL, USERNAME VARCHAR(20) NOT NULL, HASH CHAR(32) NOT NULL, SALT CHAR(32) NOT NULL, ADMIN BOOLEAN NOT NULL)";
    private final static String createUserSql = "INSERT INTO USERS (ID, USERNAME, HASH, SALT, ADMIN) values (?,?,?,?,?)";
    private final static String getUserByUserNameSql = "SELECT ID, USERNAME, ADMIN FROM USERS WHERE USERNAME=?";
    private final static String getUserHashSql = "SELECT HASH FROM USERS WHERE ID=?";
    private final static String getUserSaltSql = "SELECT SALT FROM USERS WHERE ID=?";
    private final String connectionString;
    private final String shutdownString;

    /**
     * A DerbyDB constructor
     * @param dbName the name of the database to be created
     */
    public DerbyDB(String dbName){

        this.connectionString = String.format(connectionStringTemplate, Objects.requireNonNull(dbName));
        this.shutdownString = String.format(shutdownStringTemplate, Objects.requireNonNull(dbName));
        try {
            this.createUserTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }

    private void createUserTable() throws SQLException {
        try{
            Connection connection = Objects.requireNonNull(getConnection());
            PreparedStatement statement = connection.prepareStatement(userTableSchema);
            statement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public <T> T usingStatement(final String sql, final Function<PreparedStatement,T> f) {
        try {
            final PreparedStatement statement = getConnection().prepareStatement(sql);
            return f.apply(statement);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateUsingStatement(final String sql, final Consumer<PreparedStatement> f){
        usingStatement(sql, statement -> {
            f.accept(statement);
            return true;
        });
    }

    @Override
    public void insertUser(long id, String userName, String hash, String salt, boolean admin) {
        updateUsingStatement(createUserSql, preparedStatement -> {
            try {
                preparedStatement.setLong(1, id);
                preparedStatement.setString(2, userName);
                preparedStatement.setString(3, hash);
                preparedStatement.setString(4,salt);
                preparedStatement.setBoolean(5, admin);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public User getUserByUserName(String userName) {

        try (Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(getUserByUserNameSql);
            preparedStatement.setString(1, userName);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                long userIDResult = rs.getLong("ID");
                String userNameResult = rs.getString("USERNAME");
                boolean userAdminResult = rs.getBoolean("ADMIN");
                rs.close();
                User user = new User(userIDResult,userNameResult,userAdminResult);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String getUserHash(User user) {
        if(user==null){
            return null;
        }
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(getUserHashSql);
            preparedStatement.setLong(1, user.getId());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                String userHashResult = rs.getString("HASH");
                return userHashResult;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUserSalt(User user) {
        if(user==null){
            return null;
        }
        try (Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(getUserSaltSql);
            preparedStatement.setLong(1, user.getId());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                String userSaltResult = rs.getString("SALT");
                return userSaltResult;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Allows for the use of the DB in a try with resource
     * block which will close the DB on exit. Note that this
     * is only really useful for testing as the DB would need
     * to remain available for the life of the application
     * otherwise
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        DriverManager.getConnection(shutdownString);
    }
}
