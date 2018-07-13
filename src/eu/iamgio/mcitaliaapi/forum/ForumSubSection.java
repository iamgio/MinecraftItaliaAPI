package eu.iamgio.mcitaliaapi.forum;

/**
 * @author Gio
 */
public class ForumSubSection {

    protected String name, url;

    ForumSubSection(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * @return Section name
     */
    public String getName() {
        return name;
    }

    /**
     * @return URL to section
     */
    public String getUrl() {
        return url;
    }
}
