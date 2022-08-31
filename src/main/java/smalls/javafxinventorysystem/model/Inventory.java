package smalls.javafxinventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.util.Comparator;
import java.util.Properties;
import java.sql.*;

public class Inventory {
    private static Inventory inv = null;
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private final Comparator<Part> comparePartsById = (p1, p2) -> p1.getId() - p2.getId();
    private final Comparator<Product> compareProductsById = (p1, p2) -> p1.getId() - p2.getId();
    private Statement stmt;
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
            stmt = conn.createStatement();
            StringBuilder query = new StringBuilder("INSERT INTO part (name, price, inventory, min_stock, max_stock) VALUES (\"")
                    .append(newPart.getName()).append("\", ")
                    .append(String.valueOf(newPart.getPrice())).append(", ")
                    .append(String.valueOf(newPart.getStock())).append(", ")
                    .append(String.valueOf(newPart.getMin())).append(", ")
                    .append(String.valueOf(newPart.getMax())).append("); ");
            stmt.executeUpdate(query.toString());

            if (newPart instanceof InHouse) {
                query = new StringBuilder("INSERT INTO in_house (part_id, machine_id) VALUES (")
                        .append(String.valueOf(newPart.getId())).append(", ")
                        .append(String.valueOf(((InHouse) newPart).getMachineId())).append(")");
                stmt.executeUpdate(query.toString());
            }
            if (newPart instanceof Outsourced) {
                query = new StringBuilder("INSERT INTO outsourced (part_id, company_name) VALUES (")
                        .append(String.valueOf(newPart.getId())).append(", \"")
                        .append(((Outsourced) newPart).getCompanyName()).append("\")");
                stmt.executeUpdate(query.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        allParts.add(newPart);
    }//END of addPart

    /**
     *
     * @param newProduct the product to add
     */
    public void addProduct(Product newProduct) {
        try {
            stmt = conn.createStatement();
            StringBuilder query = new StringBuilder("INSERT INTO product (name, price, inventory, " +
                    "min_stock, max_stock) VALUES (")
                    .append("\"").append(newProduct.getName()).append("\", ")
                    .append(newProduct.getPrice()).append(", ")
                    .append(newProduct.getStock()).append(", ")
                    .append(newProduct.getMin()).append(", ")
                    .append(newProduct.getMax()).append(")");
            stmt.executeUpdate(query.toString());

            if (newProduct.getAllAssociatedParts().size() > 0) {
                query = new StringBuilder("INSERT INTO product_assoc_parts (product_id, part_id) VALUES ");
                for (int i = 0; i < newProduct.getAllAssociatedParts().size() - 1; i++) {
                    query.append("(").append(newProduct.getId()).append(", ")
                            .append(inv.getAllParts().get(i).getId()).append("), ");
                }
                query.append("(").append(newProduct.getId()).append(", ")
                        .append(inv.getAllParts().get(inv.getAllParts().size() - 1).getId())
                                .append(");");
                stmt.executeUpdate(query.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        allProducts.add(newProduct);
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
            stmt = conn.createStatement();
            String query = "UPDATE part " +
                    "SET name=\"" + selectedPart.getName() + "\"," +
                    "price=" + selectedPart.getPrice() + ", " +
                    "inventory=" + selectedPart.getStock() + ", " +
                    "min_stock=" + selectedPart.getMin() + ", " +
                    "max_stock=" + selectedPart.getMax() +  " " +
                    "WHERE part_id=" + selectedPart.getId();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        allParts.set(index, selectedPart);
    }

    /**
     * @param index the index of the product to be updated
     * @param newProduct the product to be updated
     */
    public void updateProduct(int index, Product newProduct) {
        try {
            stmt = conn.createStatement();
            String query = "UPDATE product " +
                    "SET name=\"" + newProduct.getName() + "\"," +
                    "price=" + newProduct.getPrice() + ", " +
                    "inventory=" + newProduct.getStock() + ", " +
                    "min_stock=" + newProduct.getMin() + ", " +
                    "max_stock=" + newProduct.getMax() + " " +
                    "WHERE product_id=" + newProduct.getId();
            stmt.executeUpdate(query);
            if (newProduct.getAllAssociatedParts().size() > 0) {
                query = "DELETE FROM product_assoc_parts WHERE " +
                        "product_id=" + newProduct.getId();
                stmt.executeUpdate(query);
                for (Part p : newProduct.getAllAssociatedParts()) {
                    query = "INSERT INTO product_assoc_parts (product_id, part_id) VALUES " +
                            "(" + newProduct.getId() + ", " + p.getId() + ")";
                    stmt.executeUpdate(query);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        allProducts.set(index, newProduct);
    }//END of updateProduct

    /**
     * @param selectedPart the part to be deleted
     * @return true if the part is removed, false if not
     */
    public boolean deletePart(Part selectedPart) {
        try {
            stmt = conn.createStatement();
            String query;
            if (selectedPart instanceof InHouse) {
                query = "DELETE FROM in_house WHERE part_id=" + selectedPart.getId();
                stmt.executeUpdate(query);
            }
            if (selectedPart instanceof Outsourced) {
                query = "DELETE FROM outsourced WHERE part_id=" + selectedPart.getId();
                stmt.executeUpdate(query);
            }
            query = "DELETE FROM part WHERE part_id=" + selectedPart.getId();
            stmt.executeUpdate(query);
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
        try {
            stmt = conn.createStatement();
            String query = "DELETE FROM product WHERE " +
                    "product_id=" + selectedProduct.getId();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return the part list
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

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
    public int getNextId() {
        int nextId = -1;
        try {
            stmt = conn.createStatement();
            String query = "SELECT AUTO_INCREMENT FROM " +
                    "information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = \"javafx_inventory_system\" " +
                    "AND TABLE_NAME = \"part\"";
            ResultSet result = stmt.executeQuery(query);

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
            stmt = conn.createStatement();
            String query = "SELECT part.part_id, name, price, inventory, min_stock, max_stock," +
                    "machine_id, company_name FROM part " +
                    "LEFT JOIN in_house ON part.part_id=in_house.part_id " +
                    "LEFT JOIN outsourced ON part.part_id=outsourced.part_id";
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
            stmt = conn.createStatement();
            String query = "SELECT * FROM product";
            ResultSet result = stmt.executeQuery(query);
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
