package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;

import java.util.List;

/**
 * @author Gio
 */
public class ForumSection extends ForumSubSection {

    private String description, imageUrl, lastPostTitle;
    private int discussionsCount, messagesCount, viewingNowCount;
    private UnparsedUser lastPostUser;
    private List<ForumSubSection> subsections;

    ForumSection(String name, String url, String description, String imageUrl, int discussionsCount, int messagesCount, int viewingNowCount, String lastPostTitle, UnparsedUser lastPostUser, List<ForumSubSection> subsections) {
        super(name, url);
        this.name = name;
        this.url = url;
        this.description = description;
        this.imageUrl = imageUrl;
        this.lastPostTitle = lastPostTitle;
        this.discussionsCount = discussionsCount;
        this.messagesCount = messagesCount;
        this.viewingNowCount = viewingNowCount;
        this.lastPostUser = lastPostUser;
        this.subsections = subsections;
    }
}
