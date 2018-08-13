package eu.iamgio.mcitaliaapi.home;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.user.UnparsedUser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Represents the home (www.minecraft-italia.it)
 * @author Gio
 */
public class Home {

    private Document document;

    private static Home instance;

    private Home() {
        instance = this;
        this.update();
    }

    /**
     * @return Forum instance
     */
    public static Home getHome() {
        return instance == null ? new Home() : instance;
    }

    /**
     * Updates connection
     */
    public void update() {
        this.document = new HttpConnection("https://www.minecraft-italia.it").connect().get();
    }

    /**
     * @return Articles (available on homepage)
     */
    public List<Article> getArticles() {
        List<Article> feeds = new ArrayList<>();
        for(Element article : document.getElementsByClass("article")) {
            String name = article.getElementsByClass("post-title").first().text();
            String description = article.getElementsByClass("post-body").first().text();
            String url = article.getElementsByClass("post-header").first().attr("href");
            String imageUrl = article.getElementsByClass("post-cover").first().attr("data-background");
            Element categoryElement = article.getElementsByClass("blog-category").first();
            String category = categoryElement.text();
            String categoryUrl = categoryElement.attr("href");
            UnparsedUser author = new UnparsedUser(article.getElementsByClass("author").first().getElementsByTag("a").first().text());
            DateFormat format = new SimpleDateFormat("yyy-MM-dd'T'hh:mm:ss", Locale.ITALIAN);
            Date publishedDate = null, modifiedDate = null;
            try {
                publishedDate = format.parse(article.getElementsByAttributeValue("itemprop", "datePublished").first().attr("content"));
                modifiedDate = format.parse(article.getElementsByAttributeValue("itemprop", "dateModified").first().attr("content"));
            } catch(ParseException ignored) {}
            feeds.add(new Article(name, description, url, imageUrl, category, categoryUrl, author, publishedDate, modifiedDate));
        }
        return feeds;
    }
}
