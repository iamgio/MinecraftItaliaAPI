package eu.iamgio.mcitaliaapi.server;

/**
 * Represents a server
 * @author Gio
 */
public class Server {

    Server() {} //TODO

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
