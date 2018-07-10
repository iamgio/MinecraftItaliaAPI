package eu.iamgio.mcitaliaapi;

/**
 * General object that represents an user of Minecraft Italia but doesn't connect to its page
 * @author Gio
 */
public class UnparsedUser {

    private String name;

    UnparsedUser(String name) {
        this.name = name;
    }

    /**
     * @return User's name
     */
    public String getName() {
        return name;
    }

    public User toParsedUser() {
        return User.getUser(name);
    }
}
