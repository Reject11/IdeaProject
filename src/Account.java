import java.util.ArrayList;

class Account {
    private String name;
    private ArrayList<Account> follower;

    Account(String name) {
        this.name = name;
        this.follower = new ArrayList<>();
    }

    String getName() {
        return name;
    }

    boolean isFollower(Account account){
        Main.followerRequests += 1;
        return follower.contains(account);
    }

    void addFollower(Account account){
        follower.add(account);
    }
}
