package pl.zakrzewb.optimization.main;

import pl.zakrzewb.optimization.algorithm.QuickSortRecursivePharmacy;
import pl.zakrzewb.optimization.algorithm.QuickSortRecursiveProducers;
import pl.zakrzewb.optimization.algorithm.VamMethod;
import pl.zakrzewb.optimization.data.Connection;
import pl.zakrzewb.optimization.data.Pharmacy;
import pl.zakrzewb.optimization.data.Producer;
import pl.zakrzewb.optimization.errors.ErrorManager;
import pl.zakrzewb.optimization.errors.ErrorManagerInterface;
import pl.zakrzewb.optimization.read.ReadDataHandler;
import pl.zakrzewb.optimization.read.ReadDataHandlerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) throws IOException {
        ErrorManagerInterface errorManager = new ErrorManager();

        String inputFileName = "correctData/data1.txt";

        if (args.length == 0) {
            inputFileName = "correctData/data1.txt";
        } else if (args.length == 1) {
            inputFileName = args[0];
        } else {
            String message = "Your arguments must be only the name of the input file.";
            errorManager.throwMyException(message);
        }

        ReadDataHandlerInterface readDataHandler = new ReadDataHandler();
        readDataHandler.readDataFromFileToListsOfObjects(inputFileName);

        ArrayList<Producer> producers = readDataHandler.getProducers();

        QuickSortRecursiveProducers quickSortRecursiveProducers = new QuickSortRecursiveProducers();
        quickSortRecursiveProducers.sort(producers);

        ArrayList<Pharmacy> pharmacies = readDataHandler.getPharmacies();

        QuickSortRecursivePharmacy quickSortRecursivePharmacy = new QuickSortRecursivePharmacy();
        quickSortRecursivePharmacy.sort(pharmacies);

        ArrayList<Connection> connections = readDataHandler.getConnections();
        sortConnections(connections);

        VamMethod vamMethod = new VamMethod(producers, pharmacies, connections);
        vamMethod.optimize();
    }

    private static void sortConnections(ArrayList<Connection> connections) {
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
    }
}