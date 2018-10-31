import java.util.ArrayList;

public class Account {
    private String name;
    private ArrayList<Account> follower;

    public Account(String name) {
        this.name = name;
        this.follower = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isFollower(Account account){
        Main.followerRequests += 1;
        return follower.contains(account);
    }

    public void addFollower(Account account){
        follower.add(account);
    }
}
