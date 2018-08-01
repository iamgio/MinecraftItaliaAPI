package eu.iamgio.mcitaliaapi.user;

/**
 * General object that represents an user of Minecraft Italia but doesn't connect to its page
 * @author Gio
 */
public class UnparsedUser {

    private String name;

    public UnparsedUser(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return User's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return URL of Minecraft skin icon
     */
    public String getMinecraftSkinIconUrl() {
        return "https://www.minecraft-italia.it/utils/minepic_avatar/" + name;
    }

    /**
     * @return Parsed user
     */
    public User toParsedUser() {
        return User.fromName(name);
    }
}
