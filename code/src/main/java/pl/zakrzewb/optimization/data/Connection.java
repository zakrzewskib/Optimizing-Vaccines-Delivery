package pl.zakrzewb.optimization.data;

public class Connection {
    private final int idProducer;
    private final int idPharmacy;
    private final int dailyMaximumNumber;
    private final double cost;

    public Connection(int idProducer, int idPharmacy, int dailyMaximumNumber, double cost) {
        this.idProducer = idProducer;
        this.idPharmacy = idPharmacy;
        this.dailyMaximumNumber = dailyMaximumNumber;
        this.cost = cost;
    }

    public int getIdProducer() {
        return idProducer;
    }

    public int getIdPharmacy() {
        return idPharmacy;
    }

    public int getDailyMaximumNumber() {
        return dailyMaximumNumber;
    }

    public double getCost() {
        return cost;
    }
}
