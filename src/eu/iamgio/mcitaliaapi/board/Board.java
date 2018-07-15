package eu.iamgio.mcitaliaapi.board;

import eu.iamgio.mcitaliaapi.connection.json.JSONParser;
import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the board
 * @author Gio
 */
public class Board {

    private static Board instance;

    private Board() {
        instance = this;
    }

    /**
     * @return Board instance
     */
    public static Board getBoard() {
        return instance == null ? new Board() : instance;
    }

    /**
     * @param url URL to parse JSON to
     * @return Board posts
     */
    public static List<BoardPost> getBoardPosts(String url) throws MinecraftItaliaException {
        List<BoardPost> posts = new ArrayList<>();
        JSONObject object = new JSONParser(url).parse();
        if(object.get("status").toString().equals("error")) throw new MinecraftItaliaException(object.get("descr").toString());
        JSONArray array = (JSONArray) object.get("data");
        for(Object obj : array) {
            posts.add(BoardPost.fromJsonObject((JSONObject) obj));
        }
        return posts;
    }

    /**
     * @return First 15 board posts
     */
    public List<BoardPost> getBoardPosts() {
        return getBoardPosts("https://www.minecraft-italia.it/board/get_posts?filter[type]=all&filter[uid]=0&start=0");
    }

    /**
     * @param start Start post
     * @return 15 board posts after <tt>start</tt>
     */
    public List<BoardPost> getBoardPosts(BoardPost start) {
        return getBoardPosts("https://www.minecraft-italia.it/board/get_posts?filter[type]=all&filter[uid]=0&start=" + start.getId());
    }
}
