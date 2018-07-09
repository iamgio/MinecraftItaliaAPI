package eu.iamgio.mcitaliaapi.connection;

import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Connection to any common website
 * @author Gio
 */
public class HttpConnection {

    private String url;
    private Connection connection;

    /**
     * @param url URL of the web page
     */
    public HttpConnection(String url) {
        this.url = url;
    }

    /**
     * Connects to the URL
     * @return This for concatenating
     */
    public HttpConnection connect() {
        this.connection = Jsoup.connect(url);
        return this;
    }

    /**
     * GET request
     * @return Parsed {@link Document}
     * @throws MinecraftItaliaException if an error occurred during the request
     */
    public Document get() throws MinecraftItaliaException {
        try {
            return this.connection.get();
        } catch(IOException e) {
            throw new MinecraftItaliaException(e.getMessage());
        }
    }

    /**
     * POST request
     * @return Parsed {@link Document}
     * @throws MinecraftItaliaException if an error occurred during the request
     */
    public Document post() throws MinecraftItaliaException {
        try {
            return this.connection.post();
        } catch(IOException e) {
            throw new MinecraftItaliaException(e.getMessage());
        }
    }

    /**
     * Adds parameter to request
     * @param k Parameter name
     * @param v Parameter value
     * @return This for concatenating
     */
    public HttpConnection data(String k, String v) {
        this.connection = connection.data(k, v);
        return this;
    }
}
