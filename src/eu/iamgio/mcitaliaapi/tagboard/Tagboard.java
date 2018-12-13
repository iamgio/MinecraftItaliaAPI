package eu.iamgio.mcitaliaapi.tagboard;

import eu.iamgio.mcitaliaapi.connection.json.JSONParser;
import eu.iamgio.mcitaliaapi.user.UnparsedUser;
import eu.iamgio.mcitaliaapi.util.Utils;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents the tagboard
 * @deprecated The tagboard has been deleted from the website
 * @author Gio
 */
@Deprecated
public class Tagboard {

    private static Tagboard instance;

    private Element element;

    private Tagboard() {
        instance = this;
        JSONObject json = new JSONParser("https://www.minecraft-italia.it/forum/xmlhttp.php?action=dvz_sb_get_shouts&from=0").parse();
        this.element = Jsoup.parse(json.get("html").toString());
    }

    /**
     * @return Tagboard messages
     */
    public List<TagboardMessage> getMessages() {
        List<TagboardMessage> messages = new ArrayList<>();
        for(Element entry : element.getElementsByClass("entry")) {
            UnparsedUser user = new UnparsedUser(entry.getElementsByClass("username-inner").first().ownText());
            UnparsedUser target;
            Element targetUserElement = entry.getElementsByClass("private-username").first();
            if(targetUserElement == null) {
                target = null;
            } else {
                target = new UnparsedUser(targetUserElement.ownText());
            }
            String text = entry.getElementsByClass("text").first().ownText();
            long id = Long.parseLong(entry.attr("data-id"));
            long userId = Long.parseLong(entry.getElementsByClass("username").first().attr("data-id"));
            Date date = Utils.getDateByTimestamp(entry.getElementsByClass("date time").first().attr("data-timestamp"));
            messages.add(new TagboardMessage(user, target, id, text, userId, date));
        }
        return messages;
    }

    /**
     * @return Tagboard instance
     */
    public static Tagboard getTagboard() {
        return instance == null ? new Tagboard() : instance;
    }
}
