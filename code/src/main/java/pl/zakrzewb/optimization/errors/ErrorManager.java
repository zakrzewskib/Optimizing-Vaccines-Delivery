package pl.zakrzewb.optimization.errors;

public class ErrorManager implements ErrorManagerInterface {
    @Override
    public void throwMyException(String message) {
        System.out.println(message);
        // Print to errors.txt
        throw new IllegalArgumentException(message);
    }
}