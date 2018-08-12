package eu.iamgio.mcitaliaapi.user;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * User's panel containing private messages
 * @author Gio
 */
public class UserPrivateMessagesPanel {

    private Document document;

    UserPrivateMessagesPanel(int fid, int page) {
        this.document = new HttpConnection("https://www.minecraft-italia.it/forum/private.php?fid=" + fid + "&page=" + page).connect().get();
    }

    UserPrivateMessagesPanel(int fid) {
        this(fid, 0);
    }

    UserPrivateMessagesPanel(PrivateMessageCategory category, int page) {
        this(category.fid(), page);
    }

    UserPrivateMessagesPanel(PrivateMessageCategory category) {
        this(category, 0);
    }

    /**
     * @return Panel name
     */
    public String getName() {
        return document.getElementsByClass("heading no-margin-top").text();
    }

    /**
     * @return Amount of available pages
     */
    public int getPagesCount() {
        Element pagesElement = document.getElementsByClass("pagination").first();
        if(pagesElement == null) return 1;
        Elements pages = pagesElement.children();
        if(pages.size() <= 1) return 1;
        return Integer.parseInt(pages.get(pages.size() - 2).text());
    }

    /**
     * @return Percentage of used space
     */
    public int getUsedSpacePerc() {
        return Integer.parseInt(document.getElementsByClass("pmspace_text").text().replaceAll("[^\\d]", ""));
    }

    /**
     * @return Message categories as [name, ID]
     */
    public List<Pair<String, Integer>> getMessageCategories() {
        final String s = "https://www.minecraft-italia.it/forum/private.php?fid=";
        List<Pair<String, Integer>> categories = new ArrayList<>();
        for(Element element : document.getElementById("nav-left").getElementsByAttributeValueStarting("href", s)) {
            String url = element.attr("href");
            categories.add(new Pair<>(element.text(), Integer.parseInt(url.substring(s.length(), url.length()))));
        }
        return categories;
    }

    /**
     * @return Private messages (in the selected page)
     */
    public List<ListedPrivateMessage> getPrivateMessages() {
        List<ListedPrivateMessage> messages = new ArrayList<>();
        for(Element element : document.getElementsByClass("pm")) {
            Element titleElement = element.getElementsByClass("pm-title").first();
            String subject = titleElement.text();
            String url = titleElement.getElementsByTag("a").first().attr("href");
            long id = Long.parseLong(url.substring("private.php?action=read&pmid=".length(), url.length()));
            UnparsedUser user = new UnparsedUser(element.getElementsByClass("pm-author").text());
            String rawDate = element.getElementsByClass("pm-lastpost-date").text();
            boolean read = element.getElementsByClass("pm-icon").first().attr("data-tooltip").equals("Messaggi vecchi");
            messages.add(new ListedPrivateMessage(subject, user, id, rawDate, read));
        }
        return messages;
    }
}
