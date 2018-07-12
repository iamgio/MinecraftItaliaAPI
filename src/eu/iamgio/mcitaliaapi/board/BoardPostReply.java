package eu.iamgio.mcitaliaapi.board;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;

import java.util.Date;

/**
 * Represents a reply to a {@link BoardPostComment}
 * @author Gio
 */
public class BoardPostReply {

    private int id;
    private UnparsedUser user;
    private String content;
    private Date date;
    private long[] likeGivers;

    public BoardPostReply(int id, UnparsedUser user, String content, Date date, long[] likeGivers) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
        this.likeGivers = likeGivers;
    }

    /**
     * @return Reply ID
     */
    public int getId() {
        return id;
    }

    /**
     * @return Author of the reply
     */
    public UnparsedUser getUser() {
        return user;
    }

    /**
     * @return Text of the reply
     */
    public String getContent() {
        return content;
    }

    /**
     * @return Date of the reply
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return UIDs of people who liked the post
     */
    public long[] getLikeGivers() {
        return likeGivers;
    }
}
