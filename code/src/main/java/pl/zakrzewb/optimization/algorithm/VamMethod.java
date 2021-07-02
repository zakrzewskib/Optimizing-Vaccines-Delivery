package pl.zakrzewb.optimization.algorithm;

import pl.zakrzewb.optimization.data.Connection;
import pl.zakrzewb.optimization.data.Pharmacy;
import pl.zakrzewb.optimization.data.Producer;

import java.io.FileWriter;
import java.util.ArrayList;

public class VamMethod {
    ArrayList<Producer> producers;
    ArrayList<Pharmacy> pharmacies;
    ArrayList<Connection> connections;

    ArrayList<Integer> producersDailyProduction;
    ArrayList<Integer> pharmacyDailyDemand;

    ArrayList<ArrayList<Double>> matrixOfCostsToBeModified;
    ArrayList<ArrayList<Double>> originalMatrixOfCosts;
    ArrayList<ArrayList<Double>> columnsOfMatrixOfCosts;

    private ArrayList<Double> listOfDifferencesOfTheTwoSmallestValuesInColumns;
    private ArrayList<Double> listOfDifferencesOfTheTwoSmallestValuesInRows;

    private static final double signalThatIndexInMatrixIsDeleted = 1_000_000; // Should be maximum of costs from Connections
    private static final double signalThatIndexIsDeletedInDifferencesList = -1; // Should be negative

    ArrayList<ArrayList<Integer>> resultMatrixOfAmountsThatNeedToBePurchased;

    public ArrayList<ArrayList<Integer>> getResultMatrixOfAmountsThatNeedToBePurchased() {
        return resultMatrixOfAmountsThatNeedToBePurchased;
    }

    private double sumOfTotalCosts;

    public VamMethod(ArrayList<Producer> producers, ArrayList<Pharmacy> pharmacies, ArrayList<Connection> connections) {
        this.producers = producers;
        this.pharmacies = pharmacies;
        this.connections = connections;
        matrixOfCostsToBeModified = new ArrayList<>();
        columnsOfMatrixOfCosts = new ArrayList<>();
        resultMatrixOfAmountsThatNeedToBePurchased = new ArrayList<>();
        originalMatrixOfCosts = new ArrayList<>();
    }

    private void fillProducersDailyProduction() {
        producersDailyProduction = new ArrayList<>();
        for (int j = 0; j < producers.size(); j++) {
            producersDailyProduction.add(0);
        }
        for (Producer producers : producers) {
            producersDailyProduction.set(producers.getId(), producers.getDailyProduction());
        }
    }

    private void fillPharmacyDailyDemand() {
        pharmacyDailyDemand = new ArrayList<>();
        for (int j = 0; j < pharmacies.size(); j++) {
            pharmacyDailyDemand.add(0);
        }
        for (Pharmacy pharmacy : pharmacies) {
            pharmacyDailyDemand.set(pharmacy.getId(), pharmacy.getDailyDemand());
        }
    }

    private void fillOriginalMatrixOfCosts() {
        for (int i = 0; i < pharmacies.size(); i++) {
            ArrayList<Double> rows = new ArrayList<>();
            for (int j = 0; j < producers.size(); j++) {
                rows.add(0.0);
            }
            originalMatrixOfCosts.add(rows);
        }

        for (Connection connection : connections) {
            int rowIndex = connection.getIdPharmacy();
            int columnIndex = connection.getIdProducer();
            originalMatrixOfCosts.get(rowIndex).set(columnIndex, connection.getCost());
        }
    }

    private void fillMatrixOfCostsToBeModified() {
        for (int i = 0; i < pharmacies.size(); i++) {
            ArrayList<Double> rows = new ArrayList<>();
            for (int j = 0; j < producers.size(); j++) {
                rows.add(0.0);
            }
            this.matrixOfCostsToBeModified.add(rows);
        }

        for (Connection connection : connections) {
            int rowIndex = connection.getIdPharmacy();
            int columnIndex = connection.getIdProducer();
            this.matrixOfCostsToBeModified.get(rowIndex).set(columnIndex, connection.getCost());
        }
    }

    private void fillColumnsOfMatrixOfCosts() {
        for (int i = 0; i < producers.size(); i++) {
            ArrayList<Double> column = new ArrayList<>();
            for (int j = 0; j < pharmacies.size(); j++) {
                column.add(matrixOfCostsToBeModified.get(j).get(i));
            }
            columnsOfMatrixOfCosts.add(column);
        }
    }

