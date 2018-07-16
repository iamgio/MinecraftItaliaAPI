package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;
import org.jsoup.nodes.Element;

/**
 * Represents a topic listed inside of a forum section
 * @author Gio
 */
public class ListedTopic {

    private String name, iconUrl, url, rawLastReplyDate;
    private UnparsedUser author, lastReplyAuthor;
    private int repliesCount, viewsCount;
    private TopicPrefix prefix;

    ListedTopic(String name, String iconUrl, String url, String rawLastReplyDate, UnparsedUser author, UnparsedUser lastReplyAuthor, int repliesCount, int viewsCount, TopicPrefix prefix) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.url = url;
        this.rawLastReplyDate = rawLastReplyDate;
        this.author = author;
        this.lastReplyAuthor = lastReplyAuthor;
        this.repliesCount = repliesCount;
        this.viewsCount = viewsCount;
        this.prefix = prefix;
    }

    static ListedTopic fromElement(Element element) {
        Element lastReplyElement = element.parent().getElementsByClass("thread-lastpost").first();
        Element titleElement = element.getElementsByClass("thread-title").first();
        Element titleLink = titleElement.getElementsByTag("a").first();
        String name = null;
        String url = Forum.FORUM_URL + titleLink.attr("href").replace("?action=newpost", "");
        String iconUrl = element.getElementsByClass("thread-avatar").first().getElementsByTag("img").first().attr("src");
        UnparsedUser author = new UnparsedUser(element.getElementsByClass("thread-author").first().text());
        Element repliesElement = element.getElementsByClass("replies").first();
        int repliesCount = repliesElement == null ? 0 : Integer.parseInt(repliesElement.getElementsByClass("num").first().text().replace(",", ""));
        Element viewsElement = element.getElementsByClass("views").first();
        int viewsCount = repliesElement == null ? 0 : Integer.parseInt(viewsElement.getElementsByClass("num").first().text().replace(",", ""));
        String rawLastReplyDate = element.getElementsByClass("thread-lastpost-date").first().ownText();
        Element lastReplyAuthorElement = lastReplyElement.getElementsByClass("load-user-box").first();
        UnparsedUser lastReplyAuthor = lastReplyAuthorElement == null ? author : new UnparsedUser(lastReplyAuthorElement.text());
        TopicPrefix prefix = null;
        for(Element span : titleElement.getElementsByTag("span")) {
            if(!span.hasText()) continue;
            if(span.hasAttr("style")) {
                String prefixText = span.text();
                String[] prefixStyleParts = span.attr("style").split(";");
                String prefixColor = prefixStyleParts[0].substring("color: ".length(), prefixStyleParts[0].length());
                prefix = new TopicPrefix(prefixText, prefixColor);
            } else {
                name = span.text();
            }
        }
        return new ListedTopic(name, iconUrl, url, rawLastReplyDate, author, lastReplyAuthor, repliesCount, viewsCount, prefix);
    }

    /**
     * @return Getter for property {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * @return Getter for property {@link #iconUrl}
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * @return Getter for property {@link #url}
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return Getter for property {@link #rawLastReplyDate}
     */
    public String getRawLastReplyDate() {
        return rawLastReplyDate;
    }

    /**
     * @return Getter for property {@link #author}
     */
    public UnparsedUser getAuthor() {
        return author;
    }

    /**
     * @return Getter for property {@link #lastReplyAuthor}
     */
    public UnparsedUser getLastReplyAuthor() {
        return lastReplyAuthor;
    }

    /**
     * @return Getter for property {@link #repliesCount}
     */
    public int getRepliesCount() {
        return repliesCount;
    }

    /**
     * @return Getter for property {@link #viewsCount}
     */
    public int getViewsCount() {
        return viewsCount;
    }

    /**
     * @return Getter for property {@link #prefix}
     */
    public TopicPrefix getPrefix() {
        return prefix;
    }
}
