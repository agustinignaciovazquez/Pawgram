package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.User;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

public class UserListDTO {
    private List<UserDTO> users;
    private long totalCount;
    private long count;

    public UserListDTO() {}

    public UserListDTO(final List<User> users, long totalCount, final URI baseUri) {
        this.setTotalCount(totalCount);
        this.users = new LinkedList<>();
        this.setCount(users.size());

        for (User u : users)
            this.users.add(new UserDTO(u, baseUri));
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

}
