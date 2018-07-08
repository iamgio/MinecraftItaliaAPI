package eu.iamgio.mcitaliaapi;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.exception.McItaliaRuntimeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Gio
 */
public class User {

    private Document document;

    private User(String name) {
        this.document = new HttpConnection("https://www.minecraft-italia.it/user/" + name).connect().get();
    }

    private Element getStatisticsRowProperty(int index) {
        return document.getElementsByClass("profile-counts forms").first().child(0).child(index);
    }

    private String getInfoProperty(String key) {
        Element table = document.getElementsByClass("profile-more-info").first()
                .getElementsByClass("collection-item").first();
        for(Element element : table.getElementsByClass("row")) {
            if(element.child(0).text().equals(key)) return element.child(1).text();
        }
        return null;
    }

    /**
     * @param name User's name
     * @return Minecraft Italia user by name
     */
    public static User getUser(String name) {
        return new User(name);
    }

    /**
     * @return User's name
     */
    public String getName() {
        return document.getElementsByClass("username").first().text();
    }

    public String getAvatarUrl() {
        return document.getElementsByClass("avatar").first().getElementsByTag("img").first().attr("src");
    }

    /**
     * @return User's messages count
     */
    public int getMessagesCount() {
        return Integer.parseInt(getStatisticsRowProperty(0).ownText());
    }

    /**
     * @return User's reputation
     */
    public int getReputation() {
        return Integer.parseInt(getStatisticsRowProperty(1).child(1).text());
    }

    /**
     * @return User's resources count
     */
    public int getResourcesCount() {
        return Integer.parseInt(getStatisticsRowProperty(2).ownText());
    }

    /**
     * @return User's revisions count
     */
    public int getRevisionsCount() {
        return Integer.parseInt(getStatisticsRowProperty(3).ownText());
    }

    /**
     * @return User's followed users count
     */
    public int getFollowedCount() {
        return Integer.parseInt(document.getElementsByClass("col-sm-8").first().getElementsByTag("b").get(0).ownText());
    }

    /**
     * @return User's followers count
     */
    public int getFollowersCount() {
        return Integer.parseInt(document.getElementsByClass("col-sm-8").first().getElementsByTag("b").get(1).ownText());
    }

    /**
     * @return User's average messages-per-day count
     * @throws McItaliaRuntimeException if the user hasn't this information saved
     */
    public float getMessagesPerDayCount() throws McItaliaRuntimeException {
        String s = getInfoProperty("Messaggi");
        if(s == null) throw new McItaliaRuntimeException("User hasn't this info saved.");
        return Float.parseFloat(s.split("\\(")[1].split(" ")[0]);
    }
}