package eu.iamgio.mcitaliaapi.forum;

import java.util.List;

/**
 * Represents a poll inside of a topic
 * @author Gio
 */
public class TopicPoll {

    private List<TopicPollMember> members;
    private int count;
    private boolean locked;
    private long id;

    TopicPoll(List<TopicPollMember> members, int count, long id, boolean locked) {
        this.members = members;
        this.count = count;
        this.id = id;
        this.locked = locked;
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

    /**
     * @return <tt>true</tt> if the poll is locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * @return Poll ID
     */
    public long getId() {
        return id;
    }
}
