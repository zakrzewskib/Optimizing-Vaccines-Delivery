package pl.zakrzewb.optimization.data;

public class Producer {
    private final int id;
    private final String name;
    private final int dailyProduction;

    public Producer(int id, String name, int dailyProduction) {
        this.id = id;
        this.name = name;
        this.dailyProduction = dailyProduction;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDailyProduction() {
        return dailyProduction;
    }
}
