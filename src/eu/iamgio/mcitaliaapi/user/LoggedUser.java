package eu.iamgio.mcitaliaapi.user;

import eu.iamgio.mcitaliaapi.board.BoardPost;
import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.json.JSONParser;
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
}
