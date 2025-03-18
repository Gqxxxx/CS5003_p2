package stacs.Users;

public class Users {
    private int identifier;
    private String name;

    public Users(int identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public int getUserId() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the name of this user.
     *
     * @param name the new name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

}
