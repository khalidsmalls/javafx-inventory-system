package smalls.javafxinventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.util.Comparator;
import java.util.Properties;
import java.sql.*;

public class Inventory {
    private static Inventory inv = null;
    private final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private final ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private final Comparator<Part> comparePartsById = (p1, p2) -> p1.getId() - p2.getId();
    private final Comparator<Product> compareProductsById = (p1, p2) -> p1.getId() - p2.getId();
    private PreparedStatement stmt;
    private Connection conn;

    private Inventory() {
        initDb();
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
        try {
            String query = "INSERT INTO part (name, price, inventory, min_stock, max_stock) " +
                    "VALUES (?,?,?,?,?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, newPart.getName());
            stmt.setDouble(2,newPart.getPrice());
            stmt.setInt(3, newPart.getStock());
            stmt.setInt(4, newPart.getMin());
            stmt.setInt(5, newPart.getMax());
            stmt.executeQuery();

            if (newPart instanceof InHouse) {
                query = "INSERT INTO in_house (part_id, machine_id) VALUES (?,?)";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, newPart.getId());
                stmt.setInt(2, ((InHouse) newPart).getMachineId());
                stmt.executeQuery();
            }
            if (newPart instanceof Outsourced) {
                query = "INSERT INTO outsourced (part_id, company_name) VALUES (?,?)";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, newPart.getId());
                stmt.setString(2, ((Outsourced) newPart).getCompanyName());
                stmt.executeQuery();
            }
            allParts.add(newPart);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//END of addPart

    /**
     *
     * @param newProduct the product to add
     */
    public void addProduct(Product newProduct) {
        try {
            String query = "INSERT INTO product (name, price, inventory, min_stock, " +
                    "max_stock) VALUES (?,?,?,?,?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, newProduct.getName());
            stmt.setDouble(2, newProduct.getPrice());
            stmt.setInt(3, newProduct.getStock());
            stmt.setInt(4, newProduct.getMin());
            stmt.setInt(5, newProduct.getMax());
            stmt.executeQuery();

            if (newProduct.getAllAssociatedParts().size() > 0) {
                query = "INSERT INTO product_assoc_parts (product_id, part_id) VALUES (?,?)";
                stmt = conn.prepareStatement(query);
                for (Part p : newProduct.getAllAssociatedParts()) {
                    stmt.setInt(1, newProduct.getId());
                    stmt.setInt(2, p.getId());
                    stmt.executeQuery();
                }
            }
            allProducts.add(newProduct);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//END of addProduct

    /**
     * @param partId the partId to look up
     * @return the part matching the id
     */
    public Part lookupPart(int partId) {
        allParts.sort(comparePartsById);

        return searchPartsById(partId);
    }

    /**
     * @param productId the productId to look up
     * @return the product matching the id
     */
    public Product lookupProduct(int productId) {
        allProducts.sort(compareProductsById);

        return searchProductsById(productId);
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
            return part.getName().toLowerCase().contains((lowerCaseFilter));
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
            return product.getName().toLowerCase().contains(lowerCaseFilter);
        });
        return filteredProducts;
    }

