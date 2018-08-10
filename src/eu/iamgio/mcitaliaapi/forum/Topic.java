package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
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

    private String url, standardUrl, postHash;
    private Document document;

    private long id;

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

    /**
     * @return Topic ID
     */
    public long getId() {
        if(id == 0) {
            id = Long.parseLong(document.select("input[name=tid]").attr("value"));
        }
        return id;
    }

    /**
     * @return Post hash of topic
     * @throws MinecraftItaliaException if there isn't any logged user
     */
    public String getPostHash() throws MinecraftItaliaException {
        if(postHash == null) {
            Element postHashElement = document.getElementById("posthash");
            if(postHashElement == null) throw new MinecraftItaliaException("There isn't any logged user: could not find post hash.");
            postHash = postHashElement.attr("value");
        }
        return postHash;
    }

    /**
     * @return Subject of new replies to the topic
     * @throws MinecraftItaliaException if there isn't any logged user
     */
    public String getReplySubject() throws MinecraftItaliaException {
        Element subjectElement = document.select("input[name=subject]").first();
        if(subjectElement == null) throw new MinecraftItaliaException("There isn't any logged user: could not find subject.");
        return subjectElement.attr("value");
    }

    /**
     * @return Section of the topic
     */
    public ForumSubSection getSection() {
        if(document == null) update();
        Element item = document.getElementsByAttributeValue("itemprop", "itemListElement").last().child(0);
        return new ForumSubSection(item.ownText(), item.attr("href"));
    }

    /**
     * @param page Page of the topic
     * @return Posts of the topic in the selected page
     */
    public List<TopicPost> getPosts(int page) {
        Document document;
        if(page == this.page) {
            document = this.document;
        } else {
            document = new HttpConnection(standardUrl + "?page=" + (page + 1)).connect().get();
        }
        List<TopicPost> posts = new ArrayList<>();
        for(Element postElement : document.getElementsByClass("post")) {
            String pid = postElement.attr("data-post-id");
            long id = pid.isEmpty() ? -1 : Long.parseLong(pid);
            Element authorElement = postElement.getElementsByClass("post_author").first();
            Element bodyElement = postElement.getElementsByClass("post_body").first();
            Element signatureElement = postElement.getElementsByClass("signature").first();
            UnparsedUser user = new UnparsedUser(authorElement.getElementsByClass("username-inner").first().text());
            Element statisticsElement = authorElement.getElementsByClass("author_statistics").first();
            String[] statisticsParts = statisticsElement.ownText().split(" ");
            int userMessagesCount = Integer.parseInt(statisticsParts[1].replace(",", ""));
            String rawRegistrationDate = statisticsParts[statisticsParts.length - 2] + " " + statisticsParts[statisticsParts.length - 1];
            Date registrationDate = null;
            try {
                registrationDate = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH).parse(rawRegistrationDate);
            } catch(ParseException e) {
                e.printStackTrace();
            }
            int userTopicsCount = Integer.parseInt(statisticsElement.getElementsByClass("postbit_userthreads").first().ownText().replace(",", "").split(" ")[1]);
            String[] likesReceivedParts = statisticsElement.getElementsByClass("postbit_tylreceived").first().ownText().replace(",", "").split(" ");
            int userLikesReceivedCount = Integer.parseInt(likesReceivedParts[2]);
            int userLikedPostsCount = Integer.parseInt(likesReceivedParts[4]);
            int userLikesGivenCount = Integer.parseInt(statisticsElement.getElementsByClass("postbit_tylgiven").first().ownText().replace(",", "").split(" ")[2]);
            List<String> userBadges = new ArrayList<>();
            for(Element badge : document.getElementsByClass("my-badge-inner")) {
                userBadges.add(badge.text());
            }
            String userAvatarUrl = authorElement.getElementsByClass("author_avatar").first().getElementsByTag("img").first().attr("src");
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
            String userSignatureHtml = signatureElement == null ? "" : signatureElement.html();
            posts.add(new TopicPost(id, plainText, html, user, userMessagesCount, userTopicsCount, userLikesReceivedCount, userLikedPostsCount, userLikesGivenCount, userBadges, rawRegistrationDate, registrationDate, userAvatarUrl, userOnline, likeGivers, userSignatureHtml));
        }
        return posts;
    }

    /**
     * @return <tt>true</tt> if the topic is locked
     */
    public boolean isLocked() {
        return document.getElementsByClass("mdi-lock-outline").size() > 0;
    }

    /**
     * @return Posts of the topic inside of page n.{@link #page}
     */
    public List<TopicPost> getPosts() {
        return getPosts(page);
    }

    /**
     * @return <tt>true</tt> if the topic has a poll
     */
    public boolean hasPoll() {
        return document.getElementsByClass("tborder tfixed").size() > 0;
    }

    /**
     * @return Poll of the topic
     * @throws MinecraftItaliaException If there isn't an ongoing poll
     */
    public TopicPoll getPoll() throws MinecraftItaliaException {
        if(!hasPoll()) throw new MinecraftItaliaException("There isn't an ongoing poll.");
        String resultsUrl = document.getElementsContainingOwnText("Mostra risultato").first().attr("href");
        Document document = new HttpConnection(Forum.FORUM_URL + resultsUrl).connect().get();
        Element tbody = document.getElementsByTag("tbody").first();
        List<TopicPollMember> members = new ArrayList<>();
        for(Element element : tbody.getElementsByTag("tr")) {
            if(element.child(0).className().equals("tfoot")) continue;
            String name = element.child(0).ownText();
            int count = Integer.parseInt(element.child(2).text());
            double perc = Double.parseDouble(element.child(3).ownText().replace("%", ""));
            List<UnparsedUser> users = new ArrayList<>();
            for(Element userElement : element.getElementsByTag("a")) {
                if(userElement.attr("href").startsWith("https://www.minecraft-italia.it/forum/u-")) {
                    users.add(new UnparsedUser(userElement.ownText()));
                }
            }
            members.add(new TopicPollMember(name, count, perc, users));
        }
        int count = Integer.parseInt(document.getElementsByClass("tfoot").get(1).text().split(" ")[0]);
        long id = Long.parseLong(resultsUrl.substring(33, resultsUrl.length()));
        return new TopicPoll(members, count, id, isLocked());
    }
}
