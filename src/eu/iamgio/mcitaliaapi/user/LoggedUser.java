package eu.iamgio.mcitaliaapi.user;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
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

    public void sendTagboardMessage(String text) {
        new HttpConnection("https://www.minecraft-italia.it/forum/xmlhttp.php?action=dvz_sb_shout&text=" + text + "&key=" + getPostKey()).connect()
                .post();
    }

    public void createBoardPost(String text) {
        new HttpConnection("https://www.minecraft-italia.it/board/post_add").connect()
                .data("content", text)
                .data("uid_to", String.valueOf(getUid()))
                .data("url_preview", "")
                .post();
    }
}