    /**
     * @param index the index of the part to be updated
     * @param selectedPart the part to be updated
     */
    public void updatePart(int index, Part selectedPart) {
        try {
            String query = "UPDATE part SET part_id=?, name=?, price=?, inventory=?," +
                    "min_stock=?, max_stock=? WHERE part_id=?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, selectedPart.getId());
            stmt.setString(2, selectedPart.getName());
            stmt.setDouble(3, selectedPart.getPrice());
            stmt.setInt(4, selectedPart.getStock());
            stmt.setInt(5, selectedPart.getMin());
            stmt.setInt(6, selectedPart.getMax());
            stmt.setInt(7, selectedPart.getId());
            stmt.executeQuery();

            if (selectedPart instanceof InHouse) {
                query = "UPDATE in_house SET machine_id=? WHERE part_id=?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, ((InHouse) selectedPart).getMachineId());
                stmt.setInt(2, selectedPart.getId());
                stmt.executeQuery();
                allParts.set(index, selectedPart);
            }
            if (selectedPart instanceof Outsourced) {
                query = "UPDATE outsourced SET company_name=? WHERE part_id=?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, ((Outsourced) selectedPart).getCompanyName());
                stmt.setInt(2, selectedPart.getId());
                stmt.executeQuery();
                allParts.set(index, selectedPart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param index the index of the product to be updated
     * @param newProduct the product to be updated
     */
    public void updateProduct(int index, Product newProduct) {
        try {
            String query = "UPDATE product SET product_id=?, name=?, price=?, " +
                    "inventory=?, min_stock=?, max_stock=? " +
                    "WHERE product_id=?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, newProduct.getId());
            stmt.setString(2, newProduct.getName());
            stmt.setDouble(3, newProduct.getPrice());
            stmt.setInt(4, newProduct.getStock());
            stmt.setInt(5, newProduct.getMin());
            stmt.setInt(6, newProduct.getMax());
            stmt.setInt(7, newProduct.getId());
            stmt.executeQuery();

            query = "DELETE FROM product_assoc_parts WHERE product_id=?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, newProduct.getId());
            stmt.executeQuery();

            query = "INSERT INTO product_assoc_parts (product_id, part_id) VALUES (?,?)";
            stmt = conn.prepareStatement(query);
            for (Part p : newProduct.getAllAssociatedParts()) {
                stmt.setInt(1, newProduct.getId());
                stmt.setInt(2, p.getId());
                stmt.executeQuery();
            }
            allProducts.set(index, newProduct);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//END of updateProduct

    /**
     * @param selectedPart the part to be deleted
     * @return true if the part is removed, false if not
     */
    public boolean deletePart(Part selectedPart) {
        try {
            String query;
            if (selectedPart instanceof InHouse) {
                query = "DELETE FROM in_house WHERE part_id=?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, selectedPart.getId());
                stmt.executeQuery();
            }
            if (selectedPart instanceof Outsourced) {
                query = "DELETE FROM outsourced WHERE part_id=?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, selectedPart.getId());
                stmt.executeQuery();
            }
            query = "DELETE FROM part WHERE part_id=?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, selectedPart.getId());
            stmt.executeQuery();
            return allParts.remove(selectedPart);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param selectedProduct the product to be deleted
     * @return true if the product is deleted, false if not
     */
    public boolean deleteProduct(Product selectedProduct) {
        String query = "DELETE FROM product WHERE product_id=?";
        try {
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, selectedProduct.getId());
            stmt.executeQuery();
            return allProducts.remove(selectedProduct);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return the part observable list
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return the product observable list
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

    public void initDb() {
        try {
            Properties info = new Properties();
            info.put("user", "khalid");
            info.put("password", "");

            String dbUrl = "jdbc:mariadb://localhost:3306/javafx_inventory_system";
            conn = DriverManager.getConnection(dbUrl, info);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the next part_id from part table
     */
    public int getNextPartId() {
        int nextId = -1;
        try {
            String query = "SELECT AUTO_INCREMENT FROM " +
                    "information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = \"javafx_inventory_system\" " +
                    "AND TABLE_NAME = \"part\"";
            stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                nextId = result.getInt("AUTO_INCREMENT");
            }
            return nextId;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

    public int getNextProductId() {
        int nextId = -1;
        try {
            String query = "SELECT AUTO_INCREMENT FROM " +
                    "information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = \"javafx_inventory_system\" " +
                    "AND TABLE_NAME = \"product\"";
            stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                nextId = result.getInt("AUTO_INCREMENT");
            }
            return nextId;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

    public void loadParts() {
        try {
            String query = "SELECT part.part_id, name, price, inventory, min_stock, max_stock," +
                    "machine_id, company_name FROM part " +
                    "LEFT JOIN in_house ON part.part_id=in_house.part_id " +
                    "LEFT JOIN outsourced ON part.part_id=outsourced.part_id";
            stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                String part_id = result.getString("part_id");
                String name = result.getString("name");
                String price = result.getString("price");
                String inventory = result.getString("inventory");
                String min_stock = result.getString("min_stock");
                String max_stock = result.getString("max_stock");

                if (result.getString("machine_id") != null) {
                    String machine_id = result.getString("machine_id");
                    allParts.add(new InHouse(
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
                    allParts.add(new Outsourced(
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
    }//END of loadParts

    public void loadProducts() {
        try {
            String query = "SELECT * FROM product";
            stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                String product_id = result.getString("product_id");
                String name = result.getString("name");
                String price = result.getString("price");
                String inventory = result.getString("inventory");
                String min_stock = result.getString("min_stock");
                String max_stock = result.getString("max_stock");
                allProducts.add(new Product(
                        Integer.parseInt(product_id),
                        name,
                        Double.parseDouble(price),
                        Integer.parseInt(inventory),
                        Integer.parseInt(min_stock),
                        Integer.parseInt(max_stock)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}//END of class
