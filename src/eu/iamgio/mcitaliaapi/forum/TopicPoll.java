package eu.iamgio.mcitaliaapi.forum;

import java.util.List;

/**
 * Represents a poll inside of a topic
 * @author Gio
 */
public class TopicPoll {

    private List<TopicPollMember> members;
    private int count;

    TopicPoll(List<TopicPollMember> members, int count) {
        this.members = members;
        this.count = count;
    }

    /**
     * @return Members (options) of the poll
     */
    public List<TopicPollMember> getMembers() {
        return members;
    }

    /**
     * @return Total votes count of the poll
     */
    public int getCount() {
        return count;
    }
}
