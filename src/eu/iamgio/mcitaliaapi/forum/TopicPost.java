package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;

import java.util.Date;
import java.util.List;

/**
 * Represents a post inside of a topic
 * @author Gio
 */
public class TopicPost {

    private long id;

    private String plainText, html;

    private UnparsedUser user;


    // These values can be gained through User but they are loaded here too to prevent other connections
    private int usersMessagesCount, usersTopicsCount, usersLikesReceivedCount, usersLikedPostsCount, usersLikesGivenCount;
    private List<String> usersBadges;
    private String rawRegistrationDate;
    private Date registrationDate;
    private boolean userOnline;

    private List<UnparsedUser> likeGivers;

    private String usersSignatureHtml;

    TopicPost(long id, String plainText, String html, UnparsedUser user, int usersMessagesCount, int usersTopicsCount, int usersLikesReceivedCount, int usersLikedPostsCount, int usersLikesGivenCount, List<String> usersBadges, String rawRegistrationDate, Date registrationDate, boolean userOnline, List<UnparsedUser> likeGivers, String usersSignatureHtml) {
        this.id = id;
        this.plainText = plainText;
        this.html = html;
        this.user = user;
        this.usersMessagesCount = usersMessagesCount;
        this.usersTopicsCount = usersTopicsCount;
        this.usersLikesReceivedCount = usersLikesReceivedCount;
        this.usersLikedPostsCount = usersLikedPostsCount;
        this.usersLikesGivenCount = usersLikesGivenCount;
        this.usersBadges = usersBadges;
        this.rawRegistrationDate = rawRegistrationDate;
        this.registrationDate = registrationDate;
        this.userOnline = userOnline;
        this.likeGivers = likeGivers;
        this.usersSignatureHtml = usersSignatureHtml;
    }

    /**
     * @return Post ID
     */
    public long getId() {
        return id;
    }

    /**
     * @return Post content as plain text
     */
    public String getPlainText() {
        return plainText;
    }

    /**
     * @return Post content as HTML
     */
    public String getHtml() {
        return html;
    }

    /**
     * @return Author of the post
     */
    public UnparsedUser getUser() {
        return user;
    }

    /**
     * @return User's amount of messages sent
     */
    public int getUsersMessagesCount() {
        return usersMessagesCount;
    }

    /**
     * @return User's amount of topics created
     */
    public int getUsersTopicsCount() {
        return usersTopicsCount;
    }

    /**
     * @return User's amount of likes received
     */
    public int getUsersLikesReceivedCount() {
        return usersLikesReceivedCount;
    }

    /**
     * @return User's amount of posts that received likes
     */
    public int getUsersLikedPostsCount() {
        return usersLikedPostsCount;
    }

    /**
     * @return User's likes given
     */
    public int getUsersLikesGivenCount() {
        return usersLikesGivenCount;
    }

    /**
     * @return User's badges
     */
    public List<String> getUsersBadges() {
        return usersBadges;
    }

    /**
     * @return Raw user's registration date
     */
    public String getRawRegistrationDate() {
        return rawRegistrationDate;
    }

    /**
     * @return User's registration date
     */
    public Date getRegistrationDate() {
        return registrationDate;
    }

    /**
     * @return <tt>true</tt> if user is online
     */
    public boolean isUserOnline() {
        return userOnline;
    }

    /**
     * @return People who liked the post
     */
    public List<UnparsedUser> getLikeGivers() {
        return likeGivers;
    }

    /**
     * @return User's signature content as HTML
     */
    public String getUsersSignatureHtml() {
        return usersSignatureHtml;
    }
}
