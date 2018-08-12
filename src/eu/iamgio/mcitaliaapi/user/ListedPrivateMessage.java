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
    private boolean read;

    ListedPrivateMessage(String subject, UnparsedUser user, long id, String rawDate, boolean read) {
        this.subject = subject;
        this.user = user;
        this.id = id;
        this.rawDate = rawDate;
        this.read = read;
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

    /**
     * @return <tt>true</tt> if the message was read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @return Listed private message to actual private message
     */
    public PrivateMessage toPrivateMessage() {
        return PrivateMessage.fromId(id);
    }
}
