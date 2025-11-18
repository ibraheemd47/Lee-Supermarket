package domainLayer.Supplier.Managers;


import ObjectDTO.Supplier.ProductDTO;
import domainLayer.Supplier.Objects.Product;
import domainLayer.Supplier.Repository;
import domainLayer.Supplier.RepositoryIMP;

import java.util.HashMap;
import java.util.Map;

public class ProductManager {
    private final Map<String, Product> productsById = new HashMap<>();  //<product id , product>
    private final Map<String, Product> productsByName = new HashMap<>();//<product name , product>
    Repository dataBase = RepositoryIMP.getInstance();
    public String addProduct(ProductDTO product) {
        if (product == null || product.toString() == null) {
            throw new IllegalArgumentException("Cannot add null product.");
        }
        Product productToAdd = new Product(product);
        productsById.put(product.getId(), productToAdd);
        productsByName.put(product.getName(), productToAdd);

        //adding to data base
        dataBase.insert_product(product);
        return "Product " + product.getId() + " added successfully!!";
    }
    public void addProduct(Product product) {
        if (product == null || product.toString() == null) {
            throw new IllegalArgumentException("Cannot add null product.");
        }

        productsById.put(product.getId(), product);
        productsByName.put(product.getName(), product);
    }

    public ProductDTO getProductById(String id) {
        if (productsById.get(id)!=null)
            return productsById.get(id).convertToDTO();

        //get it from the data base
        ProductDTO productDTO =dataBase.get_product(id);
        return productDTO;
    }

    public Product getProductByName(String name) {
        if( productsByName.get(name)!=null)
            return productsByName.get(name);

        //get from the data base
        return dataBase.get_product_nt_name(name);
    }
    public ProductDTO getProductDTOByName(String name) {
        
         Product product = getProductByName(name);
        if (product == null) {
            return null;
        }
        return product.convertToDTO();
    }


    public boolean removeProductById(String id) {
        boolean removed = productsById.remove(id) != null;


        //remove from the data base
        dataBase.remove_product(id);
        return removed;
    }

    public Map<String, Product> getAllProducts() {
        return dataBase.get_all_product();
    }

    public boolean exists(String id) {
        if(productsById.containsKey(id))
            return true ;


        //check also the data base
        return dataBase.exist_product(id);
    }
}