    private void fillResultMatrixOfAmountsThatNeedToBePurchased() {
        for (int i = 0; i < pharmacies.size(); i++) {
            ArrayList<Integer> rows = new ArrayList<>();
            for (int j = 0; j < producers.size(); j++) {
                rows.add(0);
            }
            resultMatrixOfAmountsThatNeedToBePurchased.add(rows);
        }
    }

    private void fillListOfDifferencesOfTheTwoSmallestValuesInColumns() {
        listOfDifferencesOfTheTwoSmallestValuesInRows = new ArrayList<>();
        for (ArrayList<Double> doubles : matrixOfCostsToBeModified) {
            double min = findMinimal(doubles);
            double secondMin = findSecondMinimal(doubles);
            if (min == signalThatIndexInMatrixIsDeleted && secondMin == signalThatIndexInMatrixIsDeleted) {
                listOfDifferencesOfTheTwoSmallestValuesInRows.add(signalThatIndexIsDeletedInDifferencesList);
            } else {
                listOfDifferencesOfTheTwoSmallestValuesInRows.add(secondMin - min);
            }
        }
    }

    private void fillListOfDifferencesOfTheTwoSmallestValuesInRows() {
        listOfDifferencesOfTheTwoSmallestValuesInColumns = new ArrayList<>();
        for (ArrayList<Double> doubles : columnsOfMatrixOfCosts) {
            double min = findMinimal(doubles);
            double secondMin = findSecondMinimal(doubles);
            if (min == signalThatIndexInMatrixIsDeleted && secondMin == signalThatIndexInMatrixIsDeleted) {
                listOfDifferencesOfTheTwoSmallestValuesInColumns.add(signalThatIndexIsDeletedInDifferencesList);
            } else {
                listOfDifferencesOfTheTwoSmallestValuesInColumns.add(secondMin - min);
            }
        }
    }

    private double valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInColumns() {
        return findMaximum(listOfDifferencesOfTheTwoSmallestValuesInColumns);
    }

    private double valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInRows() {
        return findMaximum(listOfDifferencesOfTheTwoSmallestValuesInRows);
    }

    private int indexOfMaxDifferenceOfTheTwoSmallestValuesInColumns() {
        return myIndexOfArray(valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInColumns(),
                listOfDifferencesOfTheTwoSmallestValuesInColumns);
    }

    private int indexOfMaxDifferenceOfTheTwoSmallestValuesInRows() {
        return myIndexOfArray(valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInRows(),
                listOfDifferencesOfTheTwoSmallestValuesInRows);
    }

    private int indexOfRow(int column, double minimalValue) {
        return myIndexOfArray(minimalValue, columnsOfMatrixOfCosts.get(column));
    }

    private int indexOfColumn(int row, double minimalValue) {
        return myIndexOfArray(minimalValue, matrixOfCostsToBeModified.get(row));
    }

    private int myIndexOfArray(double value, ArrayList<Double> arrayList) {
        for (int j = 0; j < arrayList.size(); j++) {
            double epsilon = 0.01;
            if (arrayList.get(j) == value) {
                return j;
            } else if (Math.abs(arrayList.get(j) - value) < epsilon) {
                return j;
            }
        }
        return -1;
    }

    private double returnMinimalCostInDifferencesOfTheTwoSmallestValuesInColumns() {
        ArrayList<Double> minimalsCosts = new ArrayList<>();
        double minimalCost = findMinimal(columnsOfMatrixOfCosts.get(indexOfMaxDifferenceOfTheTwoSmallestValuesInColumns()));
        minimalsCosts.add(minimalCost);
        int size = columnsOfMatrixOfCosts.get(indexOfMaxDifferenceOfTheTwoSmallestValuesInColumns()).size();
        for (int i = indexOfMaxDifferenceOfTheTwoSmallestValuesInColumns() + 1; i < size
                && i < listOfDifferencesOfTheTwoSmallestValuesInColumns.size(); i++) {
            if (listOfDifferencesOfTheTwoSmallestValuesInColumns.get(i)
                    == valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInColumns()) {
                minimalsCosts.add(matrixOfCostsToBeModified.get(indexOfMaxDifferenceOfTheTwoSmallestValuesInColumns()).get(i));
            }
        }
        minimalCost = findMinimal(minimalsCosts);
        return minimalCost;
    }

