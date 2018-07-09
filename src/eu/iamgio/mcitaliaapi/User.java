package eu.iamgio.mcitaliaapi;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents an user of Minecraft Italia
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

    private Element getInfoProperty(String key) {
        Element table = document.getElementsByClass("profile-more-info").first()
                .getElementsByClass("collection-item").first();
        for(Element element : table.getElementsByClass("row")) {
            if(element.child(0).text().equals(key)) {
                return element.child(1);
            }
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

    /**
     * @return URL of user's avatar
     */
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
    public int getReputationScore() {
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
     * @throws MinecraftItaliaException if the user hasn't this information saved
     */
    public float getMessagesPerDayCount() throws MinecraftItaliaException {
        Element element = getInfoProperty("Messaggi");
        if(element == null) throw new MinecraftItaliaException(MinecraftItaliaException.NO_INFO);
        try {
            return Float.parseFloat(element.text().split("\\(")[1].split(" ")[0]);
        } catch(ArrayIndexOutOfBoundsException e) {
            return 0F;
        }
    }

    /**
     * @return Date of user's registration
     * @throws MinecraftItaliaException if the user hasn't this information saved
     */
    public Date getRegistrationDate() throws MinecraftItaliaException {
        Element element = getInfoProperty("Iscritto dal");
        if(element == null) throw new MinecraftItaliaException(MinecraftItaliaException.NO_INFO);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(element.child(0).attr("data-timestamp") + "000"));
        return calendar.getTime();
    }

    /**
     * @return Date of user's last visit
     * @throws MinecraftItaliaException if the user hasn't this information saved
     */
    public Date getLastVisitDate() throws MinecraftItaliaException {
        Element element = getInfoProperty("Ultima visita");
        if(element == null) throw new MinecraftItaliaException(MinecraftItaliaException.NO_INFO);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(element.child(0).attr("data-timestamp") + "000"));
        return calendar.getTime();
    }

    /**
     * @return Time the user has spent online, unparsed
     * @throws MinecraftItaliaException if the user hasn't this information saved
     */
    public String getRawOnlineTime() throws MinecraftItaliaException {
        Element element = getInfoProperty("Tempo online");
        if(element == null) throw new MinecraftItaliaException(MinecraftItaliaException.NO_INFO);
        return element.text();
    }

    /**
     * @return Time the user has spent online in millis
     * @throws MinecraftItaliaException if the user hasn't this information saved
     */
    public long getOnlineTime() throws MinecraftItaliaException {
        String raw = getRawOnlineTime();
        String[] parts = raw.split(", ");
        long time = 0L;
        for(String part : parts) {
            String[] subparts = part.split(" ");
            short amount = Short.parseShort(subparts[0]);
            String type = subparts[1];
            switch(type) {
                case "secondi":
                case "secondo":
                    time += amount * 1000;
                    break;
                case "minuti":
                case "minuto":
                    time += amount * 1000 * 60;
                    break;
                case "ore":
                case "ora":
                    time += amount * 1000 * 60 * 60;
                    break;
                case "giorni":
                case "giorno":
                    time += amount * 1000 * 60 * 60 * 24;
                    break;
                case "settimane":
                case "settimana":
                    time += amount * 1000 * 60 * 60 * 24 * 7;
                    break;
                case "anni":
                case "anno":
                    time += amount * 1000 * 60 * 60 * 24 * 365;
                    break;
            }
        }
        return time;
    }

    /**
     * @return User's gender
     * @throws MinecraftItaliaException if the user hasn't this information saved
     */
    public Gender getGender() throws MinecraftItaliaException {
        Element element = getInfoProperty("Sesso");
        if(element == null) throw new MinecraftItaliaException(MinecraftItaliaException.NO_INFO);
        return element.text().equals("Maschio") ? Gender.MALE : Gender.FEMALE;
    }

    /**
     * @return User's provenance
     * @throws MinecraftItaliaException if the user hasn't this information saved
     */
    public String getProvenance() throws MinecraftItaliaException {
        Element element = getInfoProperty("Provenienza");
        if(element == null) throw new MinecraftItaliaException(MinecraftItaliaException.NO_INFO);
        return element.text();
    }

    public enum Gender { MALE, FEMALE }
}