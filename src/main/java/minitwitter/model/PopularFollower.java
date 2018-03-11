package minitwitter.model;

public class PopularFollower {

    private int userId;
    private int popularFollowerId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPopularFollowerId() {
        return popularFollowerId;
    }

    public void setPopularFollowerId(int popularFollowerId) {
        this.popularFollowerId = popularFollowerId;
    }
}
