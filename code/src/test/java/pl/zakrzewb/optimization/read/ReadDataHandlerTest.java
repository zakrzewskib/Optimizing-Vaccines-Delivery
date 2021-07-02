package pl.zakrzewb.optimization.read;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ReadDataHandlerTest {
    ReadDataHandler readDataHandler;

    @Before
    public void setUp() {
        readDataHandler = new ReadDataHandler();
    }

    @Test(expected = AssertionError.class)
    public void testNegativeCost() throws IOException {
        readDataHandler.readDataFromFileToListsOfObjects("wrongData/negative_at_line_3_data1.txt");
        assert false;
    }

    @Test(expected = AssertionError.class)
    public void testNegativeCost2() throws IOException {
        readDataHandler.readDataFromFileToListsOfObjects("wrongData/negative_at_line_200_data2.txt");
        assert false;
    }

    @Test(expected = IOException.class)
    public void testDailyDemandTooBig() throws IOException {
        readDataHandler.readDataFromFileToListsOfObjects("wrongData/daily_demand_is_too_big_data1.txt");
        assert false;
    }

}