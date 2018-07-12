package eu.iamgio.mcitaliaapi.connection;

import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

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

    public String read() throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(this.url);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if(reader != null)
                reader.close();
        }
    }

    /**
     * Connects to the URL
     * @return This for concatenating
     */
    public HttpConnection connect() {
        this.connection = Jsoup.connect(url)
                .userAgent("Mozilla")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .followRedirects(true);
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
            e.printStackTrace();
            throw new MinecraftItaliaException(e.getMessage());
        }
    }

    /**
     * @return Connection response
     */
    public Connection.Response getResponse() {
        return this.connection.response();
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

    /**
     * Applies cookies
     * @param cookies Cookies
     * @return This for concatenating
     */
    public HttpConnection cookies(Map<String, String> cookies) {
        this.connection = connection.cookies(cookies);
        return this;
    }
}
