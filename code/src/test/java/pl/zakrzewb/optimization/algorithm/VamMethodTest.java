package pl.zakrzewb.optimization.algorithm;

import org.junit.Before;
import org.junit.Test;
import pl.zakrzewb.optimization.data.Connection;
import pl.zakrzewb.optimization.data.Pharmacy;
import pl.zakrzewb.optimization.data.Producer;
import pl.zakrzewb.optimization.read.ReadDataHandler;
import pl.zakrzewb.optimization.read.ReadDataHandlerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class VamMethodTest {

    ReadDataHandlerInterface readDataHandler;
    ArrayList<ArrayList<Integer>> expectedResultTable;

    @Before
    public void setUp() {
        expectedResultTable = new ArrayList<>();
    }

    private void readData(String inputFileName) throws IOException {
        readDataHandler = new ReadDataHandler();
        readDataHandler.readDataFromFileToListsOfObjects(inputFileName);
    }

    @Test
    public void testData1() throws IOException {
        readData("correctData/data1.txt");

        ArrayList<Producer> producers = readDataHandler.getProducers();
        ArrayList<Pharmacy> pharmacies = readDataHandler.getPharmacies();
        ArrayList<Connection> connections = readDataHandler.getConnections();
        VamMethod vamMethod = new VamMethod(producers, pharmacies, connections);
        vamMethod.optimize();

        double expected = 194403.9;
        double totalCosts = vamMethod.getSumOfTotalCosts();
        assertEquals(expected, totalCosts, 0.0001);
    }

    @Test
    public void testData2() throws IOException {
        readData("correctData/data2.txt");

        ArrayList<Producer> producers = readDataHandler.getProducers();
        ArrayList<Pharmacy> pharmacies = readDataHandler.getPharmacies();
        ArrayList<Connection> connections = readDataHandler.getConnections();
        VamMethod vamMethod = new VamMethod(producers, pharmacies, connections);
        vamMethod.optimize();

        double expected = 196093.47;
        double totalCosts = vamMethod.getSumOfTotalCosts();
        assertEquals(expected, totalCosts, 0.0001);
    }

    @Test
    public void testData3() throws IOException {
        readData("correctData/data3.txt");

        ArrayList<Producer> producers = readDataHandler.getProducers();
        ArrayList<Pharmacy> pharmacies = readDataHandler.getPharmacies();
        ArrayList<Connection> connections = readDataHandler.getConnections();
        VamMethod vamMethod = new VamMethod(producers, pharmacies, connections);
        vamMethod.optimize();

        double expected = 188617.31999999992;
        double totalCosts = vamMethod.getSumOfTotalCosts();
        assertEquals(expected, totalCosts, 0.0001);
    }

    @Test
    public void testData4() throws IOException {
        readData("correctData/data4.txt");

        ArrayList<Producer> producers = readDataHandler.getProducers();
        ArrayList<Pharmacy> pharmacies = readDataHandler.getPharmacies();
        ArrayList<Connection> connections = readDataHandler.getConnections();
        VamMethod vamMethod = new VamMethod(producers, pharmacies, connections);
        vamMethod.optimize();

        double expected = 620357.4399999998;
        double totalCosts = vamMethod.getSumOfTotalCosts();
        assertEquals(expected, totalCosts, 0.0001);
    }


    public void testIfEveryAmountIsLessThanConnectionMaximum(String fileName) throws IOException {
        readData(fileName);

        ArrayList<Producer> producers = readDataHandler.getProducers();
        ArrayList<Pharmacy> pharmacies = readDataHandler.getPharmacies();
        ArrayList<Connection> connections = readDataHandler.getConnections();

        Comparator<Connection> connectionComparator = (o1, o2) -> {
            int idPharmacy = o1.getIdPharmacy();
            int idPharmacy2 = o2.getIdPharmacy();

            int res = idPharmacy - idPharmacy2;
            if (res != 0) {
                return res;
            } else {
                int idProducer = o1.getIdProducer();
                int idProducer2 = o2.getIdProducer();
                return idProducer - idProducer2;
            }
        };
        connections.sort(connectionComparator);

        VamMethod vamMethod = new VamMethod(producers, pharmacies, connections);

        vamMethod.optimize();

        int index = 0;
        int pharmacyIndex = 0;

        for (ArrayList<Integer> arrayList : vamMethod.getResultMatrixOfAmountsThatNeedToBePurchased()) {
            for (Integer integer : arrayList) {
                if (integer != 0) {
                    if (integer > connections.get(index).getDailyMaximumNumber()) {
                        assert false;
                    }
                }
                index++;
            }
            pharmacyIndex++;
        }
    }

//    @Test
//    public void testIfEveryAmountIsLessThanConnectionMaximum_data1_Nowogrodzka() throws IOException {
//        testIfEveryAmountIsLessThanConnectionMaximum("correctData/data1_Nowogrodzka_gets_amount_bigger_than_in_connection.txt");
//    }

    @Test
    public void testIfEveryAmountIsLessThanConnectionMaximum_data1() throws IOException {
        testIfEveryAmountIsLessThanConnectionMaximum("correctData/data1.txt");
    }

    @Test
    public void testIfEveryAmountIsLessThanConnectionMaximum_data2() throws IOException {
        testIfEveryAmountIsLessThanConnectionMaximum("correctData/data2.txt");
    }

    @Test
    public void testIfEveryAmountIsLessThanConnectionMaximum_data3() throws IOException {
        testIfEveryAmountIsLessThanConnectionMaximum("correctData/data3.txt");
    }

    @Test
    public void testIfEveryAmountIsLessThanConnectionMaximum_data4() throws IOException {
        testIfEveryAmountIsLessThanConnectionMaximum("correctData/data4.txt");
    }

}