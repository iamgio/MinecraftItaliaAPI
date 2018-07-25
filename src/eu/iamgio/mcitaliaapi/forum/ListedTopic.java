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
    private boolean announcement, pinned, poll;

    ListedTopic(String name, String iconUrl, String url, String rawLastReplyDate, UnparsedUser author, UnparsedUser lastReplyAuthor, int repliesCount, int viewsCount, TopicPrefix prefix, boolean announcement, boolean pinned, boolean poll) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.url = url;
        this.rawLastReplyDate = rawLastReplyDate;
        this.author = author;
        this.lastReplyAuthor = lastReplyAuthor;
        this.repliesCount = repliesCount;
        this.viewsCount = viewsCount;
        this.prefix = prefix;
        this.announcement = announcement;
        this.pinned = pinned;
        this.poll = poll;
    }

    static ListedTopic fromElement(Element element) {
        Element lastReplyElement = element.parent().getElementsByClass("thread-lastpost").first();
        Element titleLink = null;
        for(Element link : element.getElementsByTag("a")) {
            if(link.hasText()) {
                titleLink = link;
                break;
            }
        }
        assert titleLink != null;
        String name = titleLink.text();
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
        for(Element span : element.getElementsByClass("thread-title").first().getElementsByTag("span")) {
            if(!span.hasText() || !span.text().startsWith("[") || !span.hasAttr("style")) continue;
            String prefixText = span.text();
            String[] prefixStyleParts = span.attr("style").split(";");
            String prefixColor = prefixStyleParts[0].substring("color: ".length(), prefixStyleParts[0].length());
            prefix = new TopicPrefix(prefixText, prefixColor);
        }
        boolean announcement = false, pinned = false;
        if(element.parent().className().equals("hidden-xs")) {
            for(Element div : element.parent().getElementsByTag("div")) {
                if(!div.hasAttr("class")) {
                    if(!announcement && !pinned) {
                        announcement = true;
                    } else if(announcement) {
                        announcement = false;
                        pinned = true;
                    }
                }
                if(div.equals(element)) {
                    break;
                }
            }
        }
        boolean poll = element.getElementsByClass("thread-title").first().ownText().startsWith("Sondaggio:");
        return new ListedTopic(name, iconUrl, url, rawLastReplyDate, author, lastReplyAuthor, repliesCount, viewsCount, prefix, announcement, pinned, poll);
    }

    /**
     * @return Topic name
     */
    public String getName() {
        return name;
    }

    /**
     * @return URL of the icon of topic
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * @return URL of topic
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return Raw date of last reply to this topic
     */
    public String getRawLastReplyDate() {
        return rawLastReplyDate;
    }

    /**
     * @return Author of topic
     */
    public UnparsedUser getAuthor() {
        return author;
    }

    /**
     * @return Author of last reply of this topic
     */
    public UnparsedUser getLastReplyAuthor() {
        return lastReplyAuthor;
    }

    /**
     * @return Amount of replies
     */
    public int getRepliesCount() {
        return repliesCount;
    }

    /**
     * @return Amount of views
     */
    public int getViewsCount() {
        return viewsCount;
    }

    /**
     * @return Prefix of topic
     */
    public TopicPrefix getPrefix() {
        return prefix;
    }

    /**
     * @return <tt>true</tt> if the topic has prefix
     */
    public boolean hasPrefix() {
        return prefix != null;
    }

    /**
     * @return <tt>true</tt> if the topic is an announcement by the Minecraft Italia staff
     */
    public boolean isAnnouncement() {
        return announcement;
    }

    /**
     * @return <tt>true</tt> if the topic was pinned in its section
     */
    public boolean isPinned() {
        return pinned;
    }

    /**
     * @return <tt>true</tt> if the topic has poll
     */
    public boolean hasPoll() {
        return poll;
    }

    /**
     * @return Listed topic to actual topic
     */
    public Topic toTopic() {
        return Topic.fromUrl(url);
    }

    /**
     * @param page Start page
     * @return Listed topic to actual topic
     */
    public Topic toTopic(int page) {
        return Topic.fromUrl(url, page);
    }
}
