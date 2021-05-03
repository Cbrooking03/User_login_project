package Data;

import java.util.Objects;

public class User {
    private final long id;
    private final String userName;
    private final boolean isAdmin;

    /**
     * User constructor
     * @param id the user's id
     * @param userName the user's name
     * @param isAdmin a flag indicating whether or not the user is an admin
     */
    public User(long id, String userName, boolean isAdmin){
        this.id = id;
        this.userName = Objects.requireNonNull(userName);
        this.isAdmin = isAdmin;
    }

    /**
     * An accessor method for this user's ID
     *
     * @return the user's id
     */

    public long getId(){
        return id;
    }

    /**
     * An accessor method for this user's name
     *
     * @return the user name of the given user
     */

    public String getUserName(){
        return userName;
    }

    /**
     * A method for checking whether or not this user is an admin.
     *
     * @return true if the user is an admin
     */

    public boolean isAdmin(){
        return isAdmin;
    }

    @Override
    public String toString(){
        return "Person(" + id + ", " + userName + ", " + isAdmin + ")";
    }

    @Override
    public int hashCode(){
        return Objects.hash(id,userName,isAdmin);
    }

    @Override
    public boolean equals(final Object that){
        if(this==that){
            return true;
        }else if(!(that instanceof User)){
            return false;
        }else{
            final User thatUser = (User) that;
            return id==thatUser.id
                    && userName.equals(thatUser.userName)
                    && isAdmin==thatUser.isAdmin;
        }
    }
}
