package eu.iamgio.mcitaliaapi.user;

/**
 * Represents a listed private message
 * @author Gio
 */
public class ListedPrivateMessage {

    private String subject;
    private UnparsedUser user;
    private long id;
    private String rawDate;

    ListedPrivateMessage(String subject, UnparsedUser user, long id, String rawDate) {
        this.subject = subject;
        this.user = user;
        this.id = id;
        this.rawDate = rawDate;
    }

    /**
     * @return Message subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return Author of the message
     */
    public UnparsedUser getUser() {
        return user;
    }

    /**
     * @return Private message ID
     */
    public long getId() {
        return id;
    }

    /**
     * @return Raw date of the message
     */
    public String getRawDate() {
        return rawDate;
    }
}
