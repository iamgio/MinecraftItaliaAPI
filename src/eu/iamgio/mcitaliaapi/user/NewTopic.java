package eu.iamgio.mcitaliaapi.user;

import eu.iamgio.mcitaliaapi.connection.HttpConnection;
import eu.iamgio.mcitaliaapi.forum.ForumSubSection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class used to create new topics using concatenation
 * @author Gio
 */
public class NewTopic {

    private HttpConnection connection;
    private Document document;

    private LoggedUser user;
    private String title, text;

    private short prefixValue = 0;
    private SubscriptionOption subscriptionOption = SubscriptionOption.NONE;

    NewTopic(LoggedUser user, String title, String text, ForumSubSection section) {
        this.user = user;
        this.title = title;
        this.text = text;

        this.connection = new HttpConnection("https://www.minecraft-italia.it/forum/newthread.php?fid=" + section.getId()).connect();
        this.document = connection.get();
    }

    /**
     * @return Available prefixes for the topic
     */
    public List<Prefix> getAvailablePrefixes() {
        List<Prefix> prefixes = new ArrayList<>();
        Element prefixesElement = document.select("select[name=threadprefix]").first();
        if(prefixesElement == null) return prefixes;
        for(Element option : prefixesElement.getElementsByTag("option")) {
            prefixes.add(new Prefix(option.ownText(), Short.parseShort(option.attr("value"))));
        }
        return prefixes;
    }

    /**
     * Applies prefix
     * @param prefix Prefix to apply
     * @return This for concatenating
     */
    public NewTopic prefix(Prefix prefix) {
        this.prefixValue = prefix.id;
        return this;
    }

    /**
     * Applies subscription option
     * @param subscriptionOption Subscription option to apply
     * @return This for concatenating
     */
    public NewTopic subscriptionOption(SubscriptionOption subscriptionOption) {
        this.subscriptionOption = subscriptionOption;
        return this;
    }

    /**
     * Creates the topic using given attributes
     */
    public void create() {
        long boundary = new Random().nextLong();
        String posthash = document.select("input[name=posthash]").attr("value");
        String attachmentaid = document.select("input[name=attachmentaid]").attr("value");
        String attachmentact = document.select("input[name=attachmentact]").attr("value");
        String quoted_ids = document.select("input[name=quoted_ids]").attr("value");

        String requestBody =
                "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"my_post_key\"\n\n" + user.getPostKey() + "\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"threadprefix\"\n\n" + prefixValue + "\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"subject\"\n\n" + title + "\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"message\"\n\n" + text + "\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"postoptions[signature]\"\n\n1\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"postoptions[subscriptionmethod]\"\n\n" + subscriptionOption.getValue() + "\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"numpolloptions\"\n\n2\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"action\"\n\ndo_newthread\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"posthash\"\n\n" + posthash + "\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"attachmentaid\"\n\n" + attachmentaid + "\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"attachmentact\"\n\n" + attachmentact + "\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"quoted_ids\"\n\n" + quoted_ids + "\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"tid\"\n\n0\n" +
                        "-----------------------------" + boundary + "\nContent-Disposition: form-data; name=\"submit\"\n\nNuova discussione\n" +
                        "-----------------------------" + boundary + "--";

        connection
                .data("processed", "1")
                .header("Content-Type", "multipart/form-data; boundary=---------------------------" + boundary)
                .requestBody(requestBody)
                .post();
    }

    /**
     * Prefix available for topic
     */
    public static class Prefix {

        private String name;
        private short id;

        Prefix(String name, short id) {
            this.name = name;
            this.id = id;
        }

        /**
         * @return Prefix name
         */
        public String getName() {
            return name;
        }

        /**
         * @return Prefix ID
         */
        public short getId() {
            return id;
        }
    }

    /**
     * Subscription options for new topic
     */
    public enum SubscriptionOption {

        NONE(""), NO_NOTIFICATIONS("none"), EMAIL("email"), PRIVATE_MESSAGES("pm");

        private String value;

        SubscriptionOption(String value) {
            this.value = value;
        }

        /**
         * @return Option value
         */
        public String getValue() {
            return value;
        }
    }

}
