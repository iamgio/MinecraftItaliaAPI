package eu.iamgio.mcitaliaapi.home;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.user.UnparsedUser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Date;

/**
 * Represents an article (news)
 * @author Gio
 */
public class Article {

    private Document document;

    private String name, description, url, imageUrl, category, categoryUrl;
    private UnparsedUser author;
    private Date publishedDate, modifiedDate;

    Article(String name, String description, String url, String imageUrl, String category, String categoryUrl, UnparsedUser author, Date publishedDate, Date modifiedDate) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.category = category;
        this.categoryUrl = categoryUrl;
        this.author = author;
        this.publishedDate = publishedDate;
        this.modifiedDate = modifiedDate;
    }

    /**
     * @return Article name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Article description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Article URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return Article cover image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @return Article category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return URL of article category
     */
    public String getCategoryUrl() {
        return categoryUrl;
    }

    /**
     * @return Article author
     */
    public UnparsedUser getAuthor() {
        return author;
    }

    /**
     * @return Date on which the article was published
     */
    public Date getPublishedDate() {
        return publishedDate;
    }

    /**
     * @return Date on which the article was modified
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * @return Article content as HTML
     */
    public String getHtml() {
        if(document == null) document = new HttpConnection(url).connect().get();
        Element body = document.getElementById("post-body");
        for(Element duckspace : body.getElementsByClass("duckspace-left")) {
            duckspace.remove();
        }
        return body.html();
    }
}
