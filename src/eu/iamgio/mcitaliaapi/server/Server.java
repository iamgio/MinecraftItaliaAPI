package eu.iamgio.mcitaliaapi.server;

import eu.iamgio.mcitaliaapi.connection.json.JSONParser;
import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
import org.json.simple.JSONObject;

/**
 * Represents a server
 * @author Gio
 */
public class Server {

    private long id;
    private String stringId, name, address, description, rawVersion;
    private int position, votes, votesToday, playersCount, maxPlayersCount, slots;
    private boolean online;

    private Server(long id, String stringId, String name, String address, String description, String rawVersion, int position, int votes, int votesToday, int playersCount, int maxPlayersCount, int slots, boolean online) {
        this.id = id;
        this.stringId = stringId;
        this.name = name;
        this.address = address;
        this.description = description;
        this.rawVersion = rawVersion;
        this.position = position;
        this.votes = votes;
        this.votesToday = votesToday;
        this.playersCount = playersCount;
        this.maxPlayersCount = maxPlayersCount;
        this.slots = slots;
        this.online = online;
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
     * @return Server unparsed version (e.g.: da 1.8 a 1.12)
     */
    public String getRawVersion() {
        return rawVersion;
    }

    /**
     * @return Server position on this month
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return Server votes on this month
     */
    public int getVotes() {
        return votes;
    }

    /**
     * @return Server votes on today
     */
    public int getVotesToday() {
        return votesToday;
    }

    /**
     * @return Actual online players on the server
     */
    public int getPlayersCount() {
        return playersCount;
    }

    /**
     * @return Max amount of online players on the server
     */
    public int getMaxPlayersCount() {
        return maxPlayersCount;
    }

    /**
     * @return Available player slots on the server
     */
    public int getSlots() {
        return slots;
    }

    /**
     * @return <tt>true</tt> if the server is online;
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * @param stringId String-ID of the server
     * @return Corresponding {@link Server} object
     * @throws MinecraftItaliaException if the server does not exist
     */
    public static Server fromStringId(String stringId) throws MinecraftItaliaException {
        stringId = stringId.toLowerCase();
        JSONObject json;
        try {
            json = new JSONParser("https://www.minecraft-italia.it/api/server-info/" + stringId).parse();
        } catch(RuntimeException e) {
            throw new MinecraftItaliaException("Could not find server '" + stringId + "'");
        }
        if(json.get("status").equals("error")) {
            throw new MinecraftItaliaException(json.get("message").toString());
        }
        long id = (long) json.get("id");
        String name = json.get("title").toString();
        String address = json.get("address").toString();
        String description = json.get("description").toString();
        String rawVersion = json.get("version").toString();
        int position = Integer.parseInt(json.get("position").toString());
        int votes = Integer.parseInt(json.get("votes").toString());
        int votesToday = Integer.parseInt(json.get("votes_today").toString());
        int playersCount = Integer.parseInt(json.get("players").toString());
        int maxPlayersCount = Integer.parseInt(json.get("max_players").toString());
        int slots = Integer.parseInt(json.get("slot").toString());
        boolean online = (boolean) json.get("online");
        return new Server(id, stringId, name, address, description, rawVersion, position, votes, votesToday, playersCount, maxPlayersCount, slots, online);
    }

    public enum Platform {
        PC, PE;

        public String value() {
            return name().toLowerCase();
        }
    }

    public enum OrderField {
        VOTES, TITLE;

        public String value() {
            return name().toLowerCase();
        }
    }

    public enum OrderType {
        ASCENDING("ASC"), DESCENDING("DESC");

        private String value;
        OrderType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