    private double returnMinimalCostInDifferencesOfTheTwoSmallestValuesInRows() {
        ArrayList<Double> minimalCosts = new ArrayList<>();
        double minimalCost = findMinimal(matrixOfCostsToBeModified.get(indexOfMaxDifferenceOfTheTwoSmallestValuesInRows()));
        minimalCosts.add(minimalCost);
        int size = matrixOfCostsToBeModified.get(indexOfMaxDifferenceOfTheTwoSmallestValuesInRows()).size();
        for (int i = indexOfMaxDifferenceOfTheTwoSmallestValuesInColumns() + 1; i < size
                && i < listOfDifferencesOfTheTwoSmallestValuesInRows.size(); i++) {
            if (listOfDifferencesOfTheTwoSmallestValuesInRows.get(i)
                    == valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInRows()) {
                minimalCosts.add(matrixOfCostsToBeModified.get(indexOfMaxDifferenceOfTheTwoSmallestValuesInRows()).get(i));
            }
        }
        minimalCost = findMinimal(minimalCosts);
        return minimalCost;
    }

    private void fillMatricesAndLists() {
        fillMatrixOfCostsToBeModified();
        fillResultMatrixOfAmountsThatNeedToBePurchased();
        fillProducersDailyProduction();
        fillPharmacyDailyDemand();
        fillColumnsOfMatrixOfCosts();
        fillListOfDifferencesOfTheTwoSmallestValuesInColumns();
        fillListOfDifferencesOfTheTwoSmallestValuesInRows();
    }

    private void removeRowFromMatrixOfCostsToBeModified(int indexOfPharmacy) {
        for (int i = 0; i < matrixOfCostsToBeModified.get(indexOfPharmacy).size(); i++) {
            matrixOfCostsToBeModified.get(indexOfPharmacy).set(i, signalThatIndexInMatrixIsDeleted);
        }
    }

    private void removeColumnFromColumnsOfMatrixOfCosts(int indexOfProducer) {
        for (ArrayList<Double> doubles : matrixOfCostsToBeModified) {
            doubles.set(indexOfProducer, signalThatIndexInMatrixIsDeleted);
        }
    }

    private void updateColumnsOfMatrixOfCosts() {
        for (int i = 0; i < producers.size(); i++) {
            ArrayList<Double> column = new ArrayList<>();
            for (int j = 0; j < pharmacies.size(); j++) {
                column.add(matrixOfCostsToBeModified.get(j).get(i));
            }
            columnsOfMatrixOfCosts.set(i, column);
        }
    }

    private void updateBasedOnDailyOfProducerProduction(int indexOfPharmacy, int indexOfProducer, int pharmacy, int producer) {
        pharmacyDailyDemand.set(indexOfPharmacy, 0);
        producersDailyProduction.set(indexOfProducer, producer - pharmacy);

        int before = resultMatrixOfAmountsThatNeedToBePurchased.get(indexOfPharmacy).get(indexOfProducer);
        resultMatrixOfAmountsThatNeedToBePurchased.get(indexOfPharmacy).set(indexOfProducer, before + pharmacy);

        removeRowFromMatrixOfCostsToBeModified(indexOfPharmacy);
        updateColumnsOfMatrixOfCosts();
    }

    private void updateBasedOnDailyOfPharmacyDemand(int indexOfPharmacy, int indexOfProducer, int pharmacy, int producer) {
        pharmacyDailyDemand.set(indexOfPharmacy, pharmacy - producer);
        producersDailyProduction.set(indexOfProducer, 0);

        int before = resultMatrixOfAmountsThatNeedToBePurchased.get(indexOfPharmacy).get(indexOfProducer);
        resultMatrixOfAmountsThatNeedToBePurchased.get(indexOfPharmacy).set(indexOfProducer, before + producer);

        removeColumnFromColumnsOfMatrixOfCosts(indexOfProducer);
        updateColumnsOfMatrixOfCosts();
    }

    private void updateBasedOnBothDailyProductionAndDemand(int indexOfPharmacy, int indexOfProducer, int pharmacy) {
        pharmacyDailyDemand.set(indexOfPharmacy, 0);
        producersDailyProduction.set(indexOfProducer, 0);

        int before = resultMatrixOfAmountsThatNeedToBePurchased.get(indexOfPharmacy).get(indexOfProducer);
        resultMatrixOfAmountsThatNeedToBePurchased.get(indexOfPharmacy).set(indexOfProducer, before + pharmacy);

        removeRowFromMatrixOfCostsToBeModified(indexOfPharmacy);
        removeColumnFromColumnsOfMatrixOfCosts(indexOfProducer);
        updateColumnsOfMatrixOfCosts();
    }

