package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.connection.json.JSONParser;
import eu.iamgio.mcitaliaapi.user.UnparsedUser;
import eu.iamgio.mcitaliaapi.util.Pair;
import eu.iamgio.mcitaliaapi.util.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents the forum
 * @author Gio
 */
public class Forum {

    public static String FORUM_URL = "https://www.minecraft-italia.it/forum/";

    private Document document;

    private static Forum instance;

    private Forum() {
        instance = this;
        this.update();
    }

    /**
     * @return Forum instance
     */
    public static Forum getForum() {
        return instance == null ? new Forum() : instance;
    }

    /**
     * Updates connection
     */
    public void update() {
        this.document = new HttpConnection(Forum.FORUM_URL).connect().get();
    }

    /**
     * @return Section containers
     */
    public List<ForumSectionContainer> getSectionContainers() {
        List<ForumSectionContainer> containers = new ArrayList<>();
        Elements containersElements = document.getElementsByClass("forum-section-title forumbit-head-seaction-title");
        for(int i = 0; i < containersElements.size() - 1; i++) {
            Element containerElement = containersElements.get(i);
            Element link = containerElement.getElementsByTag("a").first();
            String name = link.ownText();
            String url = FORUM_URL + link.attr("href");
            Element div = containerElement.parent().parent();
            containers.add(new ForumSectionContainer(name, url, div));
        }
        return containers;
    }

    /**
     * @return Last 30 topics
     */
    public List<LastTopic> getLastTopics() {
        JSONObject object = new JSONParser("https://www.minecraft-italia.it/api/new_topics").parse();
        List<LastTopic> lastTopics = new ArrayList<>();
        for(Object topicObj : (JSONArray) object.get("topics")) {
            JSONObject topicJson = (JSONObject) topicObj;
            String name = topicJson.get("thread_subject").toString();
            String url = topicJson.get("thread_url").toString();
            String firstMessageText = topicJson.get("thread_message").toString();
            UnparsedUser author = new UnparsedUser(topicJson.get("author_name").toString());
            long id = (long) topicJson.get("thread_id");
            long authorUid = (long) topicJson.get("author_uid");
            ForumSubSection section = new ForumSubSection(topicJson.get("forum_name").toString(), topicJson.get("forum_url").toString());
            Date date = Utils.getDateByTimestamp(topicJson.get("date_timestamp").toString());
            lastTopics.add(new LastTopic(name, url, firstMessageText, author, id, authorUid, section, date));
        }
        return lastTopics;
    }

    /**
     * @return Total registered users
     */
    public int getTotalUsersCount() {
        return Integer.parseInt(document.getElementsByClass("row_numbers").first().child(0).ownText().replace(".", ""));
    }

    /**
     * @return Total topics opened
     */
    public int getTotalTopicsCount() {
        return Integer.parseInt(document.getElementsByClass("row_numbers").first().child(1).ownText().replace(".", ""));
    }

    /**
     * @return Total messages sent
     */
    public int getTotalMessagesCount() {
        return Integer.parseInt(document.getElementsByClass("row_numbers").first().child(2).ownText().replace(".", ""));
    }

    /**
     * @return First 50 online users
     */
    public List<UnparsedUser> getOnlineUsers() {
        List<UnparsedUser> users = new ArrayList<>();
        Element div = document.getElementById("boardstats_e").getElementsByClass("trow1").first();
        for(Element userElement : div.getElementsByClass("username-inner")) {
            users.add(new UnparsedUser(userElement.ownText()));
        }
        return users;
    }

    /**
     * @return Online users count
     */
    public int getOnlineUsersCount() {
        return Integer.parseInt(document.getElementById("boardstats_e").getElementsByClass("trow1").first().ownText().split(" ")[0]);
    }

    /**
     * @return Today's online users count
     */
    public int getTodayOnlineUsersCount() {
        return Integer.parseInt(document.getElementById("boardstats_e").getElementsByClass("trow1").get(1).ownText().split(" ")[0]);
    }

    /**
     * @return Today's birthdays as [user, age]
     */
    public List<Pair<UnparsedUser, Integer>> getTodaysBirthdays() {
        List<Pair<UnparsedUser, Integer>> birthdays = new ArrayList<>();
        String raw = document.getElementById("boardstats_e").getElementsByClass("trow1").get(2).text();
        for(String part : raw.split(", ")) {
            String[] subparts = part.split(" ");
            birthdays.add(new Pair<>(new UnparsedUser(subparts[0]), Integer.parseInt(subparts[1].substring(1, subparts[1].length() - 1))));
        }
        return birthdays;
    }

    /**
     * @return Newest registered user
     */
    public UnparsedUser getNewestUser() {
        return new UnparsedUser(document.getElementById("boardstats_e").getElementsByClass("trow1").get(3).getElementsByTag("a").first().ownText());
    }
}
