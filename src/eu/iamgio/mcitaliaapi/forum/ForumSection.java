package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;

import java.util.List;

/**
 * @author Gio
 */
public class ForumSection extends ForumSubSection {

    private String description, imageUrl, lastPostTitle;
    private int topicsCount, messagesCount, viewingNowCount;
    private UnparsedUser lastPostUser;
    private List<ForumSubSection> subsections;

    ForumSection(String name, String url, String description, String imageUrl, int topicsCount, int messagesCount, int viewingNowCount, String lastPostTitle, UnparsedUser lastPostUser, List<ForumSubSection> subsections) {
        super(name, url);
        this.description = description;
        this.imageUrl = imageUrl;
        this.lastPostTitle = lastPostTitle;
        this.topicsCount = topicsCount;
        this.messagesCount = messagesCount;
        this.viewingNowCount = viewingNowCount;
        this.lastPostUser = lastPostUser;
        this.subsections = subsections;
    }

    /**
     * @return Section description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Icon URL of the section
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @return Title of the last post in the section
     */
    public String getLastPostTitle() {
        return lastPostTitle;
    }

    /**
     * @return Amount of topics
     */
    public int getTopicsCount() {
        return topicsCount;
    }

    /**
     * @return Amount of messages
     */
    public int getMessagesCount() {
        return messagesCount;
    }

    /**
     * @return Amount of users seeing this section now
     */
    public int getViewingNowCount() {
        return viewingNowCount;
    }

    /**
     * @return Author of last post in the section
     */
    public UnparsedUser getLastPostUser() {
        return lastPostUser;
    }

    /**
     * @return Subsections of this section
     */
    public List<ForumSubSection> getSubsections() {
        return subsections;
    }
}