    private void updateBasedOnDailyMaximumNumberInConnections(int indexOfPharmacy, int indexOfProducer,
                                                              int pharmacy, int producer, int connectionLimit) {
        pharmacyDailyDemand.set(indexOfPharmacy, pharmacy - connectionLimit);
        producersDailyProduction.set(indexOfProducer, producer - connectionLimit);

        int before = resultMatrixOfAmountsThatNeedToBePurchased.get(indexOfPharmacy).get(indexOfProducer);
        resultMatrixOfAmountsThatNeedToBePurchased.get(indexOfPharmacy).set(indexOfProducer, before + connectionLimit);

        matrixOfCostsToBeModified.get(indexOfPharmacy).set(indexOfProducer, signalThatIndexInMatrixIsDeleted);
        columnsOfMatrixOfCosts.get(indexOfProducer).set(indexOfPharmacy, signalThatIndexInMatrixIsDeleted);
    }

    private int getDailyMaximumNumberFromConnection(int idPharmacy, int idProducer) {
        for (Connection connection : connections) {
            if (connection.getIdProducer() == idProducer && connection.getIdPharmacy() == idPharmacy) {
                return connection.getDailyMaximumNumber();
            }
        }
        return -1;
    }

    private void updateBasedOnRowOrColumn(int indexOfPharmacy, int indexOfProducer, int pharmacy, int producer) {
        ArrayList<Integer> limits = new ArrayList<>();
        limits.add(pharmacy);
        limits.add(producer);
        int connectionLimit = getDailyMaximumNumberFromConnection(indexOfPharmacy, indexOfProducer);
        limits.add(connectionLimit);
        int minimum = findMinimalForIntegers(limits);
        if (minimum == producer || minimum == pharmacy) {
            if (producer > pharmacy) {
                updateBasedOnDailyOfProducerProduction(indexOfPharmacy, indexOfProducer, pharmacy, producer);
            } else if (producer == pharmacy) {
                updateBasedOnBothDailyProductionAndDemand(indexOfPharmacy, indexOfProducer, pharmacy);
            } else {
                updateBasedOnDailyOfPharmacyDemand(indexOfPharmacy, indexOfProducer, pharmacy, producer);
            }
        } else {
            updateBasedOnDailyMaximumNumberInConnections(indexOfPharmacy, indexOfProducer, pharmacy, producer, connectionLimit);
        }
    }

    private void updateBasedOnColumn() {
        double minimal = returnMinimalCostInDifferencesOfTheTwoSmallestValuesInColumns();
        int indexOfPharmacy = indexOfRow(indexOfMaxDifferenceOfTheTwoSmallestValuesInColumns(), minimal);

        int indexOfProducer = indexOfMaxDifferenceOfTheTwoSmallestValuesInColumns();
        int pharmacy = pharmacyDailyDemand.get(indexOfPharmacy);
        int producer = producersDailyProduction.get(indexOfProducer);

        updateBasedOnRowOrColumn(indexOfPharmacy, indexOfProducer, pharmacy, producer);
    }

    private void updateBasedOnRow() {
        double minimal = returnMinimalCostInDifferencesOfTheTwoSmallestValuesInRows();
        int indexOfPharmacy = indexOfMaxDifferenceOfTheTwoSmallestValuesInRows();
        int indexOfProducer = indexOfColumn(indexOfMaxDifferenceOfTheTwoSmallestValuesInRows(), minimal);

        int pharmacy = pharmacyDailyDemand.get(indexOfPharmacy);
        int producer = producersDailyProduction.get(indexOfProducer);

        updateBasedOnRowOrColumn(indexOfPharmacy, indexOfProducer, pharmacy, producer);
    }

    private void update() {
        if (valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInColumns() > valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInRows()) {
            updateBasedOnColumn();
        } else if (valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInColumns() < valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInRows()) {

            updateBasedOnRow();
        } else if (valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInColumns() == valueInIndexWithMaxDifferenceOfTheTwoSmallestValuesInRows()) {
            double minimalInRow = returnMinimalCostInDifferencesOfTheTwoSmallestValuesInRows();
            double minimalInColumn = returnMinimalCostInDifferencesOfTheTwoSmallestValuesInColumns();

            if (minimalInColumn <= minimalInRow) {
                updateBasedOnColumn();
            } else if (minimalInColumn > minimalInRow) {
                updateBasedOnRow();
            }
        }
        fillListOfDifferencesOfTheTwoSmallestValuesInColumns();
        fillListOfDifferencesOfTheTwoSmallestValuesInRows();
    }

