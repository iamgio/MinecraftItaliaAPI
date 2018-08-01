package eu.iamgio.mcitaliaapi.user;

/**
 * Category of private message
 * @author Gio
 */
public enum PrivateMessageCategory {

    RECEIVED, SENT, DRAFT, BIN;

    int fid() {
        return ordinal() + 1;
    }
}
