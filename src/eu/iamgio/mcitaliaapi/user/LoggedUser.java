package eu.iamgio.mcitaliaapi.user;

import com.sun.istack.internal.Nullable;
import eu.iamgio.mcitaliaapi.board.Board;
import eu.iamgio.mcitaliaapi.board.BoardPost;
import eu.iamgio.mcitaliaapi.board.BoardPostComment;
import eu.iamgio.mcitaliaapi.board.BoardPostReply;
import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.connection.json.JSONParser;
import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
import eu.iamgio.mcitaliaapi.forum.ForumSubSection;
import eu.iamgio.mcitaliaapi.forum.Topic;
import eu.iamgio.mcitaliaapi.forum.TopicPoll;
import eu.iamgio.mcitaliaapi.forum.TopicPost;
import eu.iamgio.mcitaliaapi.util.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Represent a logged user
 * @author Gio
 */
public class LoggedUser extends User {

    LoggedUser(String name) {
        super(name);
    }

    /**
     * Follows an user
     * @param uid Target user's UID
     */
    public void followUser(long uid) {
        new HttpConnection("https://www.minecraft-italia.it/board/set_buddylist").connect()
                .data("status", "0")
                .data("uid", String.valueOf(uid))
                .post();
    }

    /**
     * Unfollows an user
     * @param uid Target user's UID
     */
    public void unfollowUser(long uid) {
        new HttpConnection("https://www.minecraft-italia.it/board/set_buddylist").connect()
                .data("status", "1")
                .data("uid", String.valueOf(uid))
                .post();
    }

    /**
     * Blocks an user
     * @param uid Target user's UID
     */
    public void blockUser(long uid) {
        new HttpConnection("https://www.minecraft-italia.it/board/set_ignorelist").connect()
                .data("status", "0")
                .data("uid", String.valueOf(uid))
                .post();
    }

    /**
     * Unblocks an user
     * @param uid Target user's UID
     */
    public void unblockUser(long uid) {
        new HttpConnection("https://www.minecraft-italia.it/board/set_ignorelist").connect()
                .data("status", "1")
                .data("uid", String.valueOf(uid))
                .post();
    }

    /**
     * @param section Section of the topic
     * @param title Title of the topic
     * @param text Text of the topic
     * @return Object that will be used to manage attributes of the new topic
     */
    public NewTopic newTopic(ForumSubSection section, String title, String text) {
        return new NewTopic(this, title, text, section);
    }

    /**
     * Creates a post inside of topic
     * @param topic Topic
     * @param text Text of the post
     * @throws MinecraftItaliaException if an error occurred
     */
    public void replyToTopic(Topic topic, String text) throws MinecraftItaliaException {
        Document document = new HttpConnection("https://www.minecraft-italia.it/forum/newreply.php?ajax=1").connect()
                .data("action", "do_newreply")
                .data("frompage", "1")
                .data("lastpid", "1")
                .data("message", text)
                .data("method", "quickreply")
                .data("my_post_key", getPostKey())
                .data("posthash", topic.getPostHash())
                .data("postoptions[signature]", "1")
                .data("subject", topic.getReplySubject())
                .data("tid", String.valueOf(topic.getId()))
                .post();
        String error = Utils.retrieveErrorFromJson(new JSONParser(document).parse());
        if(error != null) throw new MinecraftItaliaException(error);
    }

    /**
     * Edits a user's post
     * @param post Target post
     * @param text Edited text
     * @throws MinecraftItaliaException if an error occurred
     */
    public void editPost(TopicPost post, String text) throws MinecraftItaliaException {
        Document document = new HttpConnection("https://www.minecraft-italia.it/forum/xmlhttp.php?action=edit_post&do=update_post&pid=" + post.getId() + "&my_post_key=" + getPostKey()).connect()
                .data("id", "pid_" + post.getId())
                .data("value", text)
                .post();
        String error = Utils.retrieveErrorFromJson(new JSONParser(document).parse());
        if(error != null) throw new MinecraftItaliaException(error);
    }

