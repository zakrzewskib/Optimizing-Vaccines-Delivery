package pl.zakrzewb.optimization.read;

import pl.zakrzewb.optimization.data.Connection;
import pl.zakrzewb.optimization.data.Pharmacy;
import pl.zakrzewb.optimization.data.Producer;

import java.io.IOException;
import java.util.ArrayList;

public interface ReadDataHandlerInterface {
    void readDataFromFileToListsOfObjects(String fileName) throws IOException;

    ArrayList<Producer> getProducers();

    ArrayList<Pharmacy> getPharmacies();

    ArrayList<Connection> getConnections();
}