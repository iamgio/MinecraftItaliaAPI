package eu.iamgio.mcitaliaapi.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Represents a listed server inside of serverlist
 * @author Gio
 */
public class ListedServer {

    private long id;
    private String stringId, name, address, description, logoUrl, coverUrl;
    private String[] versions;
    private ServerCategory[] categories;
    private int votes, votesToday;

    ListedServer(long id, String stringId, String name, String address, String description, String logoUrl, String coverUrl, String[] versions, ServerCategory[] categories, int votes, int votesToday) {
        this.id = id;
        this.stringId = stringId;
        this.name = name;
        this.address = address;
        this.description = description;
        this.logoUrl = logoUrl;
        this.coverUrl = coverUrl;
        this.versions = versions;
        this.categories = categories;
        this.votes = votes;
        this.votesToday = votesToday;
    }

    static ListedServer fromJsonObject(JSONObject json) {
        long id = (long) json.get("id");
        String stringId = json.get("serverid").toString();
        String name = json.get("name").toString();
        String address = json.get("address").toString();
        String description = json.get("description").toString();
        String logoUrl = "https://www.minecraft-italia.it/media/server/logo/" + json.get("logo").toString();
        String coverUrl = "https://www.minecraft-italia.it/media/server/cover/" + json.get("cover").toString();
        JSONArray versionsArray = (JSONArray) json.get("version");
        String[] versions = new String[versionsArray.size()];
        for(int i = 0; i < versionsArray.size(); i++) {
            versions[i] = versionsArray.get(i).toString();
        }
        JSONArray categoriesArray = (JSONArray) json.get("categories");
        ServerCategory[] categories = new ServerCategory[categoriesArray.size()];
        for(int i = 0; i < categoriesArray.size(); i++) {
            categories[i] = ServerCategory.fromId(categoriesArray.get(i).toString());
        }
        int votes = Integer.parseInt(json.get("votes").toString());
        int votesToday = Integer.parseInt(json.get("votes_today").toString());
        return new ListedServer(id, stringId, name, address, description, logoUrl, coverUrl, versions, categories, votes, votesToday);
    }

    /**
     * @return Server ID
     */
    public long getId() {
        return id;
    }

    /**
     * @return Server string-ID
     */
    public String getStringId() {
        return stringId;
    }

    /**
     * @return Server name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Server address (IP)
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return Server description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Server logo URL
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * @return Server cover URL
     */
    public String getCoverUrl() {
        return coverUrl;
    }

    /**
     * @return Supported Minecraft versions
     */
    public String[] getVersions() {
        return versions;
    }

    /**
     * @return Categories where the server is in
     */
    public ServerCategory[] getCategories() {
        return categories;
    }

    /**
     * @return Votes of the server for this month
     */
    public int getVotes() {
        return votes;
    }

    /**
     * @return Votes of the server for this day
     */
    public int getVotesToday() {
        return votesToday;
    }
}
