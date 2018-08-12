package eu.iamgio.mcitaliaapi.user;

import java.util.Date;

/**
 * Represents a notification
 * @author Gio
 */
public class Notification {

    private long id, fromUid;
    private String html;
    private Date date;
    private boolean viewed;

    Notification(long id, long fromUid, String html, Date date, boolean viewed) {
        this.id = id;
        this.fromUid = fromUid;
        this.html = html;
        this.date = date;
        this.viewed = viewed;
    }

    /**
     * @return Notification ID
     */
    public long getId() {
        return id;
    }

    /**
     * @return From's UID
     */
    public long getFromUid() {
        return fromUid;
    }

    /**
     * @return Notification text as HTML
     */
    public String getHtml() {
        return html;
    }

    /**
     * @return Notification date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return <tt>true</tt> if the notification was read
     */
    public boolean isViewed() {
        return viewed;
    }
}
