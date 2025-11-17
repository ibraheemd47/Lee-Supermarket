import Inventory.PresentationLayer.Inventory.ManagerUI;
import Inventory.ServiceLayer.Inventory.ServiceFactory;

import java.util.Scanner;


public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        ServiceFactory serviceFactory = new ServiceFactory();

        ManagerUI Program = new ManagerUI(serviceFactory);
        Program.showMainMenu();
    }
}

