package eu.iamgio.mcitaliaapi.exception;

/**
 * Runtime exception used in this API
 * @author Gio
 */
public class McItaliaRuntimeException extends RuntimeException {

    public static final String NO_INFO = "The user does not have this information.";

    public McItaliaRuntimeException(String message) {
        super(message);
    }
}
