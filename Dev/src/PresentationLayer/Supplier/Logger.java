package PresentationLayer.Supplier;

public class Logger {
    public static void printE(String error) {
        if (error == null) error = "null";
        System.out.println("\u001B[31m[ERROR] " + error + "\u001B[0m");
    }

    public static void printSuccess(String message) {
        if (message == null) message = "null";
        System.out.println("\u001B[32m[SUCCESS] " + message + "\u001B[0m");
    }
    public static void print(String msg){
        System.out.println(msg);
    }
}
