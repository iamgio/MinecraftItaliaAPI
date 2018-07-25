package eu.iamgio.mcitaliaapi.forum;

import eu.iamgio.mcitaliaapi.user.UnparsedUser;

import java.util.List;

/**
 * Represents a member of a {@link TopicPoll}
 * @author Gio
 */
public class TopicPollMember {

    private String name;
    private int count;
    private double perc;
    private List<UnparsedUser> users;

    TopicPollMember(String name, int count, double perc, List<UnparsedUser> users) {
        this.name = name;
        this.count = count;
        this.perc = perc;
        this.users = users;
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

    /**
     * @return Users who voted for this member (might be empty if anonymous)
     */
    public List<UnparsedUser> getUsers() {
        return users;
    }
}
