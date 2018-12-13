package eu.iamgio.mcitaliaapi.tagboard;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;

import java.util.Date;

/**
 * Represent a {@link Tagboard} message
 * @deprecated The tagboard has been deleted from the website
 * @author Gio
 */
@Deprecated
public class TagboardMessage {

    private UnparsedUser user, target;
    private long id;
    private String text;
    private long userUid;
    private Date date;

    public TagboardMessage(UnparsedUser user, UnparsedUser target, long id, String text, long userUid, Date date) {
        this.user = user;
        this.target = target;
        this.id = id;
        this.text = text;
        this.userUid = userUid;
        this.date = date;
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
    public long getUserUid() {
        return userUid;
    }

    /**
     * @return Date of the message
     */
    public Date getDate() {
        return date;
    }
}