    public double getSumOfTotalCosts() {
        return sumOfTotalCosts;
    }

    public void optimize() {
        fillMatricesAndLists();
        fillOriginalMatrixOfCosts();

        while (true) {
            int count = 0;
            for (ArrayList<Double> arrayList : matrixOfCostsToBeModified) {
                for (Double aDouble : arrayList) {
                    if (aDouble != signalThatIndexInMatrixIsDeleted) {
                        count++;
                    }
                    if (count > 1) break;
                }
                if (count > 1) break;
            }

            if (count == 1 || count == 0) {
                break;
            } else {
                update();
            }
        }

        int row = 0;
        int column = 0;
        for (int i = 0; i < matrixOfCostsToBeModified.size(); i++) {
            ArrayList<Double> arrayList = matrixOfCostsToBeModified.get(i);
            for (int j = 0; j < arrayList.size(); j++) {
                if (arrayList.get(j) != signalThatIndexInMatrixIsDeleted) {
                    row = i;
                    column = j;
                    break;
                }
            }
        }

        int before = resultMatrixOfAmountsThatNeedToBePurchased.get(row).get(column);
        resultMatrixOfAmountsThatNeedToBePurchased.get(row).set(column, before + pharmacyDailyDemand.get(row));

        double sum = 0;
        for (int i = 0; i < originalMatrixOfCosts.size(); i++) {
            ArrayList<Double> arrayList = originalMatrixOfCosts.get(i);
            for (int j = 0; j < arrayList.size(); j++) {
                sum += arrayList.get(j) * resultMatrixOfAmountsThatNeedToBePurchased.get(i).get(j);
            }
        }
        sumOfTotalCosts = sum;
        printToFileDistributionsOfPharmaciesPurchases();
    }

    private void printToFileDistributionsOfPharmaciesPurchases() {
        try {
            FileWriter fw = new FileWriter("optimization.txt");
            int index = 0;
            int pharmacyIndex = 0;
            for (ArrayList<Integer> arrayList : resultMatrixOfAmountsThatNeedToBePurchased) {
                for (Integer integer : arrayList) {
                    if (integer != 0) {
                        fw.write(producers.get(connections.get(index).getIdProducer()).getName()
                                + " -> " + pharmacies.get(pharmacyIndex).getName()
                                + " [Koszt = " + integer + " * " + connections.get(index).getCost()
                                + " = " + integer * connections.get(index).getCost() + " zł]"
                        );

                        if (integer > connections.get(index).getDailyMaximumNumber()) {
                            fw.write("\n Amount of vaccines to buy is bigger than connection daily maximum number!");
                        }
                        fw.write('\n');
                    }
                    index++;
                }
                pharmacyIndex++;
            }
            fw.write("Oplaty calkowite: " + sumOfTotalCosts + " zł");

            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double findMinimal(ArrayList<Double> doubles) {
        double min = doubles.get(0);
        for (int i = 1; i < doubles.size(); i++) {
            if (doubles.get(i) < min) {
                min = doubles.get(i);
            }
        }
        return min;
    }

    private int findMinimalForIntegers(ArrayList<Integer> doubles) {
        int min = doubles.get(0);
        for (int i = 1; i < doubles.size(); i++) {
            if (doubles.get(i) < min) {
                min = doubles.get(i);
            }
        }
        return min;
    }

    private double findMaximum(ArrayList<Double> doubles) {
        double max = -1;
        for (Double aDouble : doubles) {
            if (aDouble != signalThatIndexInMatrixIsDeleted && aDouble > max) {
                max = aDouble;
            }
        }
        return max;
    }

    private double findSecondMinimal(ArrayList<Double> doubles) {
        QuickSortRecursive quickSortRecursive = new QuickSortRecursive();
        if (doubles.size() == 1) {
            return doubles.get(0);
        }
        double[] values = new double[doubles.size()];
        for (int i = 0; i < doubles.size(); i++) {
            values[i] = doubles.get(i);
        }
        values = quickSortRecursive.sort(values);
        return values[1];
    }

}