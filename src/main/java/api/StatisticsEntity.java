package api;

public class StatisticsEntity {
    private int count;
    private String id;

    public StatisticsEntity(int count, String id) {
        this.count = count;
        this.id = id;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
