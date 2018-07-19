package eu.iamgio.mcitaliaapi.tagboard;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;

import java.util.Date;

/**
 * Represent a {@link Tagboard} message
 * @author Gio
 */
public class TagboardMessage {

    private UnparsedUser user, target;
    private long id;
    private String text;
    private long usersUid;
    private Date date;
    private String usersAvatarUrl;

    public TagboardMessage(UnparsedUser user, UnparsedUser target, long id, String text, long usersUid, Date date, String usersAvatarUrl) {
        this.user = user;
        this.target = target;
        this.id = id;
        this.text = text;
        this.usersUid = usersUid;
        this.date = date;
        this.usersAvatarUrl = usersAvatarUrl;
    }

    /**
     * @return Message author
     */
    public UnparsedUser getUser() {
        return user;
    }

    /**
     * @return Target of the message (may be null)
     */
    public UnparsedUser getTarget() {
        return target;
    }

    /**
     * @return Message ID
     */
    public long getId() {
        return id;
    }

    /**
     * @return Text of the message
     */
    public String getText() {
        return text;
    }

    /**
     * @return Author's UID
     */
    public long getUsersUid() {
        return usersUid;
    }

    /**
     * @return Date of the message
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return Author's avatar URL
     */
    public String getUsersAvatarUrl() {
        return usersAvatarUrl;
    }
}