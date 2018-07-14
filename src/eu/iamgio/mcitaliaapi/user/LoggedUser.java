package eu.iamgio.mcitaliaapi.user;

import eu.iamgio.mcitaliaapi.board.BoardPost;
import eu.iamgio.mcitaliaapi.board.BoardPostComment;
import eu.iamgio.mcitaliaapi.board.BoardPostReply;
import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.connection.json.JSONParser;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;

/**
 * Represent a logged user
 * @author Gio
 */
public class LoggedUser extends User {

    private Document document;

    LoggedUser(String name, Document document) {
        super(name);
        this.document = document;
    }

    /**
     * Sends a message to tagboard
     * @param text Text of the message
     */
    public void sendTagboardMessage(String text) {
        new HttpConnection("https://www.minecraft-italia.it/forum/xmlhttp.php?action=dvz_sb_shout&text=" + text + "&key=" + getPostKey())
                .connect()
                .post();
    }

    /**
     * Sends a private message to any user in tagboard
     * @param text Text of the message
     * @param uid Target player's UID
     */
    public void sendTagboardPrivateMessage(String text, long uid) {
        sendTagboardMessage("/pvt " + uid + " " + text);
    }

    /**
     * Creates a board post
     * @param text Text of the post
     * @param uid_to Target UID
     * @return New post
     */
    private BoardPost createBoardPost(String text, String uid_to) {
        Document document = new HttpConnection("https://www.minecraft-italia.it/board/post_add").connect()
                .data("content", text)
                .data("uid_to", uid_to)
                .data("url_preview", "")
                .post();
        JSONObject object = new JSONParser(document).parse();
        JSONObject data = (JSONObject) object.get("data");
        JSONObject post = (JSONObject) data.get("post");
        return BoardPost.fromJsonObject(post);
    }

    /**
     * Creates a board post
     * @param text Text of the post
     * @return New post
     */
    public BoardPost createBoardPost(String text) {
        return createBoardPost(text, "0");
    }

    /**
     * Creates a board post
     * @param text Text of the post
     * @param target Target user
     * @return New post
     */
    public BoardPost createBoardPost(String text, UnparsedUser target) {
        return createBoardPost(text, String.valueOf(target.getName()));
    }

    /**
     * Comments a board post
     * @param post Existing board post
     * @param text Text of the comment
     * @return New comment
     */
    public BoardPostComment commentBoardPost(BoardPost post, String text) {
        Document document = new HttpConnection("https://www.minecraft-italia.it/board/comment_add").connect()
                .data("cid", "0")
                .data("content", text)
                .data("pid", String.valueOf(post.getId()))
                .post();
        JSONObject object = new JSONParser(document).parse();
        JSONObject data = (JSONObject) object.get("data");
        JSONObject comment = (JSONObject) data.get("comment");
        return BoardPostComment.fromJsonObject(comment);
    }

    /**
     * Replies to a board comment
     * @param post Existing board comment
     * @param text Text of the reply
     * @return New reply
     */
    public BoardPostReply replyToBoardComment(BoardPost post, BoardPostComment comment, String text) {
        Document document = new HttpConnection("https://www.minecraft-italia.it/board/comment_add").connect()
                .data("cid", String.valueOf(comment.getId()))
                .data("content", text)
                .data("pid", String.valueOf(post.getId()))
                .post();
        JSONObject object = new JSONParser(document).parse();
        JSONObject data = (JSONObject) object.get("data");
        JSONObject reply = (JSONObject) data.get("comment");
        return BoardPostReply.fromJsonObject(reply);
    }

    public void removeBoardPost(BoardPost post) {
        new HttpConnection("https://www.minecraft-italia.it/board/post_remove").connect()
                .data("pid", String.valueOf(post.getId()))
                .post();
    }

    public void removeBoardComment(BoardPostComment comment) {
        new HttpConnection("https://www.minecraft-italia.it/board/comment_remove").connect()
                .data("cid", String.valueOf(comment.getId()))
                .post();
    }

    public void removeBoardReply(BoardPostReply reply) {
        new HttpConnection("https://www.minecraft-italia.it/board/comment_remove")
                .data("cid", String.valueOf(reply.getId()))
                .post();
    }
}
