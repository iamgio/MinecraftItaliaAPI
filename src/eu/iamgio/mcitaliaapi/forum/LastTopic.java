package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;

import java.util.Date;

/**
 * Represents a topic inside of 'Last topics' list
 * @author Gio
 */
public class LastTopic {

    private String name, url, firstMessageText;
    private UnparsedUser author;
    private long id, authorUid;
    private ForumSubSection section;
    private Date date;

    LastTopic(String name, String url, String firstMessageText, UnparsedUser author, long id, long authorUid, ForumSubSection section, Date date) {
        this.name = name;
        this.url = Forum.FORUM_URL + url;
        this.firstMessageText = firstMessageText;
        this.author = author;
        this.id = id;
        this.authorUid = authorUid;
        this.section = section;
        this.date = date;
    }

    /**
     * @return Topic name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Topic URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return First message (original post) of the topic
     */
    public String getFirstMessageText() {
        return firstMessageText;
    }

    /**
     * @return Getter for property {@link #author}
     */
    public UnparsedUser getAuthor() {
        return author;
    }

    /**
     * @return ID of the topic
     */
    public long getId() {
        return id;
    }

    /**
     * @return Author's UUID
     */
    public long getAuthorUid() {
        return authorUid;
    }

    /**
     * @return Section of the topic
     */
    public ForumSubSection getSection() {
        return section;
    }

    /**
     * @return Date of the topic
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return This to actual topic
     */
    public Topic toTopic() {
        return Topic.fromUrl(url);
    }

    /**
     * @param page Start page
     * @return This to actual topic
     */
    public Topic toTopic(int page) {
        return Topic.fromUrl(url, page);
    }
}
