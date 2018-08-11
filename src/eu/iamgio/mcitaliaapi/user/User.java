package eu.iamgio.mcitaliaapi.user;

import eu.iamgio.mcitaliaapi.board.Board;
import eu.iamgio.mcitaliaapi.board.BoardPost;
import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.connection.json.JSONParser;
import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
import eu.iamgio.mcitaliaapi.util.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Represents an user of Minecraft Italia
 * @author Gio
 */
public class User {

    private String url;
    protected Document document;

    private String name;
    private Long uid;

    User(String name) {
        this.name = name;
        this.url = "https://www.minecraft-italia.it/user/" + name;
        this.update();
        this.uid = getUid();
    }

    User(long uid) {
        this.uid = uid;
        this.url = "https://www.minecraft-italia.it/user/id/" + uid;
        this.update();
        this.name = getName();
    }

    /**
     * Updates connection
     */
    public void update() {
        this.document = new HttpConnection(url).connect().get();
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
     * @return User by name. <tt>null</tt> if it does not exist
     */
    public static User fromName(String name) {
        try {
            return new User(name);
        } catch(NullPointerException e) {
            return null;
        }
    }

    /**
     * @param uid User's unique ID
     * @return User by UID. <tt>null</tt> if it does not exist
     */
    public static User fromUid(long uid) {
        try {
            return new User(uid);
        } catch(NullPointerException e) {
            return null;
        }
    }

    /**
     * Logins
     * @param password Password
     * @return New login session
     */
    public Login login(String password) {
        HttpConnection connection = new HttpConnection("https://www.minecraft-italia.it/forum/member.php").connect();
        Document documentDummy = connection.data("action", "do_login").post();
        String postKey = documentDummy.select("input[name=my_post_key]").attr("value");
        Document document = connection
                .data("action", "do_login")
                .data("my_post_key", postKey)
                .data("password", password)
                .data("remember", "yes")
                .data("submit", "Accedi")
                .data("url", "//www.minecraft-italia.it/forum")
                .data("username", name)
                .post();
        byte status = 0;
        if(document.getElementById("modal-2fa") != null) {
            status = 1;
        } else if(document.getElementById("dropdown-profile-menu") == null) {
            status = 2;
        }
        return new Login(new LoggedUser(this.name), connection, status);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && name.equalsIgnoreCase(((User) obj).name);
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
    public long getUid() {
        if(uid == null) {
            uid = Long.parseLong(document.getElementById("users").attr("data-uid"));
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
     * @return User's Minecraft username. <tt>null</tt> if the user hasn't a linked Minecraft account
     */
    public String getMinecraftUsername() {
        Elements names = document.getElementsByClass("profile-info").first().getElementsByClass("username");
        if(names.size() == 0) return null;
        return names.first().text();
    }

    /**
     * @return User's Minecraft skin (head). <tt>null</tt> if the user hasn't a linked Minecraft account
     */
    public String getMinecraftSkinUrl() {
        if(!hasMinecraftAccount()) return null;
        return document.getElementsByClass("profile-info").first().getElementsByTag("img").first().attr("src");
    }

    /**
     * @param size Size of the image
     * @return User's Minecraft skin (head). <tt>null</tt> if the user hasn't a linked Minecraft account
     */
    public String getMinecraftSkinUrl(int size) {
        String url = getMinecraftSkinUrl();
        if(url == null) return null;
        int substringStart = "https://minepic.org/avatar/".length();
        int substringEnd = substringStart + 2;
        return url.substring(0, substringStart) + size + url.substring(substringEnd, url.length());
    }

    /**
     * @return User's biography. <tt>null</tt> if the user hasn't a saved biography
     */
    public String getBio() {
        Elements bio = document.getElementsByClass("profile-bio");
        if(bio.size() == 0) return null;
        return bio.first().getElementsByClass("collection-item").first().ownText();
    }

    /**
     * @return User's average messages-per-day count. <tt>null</tt> if this information cannot be retrieved
     */
    public Float getMessagesPerDayCount() {
        Element element = getInfoProperty("Messaggi");
        if(element == null) return null;
        try {
            return Float.parseFloat(element.text().split("\\(")[1].split(" ")[0]);
        } catch(ArrayIndexOutOfBoundsException e) {
            return 0F;
        }
    }

    /**
     * @return Date of user's registration. <tt>null</tt> if this information cannot be retrieved
     */
    public Date getRegistrationDate() {
        Element element = getInfoProperty("Iscritto dal");
        if(element == null) return null;
        return Utils.getDateByTimestamp(element.child(0).attr("data-timestamp"));
    }

    /**
     * @return Date of user's last visit. <tt>null</tt> if this information cannot be retrieved
     */
    public Date getLastVisitDate() {
        Element element = getInfoProperty("Ultima visita");
        if(element == null) return null;
        return Utils.getDateByTimestamp(element.child(0).attr("data-timestamp"));
    }

    /**
     * @return Time the user has spent online, unparsed. <tt>null</tt> if this information cannot be retrieved
     */
    public String getRawOnlineTime() {
        Element element = getInfoProperty("Tempo online");
        return element == null ? null : element.text();
    }

    /**
     * @return Time the user has spent online in millis. <tt>null</tt> if this information cannot be retrieved
     */
    public Long getOnlineTime() {
        String raw = getRawOnlineTime();
        if(raw == null) return null;
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
     * @return User's gender. <tt>null</tt> if the user hasn't specified this value
     */
    public Gender getGender() {
        Element element = getInfoProperty("Sesso");
        return element == null ? null : element.text().equals("Maschio") ? Gender.MALE : Gender.FEMALE;
    }

    /**
     * @return User's provenance. <tt>null</tt> if the user hasn't specified this value
     */
    public String getProvenance() {
        Element element = getInfoProperty("Provenienza");
        return element == null ? null : element.text();
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

    /**
     * @return User's first 15 board posts
     */
    public List<BoardPost> getBoardPosts() {
        return Board.getBoardPosts("https://www.minecraft-italia.it/board/get_posts?filter[type]=private&filter[uid]=" + getUid() + "&start=0");
    }

    /**
     * @param start Start post
     * @return User's 15 board posts after <tt>start</tt>
     */
    public List<BoardPost> getBoardPosts(BoardPost start) {
        return Board.getBoardPosts("https://www.minecraft-italia.it/board/get_posts?filter[type]=private&filter[uid]=" + getUid() + "&start=" + start.getId());
    }

    /**
     * @return First 15 posts where the user is target
     */
    public List<BoardPost> getTargetedBoardPosts() {
        return Board.getBoardPosts("https://www.minecraft-italia.it/board/get_posts?filter[type]=private-with-replies&filter[uid]=" + getUid() + "&start=0");
    }

    /**
     * @param start Start post
     * @return 15 posts afer <tt>start</tt> where the user is target
     */
    public List<BoardPost> getTargetedBoardPosts(BoardPost start) {
        return Board.getBoardPosts("https://www.minecraft-italia.it/board/get_posts?filter[type]=private-with-replies&filter[uid]=" + getUid() + "&start=" + start.getId());
    }

    /**
     * @return Servers owned by the user as [name, string id, address, image url]
     * @throws MinecraftItaliaException if an error occurred
     */
    public List<String[]> getServers() throws MinecraftItaliaException {
        JSONObject json = new JSONParser("https://www.minecraft-italia.it/user/index_ajax/" + name + "/servers").parse();
        if(json.get("status").toString().equals("error")) throw new MinecraftItaliaException(json.get("descr").toString());
        JSONObject data = (JSONObject) json.get("data");
        Document document = Jsoup.parse(data.get("html").toString());
        List<String[]> servers = new ArrayList<>();
        for(Element serverElement : document.getElementsByClass("server-box")) {
            Element serverImage = serverElement.getElementsByClass("server-box-image").first();
            String style = serverImage.attr("style");
            String imageUrl = style.substring("background-image: url('".length(), style.length() - 3);
            String[] urlParts = serverImage.attr("href").split("/");
            String stringId = urlParts[urlParts.length - 1];
            String name = serverElement.getElementsByClass("server-box-title").first().text();
            String address = serverElement.getElementsByClass("server-box-ip").first().text();
            servers.add(new String[] {name, stringId, address, imageUrl});
        }
        return servers;
    }

    public enum Gender { MALE, FEMALE }

    public enum SocialNetwork {
        WEBSITE, FACEBOOK, GOOGLE_PLUS, YOUTUBE, STEAM, SKYPE, TELEGRAM, PATREON, PAYPAL, TWITCH
    }
}