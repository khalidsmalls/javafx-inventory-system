package smalls.javafxinventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Properties;

public class Product {

    private final ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        associatedParts = FXCollections.observableArrayList();
        loadAssocParts();
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param part the part to add to associated parts
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    public void addAssociatedParts(ObservableList<Part> parts) {
        associatedParts.addAll(parts);
    }

    /**
     * @param selectedAssociatedPart the part to remove from associated parts
     * @return true if part is removed, false if not
     */
    public boolean deleteAssociatedParts(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    public boolean deleteAssociatedParts(ObservableList<Part> selectedAssociatedParts) {
        return associatedParts.removeAll(selectedAssociatedParts);
    }

    /**
     * @return the associated parts
     */

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    public void loadAssocParts() {
        Statement stmt;
        Connection conn;
        try {
            Properties info = new Properties();
            info.put("user", "khalid");
            info.put("password", "");

            String dbUrl = "jdbc:mariadb://localhost:3306/javafx_inventory_system";
            conn = DriverManager.getConnection(dbUrl, info);
            stmt = conn.createStatement();
            String query = "SELECT product_assoc_parts.part_id, name, price, inventory, min_stock, max_stock, machine_id, company_name " +
                    "FROM product_assoc_parts JOIN part ON product_assoc_parts.part_id=part.part_id " +
                    "LEFT JOIN in_house ON product_assoc_parts.part_id=in_house.part_id " +
                    "LEFT JOIN outsourced ON product_assoc_parts.part_id=outsourced.part_id " +
                    "WHERE product_id=" + this.getId();

            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                String part_id = result.getString("part_id");
                String name = result.getString("name");
                String price = result.getString("price");
                String inventory = result.getString("inventory");
                String min_stock = result.getString("min_stock");
                String max_stock = result.getString("max_stock");

                if (result.getString("machine_id") != null) {
                    String machine_id = result.getString("machine_id");
                    associatedParts.add(new InHouse(
                            Integer.parseInt(part_id),
                            name,
                            Double.parseDouble(price),
                            Integer.parseInt(inventory),
                            Integer.parseInt(min_stock),
                            Integer.parseInt(max_stock),
                            Integer.parseInt(machine_id)));
                }
                if (result.getString("company_name") != null) {
                    String company_name = result.getString("company_name");
                    associatedParts.add(new Outsourced(
                            Integer.parseInt(part_id),
                            name,
                            Double.parseDouble(price),
                            Integer.parseInt(inventory),
                            Integer.parseInt(min_stock),
                            Integer.parseInt(max_stock),
                            company_name
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//END of load assocParts
}
