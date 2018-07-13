package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a forum section container
 * @author Gio
 */
public class ForumSectionContainer {

    private String name, url;
    private Element div;

    private List<ForumSection> sections;

    ForumSectionContainer(String name, String url, Element div) {
        this.name = name;
        this.url = url;
        this.div = div;
    }

    /**
     * @return Container name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Container URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return Forum sections
     */
    public List<ForumSection> getSections() {
        if(sections == null) {
            List<ForumSection> sections = new ArrayList<>();
            for(Element row : div.getElementsByClass("forumbit-row")) {
                String name = row.getElementsByTag("strong").first().text();
                String url = row.getElementsByTag("a").first().attr("href");
                String description = row.getElementsByClass("smalltext").first().text();
                String imageUrl = row.getElementsByTag("img").first().attr("src");
                Element counts = row.getElementsByClass("count-topics").first();
                int discussionsCount = Integer.parseInt(counts.getElementsByClass("threads").first().getElementsByClass("num").first().text().replace(",", ""));
                int messagesCount = Integer.parseInt(counts.getElementsByClass("posts").first().getElementsByClass("num").first().text().replace(",", ""));
                Element viewerElement = counts.getElementsByClass("viewers").first();
                int viewingNowCount = viewerElement == null ? 0 : Integer.parseInt(viewerElement.getElementsByClass("num").first().text().replace(",", ""));
                Element lastPostRow = row.getElementsByClass("forumbit-lastpost").first();
                String lastPostTitle = lastPostRow.getElementsByClass("topic-link").first().text();
                UnparsedUser lastPostUser = new UnparsedUser(lastPostRow.getElementsByClass("last-post-author").text());
                Element clamp = lastPostRow.getElementsByClass("clamp").first();
                String rawLastPostDate = clamp.getElementsByTag("span").attr("title") + clamp.ownText();
                List<ForumSubSection> subsections = new ArrayList<>();
                for(Element subsection : row.getElementsByClass("row-subforums")) {
                    Element link = subsection.getElementsByTag("a").first();
                    subsections.add(new ForumSubSection(link.ownText(), link.attr("href")));
                }
                sections.add(new ForumSection(name, url, description, imageUrl, discussionsCount, messagesCount, viewingNowCount, lastPostTitle, lastPostUser, subsections));
            }
            this.sections = sections;
        }
        return sections;
    }
}
