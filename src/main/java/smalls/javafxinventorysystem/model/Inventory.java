package smalls.javafxinventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Scanner;

public class Inventory {

    private static final String INVENTORY_FILE = "src/main/java/smalls/javafxinventorysystem/parts.txt";
    private static final String PRODUCT_INVENTORY = "src/main/java/smalls/javafxinventorysystem/products.txt";
    private static Inventory inv = null;
    private int nextId = 1001;
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    Comparator<Part> comparePartsById = (p1, p2) -> p1.getId() - p2.getId();
    Comparator<Product> compareProductsById = (p1, p2) -> p1.getId() - p2.getId();


    /**
     * <code>Inventory</code> class private singleton class
     * constructor
     */
    private Inventory() {
    }

    /**
     * returns the <code>Inventory</code> class instance
     *
     * @return the singleton <code>Inventory</code> class
     * instance
     */
    public static Inventory getInstance() {
        if (inv == null) {
            inv = new Inventory();
        }
        return inv;
    }

    /**
     * adds a part to the <code>allParts</code>
     * <code>ObservableList</code>.
     *
     * @param newPart the part to add
     */
    public void addPart(Part newPart) {
        allParts.add(newPart);
        nextId++;
    }

    /**
     * adds a product to the <code>allProducts</code>
     * <code>ObservableList</code>
     *
     * @param newProduct the product to add
     */
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
        nextId++;
    }

    /**
     * returns <code>nextId</code> to be
     * assigned to the next part or product.
     *
     * @return the <code>nextId</code>
     */
    public int getNextId() {
        return nextId;
    }

    /**
     * searches for a part by <code>id</code>.
     *
     * @param partId the <code>id</code> to look up
     * @return the <code>Part</code> matching the <code>id</code>
     */
    public Part lookupPart(int partId) {
        allParts.sort(comparePartsById);
        Part part = searchPartsById(partId);

        return part;
    }

    /**
     * searches for a product by <code>id</code>.
     *
     * @param productId the <code>productId</code> to look up
     * @return the <code>Product</code> matching the <code>id</code>
     */
    public Product lookupProduct(int productId) {
        allProducts.sort(compareProductsById);
        Product product = searchProductsById(productId);

        return product;
    }

    /**
     * searches for a <code>Part</code> by name.
     * returns a <code>FilteredList</code>
     * of parts whose name contains the search string.
     *
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
     * searches for a <code>Product</code> by name.
     * returns a <code>FilteredList</code>
     * of products whose name contains the search string.
     *
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
     * replaces the <code>Part</code> at <code>index</code>
     * of <code>allParts</code> array with <code>selectedPart</code>.
     *
     * @param index the index of the <code>Part</code> to be updated
     * @param selectedPart the updated <code>Part</code>
     */
    public void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * replaces the <code>Product</code> at <code>index</code>
     * of <code>allProducts</code> array with <code>newProduct</code>
     *
     * @param index the index of the <code>Product</code> to be updated
     * @param newProduct the updated <code>Product</code>
     */
    public void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * removes <code>selectedPart</code> from <code>allParts</code>
     * <code>ObservableList</code>
     *
     * @param selectedPart the <code>Part</code> to be deleted
     * @return <code>true</code> if the part is removed, <code>false</code> if not
     */
    public boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * removes <code>selectedProduct</code> from <code>allProducts</code>
     * <code>ObservableList</code>
     *
     * @param selectedProduct the <code>Product</code> to be deleted
     * @return <code>true</code> if the <code>Product</code> is deleted, <code>false</code> if not
     */
    public boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * gets the list of all <code>InHouse</code> and <code>Outsourced</code> parts.
     *
     * @return the <code>allParts</code> <code>ObservableList</code>
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * gets the <code>Product</code> ObservableList
     *
     * @return the <code>allProducts</code> <code>ObservableList</code>
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * binary search helper method called by
     * lookup-part-by-id method
     *
     * @param id the <code>id</code> to be searched for
     * @param <T> generic type parameter representing <code>InHouse</code> or
     *           <code>Outsourced</code> parts
     * @return the part matching the id
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
     * binary search helper method called by
     * lookup-product-by-id method
     *
     * @param id the <code>id</code> to be searched for
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

    public void loadProducts() {
        String[] record;
        try (Scanner inFile = new Scanner(new FileInputStream(PRODUCT_INVENTORY))) {
            while (inFile.hasNextLine()) {
                record = inFile.nextLine().split(",");
                Product p = new Product(Integer.parseInt(record[0]), record[1], Double.parseDouble(record[2]),
                        Integer.parseInt(record[3]), Integer.parseInt(record[4]), Integer.parseInt(record[5]));
                inv.addProduct(p);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
