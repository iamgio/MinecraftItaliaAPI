package eu.iamgio.mcitaliaapi.board;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;

import java.util.Date;
import java.util.List;

/**
 * Represents a comment of a {@link BoardPost}
 * @author Gio
 */
public class BoardPostComment {

    private int id;
    private UnparsedUser user;
    private String content;
    private Date date;
    private long[] likeGivers;
    private List<BoardPostReply> replies;

    public BoardPostComment(int id, UnparsedUser user, String content, Date date, long[] likeGivers, List<BoardPostReply> replies) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
        this.likeGivers = likeGivers;
        this.replies = replies;
    }

    /**
     * @return Comment ID
     */
    public int getId() {
        return id;
    }

    /**
     * @return Author of the comment
     */
    public UnparsedUser getUser() {
        return user;
    }

    /**
     * @return Text of the comment
     */
    public String getContent() {
        return content;
    }

    /**
     * @return Date of the comment
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

    /**
     * @return Replies to the comment
     */
    public List<BoardPostReply> getReplies() {
        return replies;
    }
}
