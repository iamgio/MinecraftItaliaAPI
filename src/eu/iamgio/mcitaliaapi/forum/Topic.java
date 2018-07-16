package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.user.UnparsedUser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Represents a forum topic
 * @author Gio
 */
public class Topic {

    private String url, standardUrl;
    private Document document;

    private int page;

    private Topic(String url, int page) {
        this.url = url + "?page=" + (page + 1);
        this.standardUrl = url;
        this.page = page;
        this.update();
    }

    /**
     * Updates connection
     */
    public void update() {
        this.document = new HttpConnection(url).connect().get();
    }

    /**
     * @param url URL of topic
     * @return Topic by URL
     */
    public static Topic fromUrl(String url) {
        return new Topic(url, 0);
    }

    /**
     * @param url URL of topic
     * @param page Start page
     * @return Topic by URL
     */
    public static Topic fromUrl(String url, int page) {
        return new Topic(url, page);
    }

    public List<TopicPost> getPosts(int page) {
        Document document;
        if(page == this.page) {
            document = this.document;
        } else {
            document = new HttpConnection(standardUrl + "?page=" + (page + 1)).connect().get();
        }
        List<TopicPost> posts = new ArrayList<>();
        for(Element postElement : document.getElementsByClass("post")) {
            String pid = postElement.attr("id");
            long id = Long.parseLong(pid.substring("pid_".length() + 1, pid.length()));
            Element authorElement = postElement.getElementsByClass("post_author").first();
            Element bodyElement = postElement.getElementsByClass("post_body").first();
            Element signatureElement = postElement.getElementsByClass("signature").first();
            UnparsedUser user = new UnparsedUser(authorElement.getElementsByClass("username-inner").first().text());
            Element statisticsElement = authorElement.getElementsByClass("author_statistics").first();
            String[] statisticsParts = statisticsElement.ownText().split(" ");
            int usersMessagesCount = Integer.parseInt(statisticsParts[1]);
            String rawRegistrationDate = statisticsParts[statisticsParts.length - 2] + " " + statisticsParts[statisticsParts.length - 1];
            Date registrationDate = null;
            try {
                registrationDate = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH).parse(rawRegistrationDate);
            } catch(ParseException e) {
                e.printStackTrace();
            }
            int usersTopicsCount = Integer.parseInt(statisticsElement.getElementsByClass("postbit_userthreads").first().ownText().replace(",", "").split(" ")[1]);
            String[] likesReceivedParts = statisticsElement.getElementsByClass("postbit_tylreceived").first().ownText().replace(",", "").split(" ");
            int usersLikesReceivedCount = Integer.parseInt(likesReceivedParts[2]);
            int usersLikedPostsCount = Integer.parseInt(likesReceivedParts[4]);
            int usersLikesGivenCount = Integer.parseInt(statisticsElement.getElementsByClass("postbit_tylgiven").first().ownText().replace(",", "").split(" ")[2]);
            List<String> usersBadges = new ArrayList<>();
            for(Element badge : document.getElementsByClass("my-badge-inner")) {
                usersBadges.add(badge.text());
            }
            boolean userOnline = authorElement.getElementsByClass("online-status").first().attr("title").equals("Online");
            List<UnparsedUser> likeGivers = new ArrayList<>();
            Element likeGiversElement = postElement.getElementsByClass("post_controls tyllist").first();
            if(likeGiversElement != null) {
                for(Element link : likeGiversElement.getElementsByClass("tyllist-users").first().getElementsByTag("a")) {
                    likeGivers.add(new UnparsedUser(link.text()));
                }
            }
            String plainText = bodyElement.text();
            String html = bodyElement.html();
            String usersSignatureHtml = signatureElement == null ? "" : signatureElement.html();
            posts.add(new TopicPost(id, plainText, html, user, usersMessagesCount, usersTopicsCount, usersLikesReceivedCount, usersLikedPostsCount, usersLikesGivenCount, usersBadges, rawRegistrationDate, registrationDate, userOnline, likeGivers, usersSignatureHtml));
        }
        return posts;
    }

    public List<TopicPost> getPosts() {
        return getPosts(page);
    }
}
