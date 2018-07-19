package eu.iamgio.mcitaliaapi.forum;

/**
 * Represents a member of a {@link TopicPoll}
 * @author Gio
 */
public class TopicPollMember {

    private String name;
    private int count;
    private double perc;

    TopicPollMember(String name, int count, double perc) {
        this.name = name;
        this.count = count;
        this.perc = perc;
    }

    /**
     * @return Name of the member
     */
    public String getName() {
        return name;
    }

    /**
     * @return Total votes count of the member
     */
    public int getCount() {
        return count;
    }

    /**
     * @return Vote percentage of the member
     */
    public double getPerc() {
        return perc;
    }
}