    /**
     * Votes to an open poll
     * @param poll Target poll
     * @param option Option index (starts at 0)
     * @throws MinecraftItaliaException if the poll is locked
     */
    public void votePoll(TopicPoll poll, int option) throws MinecraftItaliaException {
        if(poll.isLocked()) throw new MinecraftItaliaException("Poll is locked.");
        new HttpConnection("https://www.minecraft-italia.it/forum/polls.php")
                .data("action", "vote")
                .data("my_post_key", getPostKey())
                .data("option", String.valueOf(option + 1))
                .data("pid", String.valueOf(poll.getId()))
                .post();
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
     * @return Friends' first 15 board posts
     */
    public List<BoardPost> getFriendsBoardPosts() {
        return Board.getBoardPosts("https://www.minecraft-italia.it/board/get_posts?filter[type]=friends&filter[uid]=0&start=0");
    }

    /**
     * @param start Start post
     * @return Friends' 15 board posts after <tt>start</tt>
     */
    public List<BoardPost> getFriendsBoardPosts(BoardPost start) {
        return Board.getBoardPosts("https://www.minecraft-italia.it/board/get_posts?filter[type]=friends&filter[uid]=0&start=" + start.getId());
    }

    private String retrieveMediaId(File imageFile) throws MinecraftItaliaException, IOException {
        HttpConnection imageConnection = new HttpConnection("https://www.minecraft-italia.it/board/image_add").connect()
                .data("image", imageFile.getName(), new FileInputStream(imageFile));
        JSONObject json = new JSONParser(imageConnection.post()).parse();
        if(json.get("status").equals("error")) {
            throw new MinecraftItaliaException(json.get("descr").toString());
        }
        JSONObject data = (JSONObject) json.get("data");
        return data.get("image_id").toString();
    }


    /**
     * Creates a board post
     * @param text Text of the post
     * @param uid_to Target UID
     * @param parameters Additional parameters to add inside of the request
     * @return New post
     */
    private BoardPost createBoardPost(String text, String uid_to, @Nullable HashMap<String, String> parameters) {
        HttpConnection connection = new HttpConnection("https://www.minecraft-italia.it/board/post_add").connect()
                .data("content", text)
                .data("uid_to", uid_to)
                .data("url_preview", "");
        if(parameters != null) {
            for(String parameter : parameters.keySet()) {
                connection = connection.data(parameter, parameters.get(parameter));
            }
        }
        Document document = connection.post();
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
        return createBoardPost(text, "0", null);
    }

    /**
     * Creates a board post with an image
     * @param text Text of the post
     * @param imageFile Image file
     * @return New post
     * @throws MinecraftItaliaException if an error occurred during the image upload
     * @throws IOException if an error occurred during the image read
     */
    public BoardPost createBoardPost(String text, File imageFile) throws MinecraftItaliaException, IOException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("media[]", retrieveMediaId(imageFile));
        return createBoardPost(text, "0", parameters);
    }

    /**
     * Creates a board post
     * @param text Text of the post
     * @param targetUid Target user's UID
     * @return New post
     */
    public BoardPost createBoardPost(String text, long targetUid) {
        return createBoardPost(text, String.valueOf(targetUid), null);
    }

    /**
     * Creates a board post with an image
     * @param text Text of the post
     * @param imageFile Image file
     * @param targetUid Target user's UID
     * @return New post
     * @throws MinecraftItaliaException if an error occurred during the image upload
     * @throws IOException if an error occurred during the image read
     */
    public BoardPost createBoardPost(String text, File imageFile, long targetUid) throws MinecraftItaliaException, IOException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("media[]", retrieveMediaId(imageFile));
        return createBoardPost(text, String.valueOf(targetUid), parameters);
    }

    /**
     * Removes a board post
     * @param post Post to remove
     */
    public void removeBoardPost(BoardPost post) {
        new HttpConnection("https://www.minecraft-italia.it/board/post_remove").connect()
                .data("pid", String.valueOf(post.getId()))
                .post();
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

    /**
     * Removes a board comment
     * @param comment Comment to remove
     */
    public void removeBoardComment(BoardPostComment comment) {
        new HttpConnection("https://www.minecraft-italia.it/board/comment_remove").connect()
                .data("cid", String.valueOf(comment.getId()))
                .post();
    }

    /**
     * Removes a board reply
     * @param reply Reply to remove
     */
    public void removeBoardReply(BoardPostReply reply) {
        new HttpConnection("https://www.minecraft-italia.it/board/comment_remove").connect()
                .data("cid", String.valueOf(reply.getId()))
                .post();
    }

    /**
     * @return 3 suggested friends
     */
    public UnparsedUser[] getSuggestedFriends() {
        UnparsedUser[] users = new UnparsedUser[3];
        Document document = new HttpConnection("https://www.minecraft-italia.it/board/suggested_friends").connect().post();
        JSONObject object = new JSONParser(document).parse();
        JSONObject data = (JSONObject) object.get("data");
        String json = (String) data.get("friends");
        JSONArray parsed = new JSONParser(json, true).parseArray();
        for(int i = 0; i < parsed.size(); i++) {
            users[i] = new UnparsedUser(((JSONObject) parsed.get(i)).get("username").toString());
        }
        return users;
    }
}
