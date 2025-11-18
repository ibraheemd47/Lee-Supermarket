package PresentationLayer.Supplier.Controllers;

import ServiceLayer.ServiceFactory;

public class ServiceController {
    private static ServiceController instance = null;

    private ServiceFactory serviceFactory;
    private ServiceController() {
        serviceFactory = ServiceFactory.getInstance();
    }

    public static ServiceController getInstance() {
        if (instance == null) {
            instance = new ServiceController();
        }
        return instance;
    }

    public void initialize() {
        serviceFactory.getFactory().Initialize();
    }

    public ServiceFactory getServiceFactory() {
        return serviceFactory;
    }
}
