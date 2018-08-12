package eu.iamgio.mcitaliaapi.server;

import com.sun.istack.internal.Nullable;
import eu.iamgio.mcitaliaapi.connection.json.JSONParser;
import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a server category
 * @author Gio
 */
public enum ServerCategory {

    ALL("Tutti", ""),
    PREMIUM("Premium", "premium"),
    UPDATED("Aggiornati", "aggiornati"),
    CREATIVE("Creative", "creative"),
    CTF("CTF", "ctf"),
    FACTIONS("Factions", "factions"),
    FTB("FTB", "ftb"),
    HARDCORE("Hardcore", "hardcore"),
    HUNGER_GAMES("Hunger Games", "hungergames"),
    MCMMO("mcMMO", "mcmmo"),
    MINIGAMES("Minigames", "minigames"),
    MODDED("Moddati", "moddati"),
    NEW("Nuovi", "nuovi"),
    PIXELMON("Pixelmon", "pixelmon"),
    PRISON("Prison", "prison"),
    PVP("PVP", "pvp"),
    RPG("RPG", "roleplay"),
    SKYGAMES("SkyGames", "skygames"),
    SURVIVAL("Survival", "survival"),
    SURVIVALGAMES("SurvivalGames", "survivalgames"),
    TEKKIT("Tekkit", "tekkit"),
    TOWNY("Towny", "towny"),
    UHC("UHC", "uhc"),
    VANILLA("Vanilla", "vanilla");

    /**
     * @param id Server category ID
     * @return Server category by ID
     */
    public static ServerCategory fromId(String id) {
        for(ServerCategory category : values()) {
            if(category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }

    private String name, id;
    ServerCategory(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * @return Category name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Category ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param platform Supported platform (PC/PE)
     * @param versions Supported Minecraft versions
     * @param limit Shown servers size
     * @param orderField Order field (votes/titles)
     * @param orderType Order type (ascending/descending)
     * @return Matched servers
     * @throws MinecraftItaliaException if an error occurred during the request
     */
    public List<ListedServer> getServers(Server.Platform platform, @Nullable String[] versions, int limit, @Nullable Server.OrderField orderField, @Nullable Server.OrderType orderType) throws MinecraftItaliaException {
        List<ListedServer> servers = new ArrayList<>();
        String url =
                "https://www.minecraft-italia.it/api/server-list/?"
                + "platform=" + platform.value()
                + "&limit=" + limit;
        if(this != ALL) {
            url += "&category=" + getId();
        }
        if(versions != null) {
            StringBuilder versionsStringBuilder = new StringBuilder();
            for(String version : versions) {
                versionsStringBuilder.append(version).append(",");
            }
            String versionsString = versionsStringBuilder.toString().substring(0, versionsStringBuilder.toString().length() - 1);
            url += "&version=" + versionsString;
        }
        if(orderField != null) {
            url += "&order_field=" + orderField.value();
        }
        if(orderType != null) {
            url += "&order_type=" + orderType.value();
        }
        JSONObject json = new JSONParser(url).parse();
        if(json.get("status").equals("error")) {
            throw new MinecraftItaliaException(json.get("message").toString());
        }
        for(Object result : (JSONArray) json.get("results")) {
            servers.add(ListedServer.fromJsonObject((JSONObject) result));
        }
        return servers;
    }

    /**
     * @see #getServers(Server.Platform, String[], int, Server.OrderField, Server.OrderType)
     */
    public List<ListedServer> getServers(Server.Platform platform, int limit, @Nullable Server.OrderField orderField, @Nullable Server.OrderType orderType) {
        return getServers(platform, null, limit, orderField, orderType);
    }

    /**
     * @see #getServers(Server.Platform, String[], int, Server.OrderField, Server.OrderType)
     */
    public List<ListedServer> getServers(Server.Platform platform, @Nullable Server.OrderField orderField, @Nullable Server.OrderType orderType) {
        return getServers(platform, null, 30, orderField, orderType);
    }

    /**
     * @see #getServers(Server.Platform, String[], int, Server.OrderField, Server.OrderType)
     */
    public List<ListedServer> getServers(Server.Platform platform) {
        return getServers(platform, null, 30, null, null);
    }

    /**
     * @see #getServers(Server.Platform, String[], int, Server.OrderField, Server.OrderType)
     */
    public List<ListedServer> getServers() {
        return getServers(Server.Platform.PC, null, 30, null, null);
    }
}
