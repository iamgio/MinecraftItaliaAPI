package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gio
 */
public class ForumSubSection {

    protected String name, url;

    private Document document;

    ForumSubSection(String name, String url) {
        this.name = name;
        this.url = Forum.FORUM_URL + url;
    }

    /**
     * Updates document
     */
    public void update() {
        this.document = new HttpConnection(url).connect().get();
    }

    /**
     * @return Section name
     */
    public String getName() {
        return name;
    }

    /**
     * @return URL to section
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return Amount of pages
     */
    public int getPagesCount() {
        if(document == null) update();
        return Integer.parseInt(document.getElementsByClass("pagination_last").first().ownText());
    }

    public List<ListedTopic> getTopics(int page) {
        Document document;
        if(page == 0) {
            if(this.document == null) update();
            document = this.document;
        } else {
            document = new HttpConnection(url + "?page=" + (page + 1)).connect().get();
        }
        List<ListedTopic> topics = new ArrayList<>();
        for(Element thread : document.getElementsByClass("thread")) {
            topics.add(ListedTopic.fromElement(thread));
        }
        return topics;
    }
}
