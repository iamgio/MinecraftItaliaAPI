package eu.iamgio.mcitaliaapi.user;

import eu.iamgio.mcitaliaapi.connection.Cookies;
import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.exception.MinecraftItaliaException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;

/**
 * Represents a common login
 * @author Gio
 */
public class Login {

    private LoggedUser user;
    private HttpConnection connection;
    private Map<String, String> cookies;

    private boolean invalid, has2fa, success;

    Login(LoggedUser user, HttpConnection connection, byte status) {
        this.user = user;
        this.connection = connection;
        if(status == 1) {
            has2fa = true;
        } else if(status == 2) {
            invalid = true;
        } else {
            success = true;
        }
        this.cookies = connection.getResponse().cookies();
    }

    /**
     * @return <tt>true</tt> if credentials are invalid
     */
    public boolean isInvalid() {
        return invalid;
    }

    /**
     * @return <tt>true</tt> if 2FA authentication code is required
     */
    public boolean has2FA() {
        return has2fa;
    }

    /**
     * Applies a 2FA authentication code
     * @param code Code
     * @return <tt>true</tt> if the code was correct
     * @throws MinecraftItaliaException if no authentication code is required
     */
    public boolean applyAuthenticationCode(String code) throws MinecraftItaliaException {
        if(invalid || !has2fa) {
            throw new MinecraftItaliaException("No authentication code is required");
        }
        success = new HttpConnection("https://www.minecraft-italia.it/forum/misc.php").connect()
                .data("action", "mybb2fa")
                .data("code", code)
                .data("uid", String.valueOf(user.getUid()))
                .cookies(cookies)
                .post()
                .getElementById("dropdown-profile-menu") != null;
        return success;
    }

    /**
     * Completes the login session
     * @return Logged user
     * @throws MinecraftItaliaException if the login is invalid
     */
    public LoggedUser complete() throws MinecraftItaliaException {
        if(!success) {
            throw new MinecraftItaliaException("Invalid login");
        }
        Cookies.cookies = connection.getResponse().cookies();
        Document document = connection.get();
        user.logoutUrl = document.getElementsByClass("mdi-exit-to-app").first().parent().attr("href");
        for(Element script : document.getElementsByTag("script")) {
            if(script.attributes().size() == 0) {
                String data = script.data();
                for(String line : data.split("\n")) {
                    if(line.contains("var my_post_key = ")) {
                        user.postKey = line.substring("\tvar my_post_key = \"".length(), line.length() - 3);
                        break;
                    }
                }
            }
        }
        return user;
    }
}
