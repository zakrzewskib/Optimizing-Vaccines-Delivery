package pl.zakrzewb.optimization.read;

import pl.zakrzewb.optimization.data.Connection;
import pl.zakrzewb.optimization.data.Pharmacy;
import pl.zakrzewb.optimization.data.Producer;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadDataHandler implements ReadDataHandlerInterface {

    private char c;
    ArrayList<Producer> producers;
    ArrayList<Pharmacy> pharmacies;
    ArrayList<Connection> connections;

    @Override
    public ArrayList<Producer> getProducers() {
        return producers;
    }

    @Override
    public ArrayList<Pharmacy> getPharmacies() {
        return pharmacies;
    }

    @Override
    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public ReadDataHandler() {
        producers = new ArrayList<>();
        pharmacies = new ArrayList<>();
        connections = new ArrayList<>();
    }

    private int lineNumber = 1;
    private int sumOfDailyDemand = 0;
    private int sumOfDailyProduction = 0;

    @Override
    public void readDataFromFileToListsOfObjects(String fileName) throws IOException {
        lineNumber = 1;
        int countOfHeaders = 0;
        try (FileReader f = new FileReader(fileName)) {
            while (f.ready()) {
                c = (char) f.read();

                if (c == '\n') {
                    lineNumber++;
                }

                if (c == '#') {
                    countOfHeaders++;
                    while (c != '\n') {
                        c = (char) f.read();
                    }
                }

                if (c == '\n') {
                    lineNumber++;
                }

                while (c != '\n' && f.ready()) {
                    if (countOfHeaders == 1 || countOfHeaders == 2) {
                        int id = readId(f);
                        String name = readName(f);
                        c = (char) f.read();
                        int amount = readDailyProductionOrDailyDemand(f);

                        if (countOfHeaders == 1) {
                            producers.add(new Producer(id, name, amount));
                            sumOfDailyProduction += amount;

                        } else {
                            pharmacies.add(new Pharmacy(id, name, amount));
                            sumOfDailyDemand += amount;
                        }

                    } else if (countOfHeaders == 3) {
                        int id = readId(f);
                        c = (char) f.read();
                        int id2 = readId(f);
                        int dailyMax = readDailyMaximumNumber(f);
                        c = (char) f.read();
                        double cost = readCost(f);

                        connections.add(new Connection(id, id2, dailyMax, cost));

                    } else {
                        break;
                    }

                    if (c == '\n') {
                        lineNumber++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        throwExceptionIfSumOfDailyDemandIsBiggerThanSumOfDailyProduction();
    }

    private void throwExceptionIfSumOfDailyDemandIsBiggerThanSumOfDailyProduction() throws IOException {
        if (sumOfDailyDemand > sumOfDailyProduction) {
            throw new IOException("Sum of daily demands is bigger than sum of daily productions!");
        }
    }

    private int readId(FileReader f) throws IOException {
        StringBuilder idBuilder = new StringBuilder();
        idBuilder.append(c);
        while (c != ' ') {
            c = (char) f.read();
            idBuilder.append(c);
        }
        idBuilder.deleteCharAt(idBuilder.length() - 1);
        int id = Integer.parseInt(idBuilder.toString());
        c = (char) f.read();
        c = (char) f.read();

        if (id < 0) {
            throw new IOException("Id is negative at line: " + lineNumber);
        }
        return id;
    }

    private String readName(FileReader f) throws IOException {
        StringBuilder s = new StringBuilder();
        while (c != '|') {
            c = (char) f.read();
            s.append(c);
        }
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }

    private int readDailyProductionOrDailyDemand(FileReader f) throws IOException {
        StringBuilder s2 = new StringBuilder();
        while (c != '\n') {
            c = (char) f.read();
            s2.append(c);
        }

        s2.deleteCharAt(s2.length() - 1);
        if (s2.charAt(s2.length() - 1) == 13)
            s2.deleteCharAt(s2.length() - 1);

        int daily = Integer.parseInt(s2.toString());
        if (daily < 0) {
            throw new IOException("Daily Demand/Production is negative at line: " + lineNumber);
        }

        return daily;
    }

    private int readDailyMaximumNumber(FileReader f) throws IOException {
        StringBuilder s = new StringBuilder();
        while (c != '|') {
            c = (char) f.read();
            s.append(c);
        }

        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);

        int max = Integer.parseInt(s.toString());
        if (max < 0) {
            throw new IOException("Daily Maximum number is negative at line: " + lineNumber);
        }

        return max;
    }

    private double readCost(FileReader f) throws IOException {
        StringBuilder s2 = new StringBuilder();
        while (c != '\n' && f.ready()) {
            c = (char) f.read();
            s2.append(c);
        }

        double cost = Double.parseDouble(s2.toString());
        if (cost < 0) {
            throw new IOException("Cost is negative at line: " + lineNumber);
        }

        return cost;
    }
}