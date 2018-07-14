package eu.iamgio.mcitaliaapi.board;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;
import eu.iamgio.mcitaliaapi.util.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
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

    BoardPostComment(int id, UnparsedUser user, String content, Date date, long[] likeGivers, List<BoardPostReply> replies) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
        this.likeGivers = likeGivers;
        this.replies = replies;
    }

    public static BoardPostComment fromJsonObject(JSONObject json) {
        JSONObject commentInteractionsJson = (JSONObject) json.get("interactions");
        int commentId = Integer.parseInt(json.get("id").toString());
        UnparsedUser commentUser = new UnparsedUser(json.get("username").toString());
        String commentContent = json.get("content").toString();
        Date commentDate = Utils.getDateByTimestamp(json.get("timestamp").toString());
        long[] commentLikeGivers = Utils.longJsonArrayToLongArray((JSONArray) commentInteractionsJson.get("like"));
        List<BoardPostReply> replies = new ArrayList<>();
        JSONArray repliesJson = (JSONArray) json.get("replies");
        for(Object replyObj : repliesJson) {
            replies.add(BoardPostReply.fromJsonObject((JSONObject) replyObj));
        }
        return new BoardPostComment(commentId, commentUser, commentContent, commentDate, commentLikeGivers, replies);
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
