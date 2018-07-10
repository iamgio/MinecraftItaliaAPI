package eu.iamgio.mcitaliaapi;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
import eu.iamgio.mcitaliaapi.json.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Represents an user of Minecraft Italia
 * @author Gio
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class User {

    private Document document;

    private String name;

    private Integer uid;

    private User(String name) {
        this.name = name;
        this.update();
    }

    /**
     * Updates connection
     */
    public void update() {
        this.document = new HttpConnection("https://www.minecraft-italia.it/user/" + name).connect().get();
    }

    private Element getStatisticsRowProperty(int index) {
        return document.getElementsByClass("profile-counts forms").first().child(0).child(index);
    }

    private Element getInfoProperty(String key) throws MinecraftItaliaException {
        Element table = document.getElementsByClass("profile-more-info").first()
                .getElementsByClass("collection-item").first();
        for(Element element : table.getElementsByClass("row")) {
            if(element.child(0).text().equals(key)) {
                return element.child(1);
            }
        }
        throw new MinecraftItaliaException("The user does not have this information.");
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
     * @return User's unique ID
     */
    public int getUid() {
        if(uid == null) {
            uid = Integer.parseInt(document.getElementById("users").attr("data-uid"));
        }
        return uid;
    }

    /**
     * @return <tt>true</tt> if the user is online
     */
    public boolean isOnline() {
        return document.getElementsByClass("profile-info").first().getElementsByClass("color-online").first()
                .text().equals("Online");
    }

    /**
     * @return <tt>true</tt> if the user's profile is private
     */
    public boolean isPrivate() {
        return document.getElementsByClass("forms-content").size() > 0;
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
     * @return <tt>true</tt> if the user has linked a Minecraft account
     */
    public boolean hasMinecraftAccount() {
        return document.getElementsByClass("profile-info").first().getElementsByClass("username").size() > 0;
    }

    /**
     * @return User's Minecraft username
     * @throws MinecraftItaliaException if the user hasn't a linked Minecraft account
     */
    public String getMinecraftUsername() throws MinecraftItaliaException {
        Elements names = document.getElementsByClass("profile-info").first().getElementsByClass("username");
        if(names.size() == 0) throw new MinecraftItaliaException("The user does not have a linked Minecraft account.");
        return names.first().text();
    }

    /**
     * @return User's Minecraft skin (head)
     * @throws MinecraftItaliaException if the user hasn't a linked Minecraft account
     */
    public String getMinecraftSkinUrl() throws MinecraftItaliaException {
        if(!hasMinecraftAccount()) throw new MinecraftItaliaException("The user does not have a linked Minecraft account.");
        return document.getElementsByClass("profile-info").first().getElementsByTag("img").first().attr("src");
    }

    /**
     * @param size Size of the image
     * @return User's Minecraft skin (head)
     * @throws MinecraftItaliaException if the user hasn't a linked Minecraft account
     */
    public String getMinecraftSkinUrl(int size) throws MinecraftItaliaException {
        String url = getMinecraftSkinUrl();
        int substringStart = "https://minepic.org/avatar/".length();
        int substringEnd = substringStart + 2;
        return url.substring(0, substringStart) + size + url.substring(substringEnd, url.length());
    }

    public String getBio() throws MinecraftItaliaException {
        Elements bio = document.getElementsByClass("profile-bio");
        if(bio.size() == 0) throw new MinecraftItaliaException("The user does not have this information.");
        return bio.first().getElementsByClass("collection-item").first().ownText();
    }

    /**
     * @return User's average messages-per-day count
     * @throws MinecraftItaliaException if the user hasn't this information saved
     */
    public float getMessagesPerDayCount() throws MinecraftItaliaException {
        Element element = getInfoProperty("Messaggi");
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(element.child(0).attr("data-timestamp") + "000"));
        return calendar.getTime();
    }

    /**
     * @return Time the user has spent online, unparsed
     * @throws MinecraftItaliaException if the user hasn't this information saved
     */
    public String getRawOnlineTime() throws MinecraftItaliaException {
        return getInfoProperty("Tempo online").text();
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
        return getInfoProperty("Sesso").text().equals("Maschio") ? Gender.MALE : Gender.FEMALE;
    }

    /**
     * @return User's provenance
     * @throws MinecraftItaliaException if the user hasn't this information saved
     */
    public String getProvenance() throws MinecraftItaliaException {
        return getInfoProperty("Provenienza").text();
    }

    /**
     * @return User's badges
     */
    public List<String> getBadges() {
        List<String> badges = new ArrayList<>();
        for(Element element : document.getElementsByClass("badges-container").first().children()) {
            badges.add(element.text());
        }
        return badges;
    }

    /**
     * @return User's social networks as TYPE=url
     */
    public HashMap<SocialNetwork, String> getSocialNetworks() {
        HashMap<SocialNetwork, String> socials = new HashMap<>();
        for(Element element : document.getElementsByClass("social").first().children()) {
            String attr = element.attr("class");
            socials.put(
                    SocialNetwork.valueOf(attr.substring("profile_".length(), attr.length()).toUpperCase()),
                    element.attr("href")
            );
        }
        return socials;
    }

    /**
     * @return User's friends
     * @throws MinecraftItaliaException if an error occurred (common: private profile)
     */
    public List<UnparsedUser> getFriends() throws MinecraftItaliaException {
        List<UnparsedUser> friends = new ArrayList<>();
        String url = "https://www.minecraft-italia.it/board/get_user_friends?filter[uid]=" + getUid() + "&start=0";
        JSONObject object = new JSONParser(url).parse();
        if(object.get("status").toString().equals("error")) throw new MinecraftItaliaException(object.get("descr").toString());
        JSONArray array = (JSONArray) object.get("data");
        for(Object obj : array) {
            friends.add(new UnparsedUser(((JSONObject) obj).get("username").toString()));
        }
        return friends;
    }

    public enum Gender { MALE, FEMALE }

    public enum SocialNetwork {
        WEBSITE, FACEBOOK, GOOGLE_PLUS, YOUTUBE, STEAM, SKYPE, TELEGRAM, PATREON, PAYPAL, TWITCH
    }
}