package eu.iamgio.mcitaliaapi.forum;

/**
 * Represents the prefix of a topic
 * @author Gio
 */
public class TopicPrefix {

    private String text, color;

    TopicPrefix(String text, String color) {
        this.text = text;
        this.color = color;
    }

    /**
     * @return Prefix text
     */
    public String getText() {
        return text;
    }

    /**
     * @return Prefix color in hexadecimal
     */
    public String getColor() {
        return color;
    }
}
