package smalls.javafxinventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Properties;
import java.util.Scanner;
import java.sql.*;

public class Inventory {

    private static final String INVENTORY_FILE = "src/main/java/smalls/javafxinventorysystem/parts.txt";
    private static Inventory inv = null;
    private int nextId = 1001;
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private final Comparator<Part> comparePartsById = (p1, p2) -> p1.getId() - p2.getId();
    private final Comparator<Product> compareProductsById = (p1, p2) -> p1.getId() - p2.getId();

//    private Connection conn;
//    private Statement stmt;
//    private final String db_url = "jdbc:mariadb://localhost:3306/javafx_inventory_system";
//    private final String driver = "org.mariadb.jdbc.Driver";
//    private Properties info;

    private Inventory() {
    }

    public static Inventory getInstance() {
        if (inv == null) {
            inv = new Inventory();
        }
        return inv;
    }

    /**
     *
     * @param newPart the part to add
     */
    public void addPart(Part newPart) {
        allParts.add(newPart);
        nextId++;
    }

    /**
     *
     * @param newProduct the product to add
     */
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
        nextId++;
    }

    /**
     * @return the next id
     */
    public int getNextId() {
        return nextId;
    }

    /**
     * @param partId the partId to look up
     * @return the part matching the id
     */
    public Part lookupPart(int partId) {
        allParts.sort(comparePartsById);
        Part part = searchPartsById(partId);

        return part;
    }

    /**
     * @param productId the productId to look up
     * @return the product matching the id
     */
    public Product lookupProduct(int productId) {
        allProducts.sort(compareProductsById);
        Product product = searchProductsById(productId);

        return product;
    }

    /**
     * @param name the part name string to look up
     * @return the part or parts matching the part name string
     */
    public ObservableList<Part> lookupPart(String name) {
        FilteredList<Part> filteredParts = new FilteredList<>(getAllParts(), p -> true);
        filteredParts.setPredicate(part -> {
            if (name == null || name.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = name.toLowerCase();
            if (part.getName().toLowerCase().contains((lowerCaseFilter))) {
                return true;
            }
            return false;
        });
        return filteredParts;
    }

    /**
     * @param name the product name string to look up
     * @return the product or products matching the name string
     */
    public ObservableList<Product> lookupProduct(String name) {
        FilteredList<Product> filteredProducts = new FilteredList<>(getAllProducts(), p -> true);
        filteredProducts.setPredicate(product -> {
            if (name == null || name.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = name.toLowerCase();
            if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
        });
        return filteredProducts;
    }

    /**
     * @param index the index of the part to be updated
     * @param selectedPart the part to be updated
     */
    public void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * @param index the index of the product to be updated
     * @param newProduct the product to be updated
     */
    public void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * @param selectedPart the part to be deleted
     * @return true if the part is removed, false if not
     */
    public boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * @param selectedProduct the product to be deleted
     * @return true if the product is deleted, false if not
     */
    public boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return the part list
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }


//    public ObservableList<Part> getAllParts() {
//
//    }


    /**
     * @return the products list
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * the following binary search helper methods
     * are called by the lookup-by-id methods above
     *
     * @param id the id to be searched for
     * @return the part matching the id
     * @param <T> generic type parameter representing InHouse or
     *           Outsourced parts
     */
    private <T extends Part> T searchPartsById(int id) {
        T part = null;
        int start = 0;
        int end = allParts.size() - 1;
        int mid = (start + end) / 2;

        while(start <= end) {
            if (allParts.get(mid).getId() < id) {
                start = mid + 1;
            } else if(allParts.get(mid).getId() == id) {
                part = (T) allParts.get(mid);
                break;
            } else {
                end = mid - 1;
            }
            mid = (start + end) / 2;
        }
        return part;
    }

    /**
     * @param id the id to be searched for
     * @return the product matching the id
     */
    private Product searchProductsById(int id) {
        Product product = null;
        int start = 0;
        int end = allProducts.size() - 1;
        int mid = (start + end) / 2;

        while(start <= end) {
            if (allProducts.get(mid).getId() < id) {
                start = mid + 1;
            } else if(allProducts.get(mid).getId() == id) {
                product = allProducts.get(mid);
                break;
            } else {
                end = mid - 1;
            }
            mid = (start + end) / 2;
        }
        return product;
    }

    public void loadParts() {
        String[] record;
        try (Scanner inFile = new Scanner(new FileInputStream(INVENTORY_FILE))) {
            while (inFile.hasNextLine()) {
                record = inFile.nextLine().split(",");
                Part part = new InHouse(Integer.parseInt(record[0]), record[1], Double.parseDouble(record[2]),
                        Integer.parseInt(record[3]), Integer.parseInt(record[4]), Integer.parseInt(record[5]),
                        Integer.parseInt(record[6]));
                inv.addPart(part);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void dbTest() {
        Connection conn = null;
        try {
            String url = "jdbc:mariadb://localhost:3306/javafx_inventory_system";
            Properties info = new Properties();
            info.put("user", "khalid");
            info.put("password", "");

            //load and register driver
            //Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("loaded and registered driver");

            conn = DriverManager.getConnection(url, info);

            if (conn != null) {
                System.out.println("Success!");
            }
            assert conn != null;
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
