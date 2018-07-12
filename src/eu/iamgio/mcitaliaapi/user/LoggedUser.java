package eu.iamgio.mcitaliaapi.user;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import org.jsoup.nodes.Document;

import java.util.Map;

/**
 * Represent a logged user
 * @author Gio
 */
public class LoggedUser extends User {

    private Document document;

    LoggedUser(String name, Map<String, String> cookies, Document document) {
        super(name);
        this.cookies = cookies;
        this.document = document;
    }

    public void sendTagboardMessage(String text) {
        new HttpConnection("https://www.minecraft-italia.it/forum/xmlhttp.php?action=dvz_sb_shout&text=" + text + "&key=6d05c372810cfbab8690ae1b2417d844").connect()
                .cookies(cookies)
                .post();
    }

    public void createBoardPost(String text) {
        new HttpConnection("https://www.minecraft-italia.it/board/post_add").connect()
                .data("content", text)
                .data("uid_to", String.valueOf(getUid()))
                .data("url_preview", "")
                .cookies(cookies)
                .post();
    }
}
