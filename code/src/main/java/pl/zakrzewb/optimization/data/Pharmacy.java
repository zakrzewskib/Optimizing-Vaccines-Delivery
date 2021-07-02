package pl.zakrzewb.optimization.data;

public class Pharmacy {
    private final int id;
    private final String name;
    private final int dailyDemand;

    public Pharmacy(int id, String name, int dailyDemand) {
        this.id = id;
        this.name = name;
        this.dailyDemand = dailyDemand;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDailyDemand() {
        return dailyDemand;
    }
}
