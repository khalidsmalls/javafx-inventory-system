package smalls.javafxinventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Scanner;

/**
 * static <code>Inventory</code> class maintains the
 * <code>Part</code> and <code>Product</code> observable lists.
 * <p>
 * has static methods available to search for, add, modify,
 * and delete parts and products.
 *
 * @author Khalid Smalls
 */
public class Inventory {

    private static final String INVENTORY_FILE = "src/main/java/smalls/javafxinventorysystem/parts.txt";
    private static final String PRODUCT_INVENTORY = "src/main/java/smalls/javafxinventorysystem/products.txt";

    /**
     * part id's start at 1000 and incremenet by two
     * so they will always be even integers.
     */
    private static int nextPartId = 1000;

    /**
     * product id's start at 1001 and increment by two
     * so they will always be odd integers.
     */
    private static int nextProductId = 1001;

    /**
     * <code>ObservableList</code> of <code>InHouse</code>
     * and <code>Outsourced</code> part objects.
     */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * <code>ObservableList</code> of <code>Product</code> objects.
     */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * functional interface used to compare parts by id number.
     */
    private static final Comparator<Part> comparePartsById = (p1, p2) -> p1.getId() - p2.getId();

    /**
     * functional interface used to compare products by id number.
     */
    private static final Comparator<Product> compareProductsById = (p1, p2) -> p1.getId() - p2.getId();


    /**
     * adds a part to the <code>allParts</code>
     * <code>ObservableList</code> and increments
     * <code>nextPartId</code> by two.
     *
     * @param newPart the part to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
        nextPartId += 2;
    }

    /**
     * adds a product to the <code>allProducts</code>
     * <code>ObservableList</code> and increments
     * <code>nextProductId</code> by two.
     *
     * @param newProduct the product to add
     */
    public static  void addProduct(Product newProduct) {
        allProducts.add(newProduct);
        nextProductId += 2;
    }

    /**
     * returns <code>nextPartId</code> to be
     * assigned to the next part.
     *
     * @return the <code>nextPartId</code>
     */
    public static int getNextPartId() {
        return nextPartId;
    }

    /**
     * returns <code>nextProductId</code> to be
     * assigned to the next product.
     *
     * @return the <code>nextProductId</code>
     */
    public static int getNextProductId() { return nextProductId; }

    /**
     * searches for a part by <code>id</code>.
     *
     * @param partId the <code>id</code> to look up
     * @return the <code>Part</code> matching the <code>id</code>
     */
    public static Part lookupPart(int partId) {
        allParts.sort(comparePartsById);

        return searchPartsById(partId);
    }

    /**
     * searches for a <code>Product</code> by <code>id</code>.
     *
     * @param productId the <code>id</code> to look up
     * @return the <code>Product</code> matching the <code>id</code>
     */
    public static Product lookupProduct(int productId) {
        allProducts.sort(compareProductsById);

        return searchProductsById(productId);
    }

    /**
     * searches for a <code>Part</code> by name.
     * <p>
     * returns a <code>FilteredList</code>
     * of parts whose name contains the search string.
     *
     * @param name the part name string to look up
     * @return the part or parts matching the part name string
     */
    public static ObservableList<Part> lookupPart(String name) {
        FilteredList<Part> filteredParts = new FilteredList<>(getAllParts(), p -> true);
        filteredParts.setPredicate(part -> {
            if (name == null || name.isEmpty()) {
                return true;
            }
            return part.getName().toLowerCase().contains((name.toLowerCase()));
        });
        return filteredParts;
    }

    /**
     * searches for a <code>Product</code> by name.
     *
     * returns a <code>FilteredList</code>
     * of products whose name contains the search string.
     *
     * @param name the product name string to look up
     * @return the product or products matching the name string
     */
    public static ObservableList<Product> lookupProduct(String name) {
        FilteredList<Product> filteredProducts = new FilteredList<>(getAllProducts(), p -> true);
        filteredProducts.setPredicate(product -> {
            if (name == null || name.isEmpty()) {
                return true;
            }
            return product.getName().toLowerCase().contains(name.toLowerCase());
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
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * replaces the <code>Product</code> at <code>index</code>
     * of <code>allProducts</code> array with <code>newProduct</code>.
     *
     * @param index the index of the <code>Product</code> to be updated
     * @param newProduct the updated <code>Product</code>
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * removes <code>selectedPart</code> from <code>allParts</code>
     * <code>ObservableList</code>.
     *
     * @param selectedPart the <code>Part</code> to be deleted
     * @return <code>true</code> if the part is removed, <code>false</code> if not
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * removes <code>selectedProduct</code> from <code>allProducts</code>
     * <code>ObservableList</code>.
     *
     * @param selectedProduct the <code>Product</code> to be deleted
     * @return <code>true</code> if the <code>Product</code> is deleted, <code>false</code> if not
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * gets the list of all <code>InHouse</code> and <code>Outsourced</code> parts.
     *
     * @return the <code>allParts</code> <code>ObservableList</code>
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * gets the <code>Product</code> ObservableList.
     *
     * @return the <code>allProducts</code> <code>ObservableList</code>
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * binary search helper method called by
     * lookupPart-by-id method.
     *
     * @param id the <code>id</code> to be searched for
     * @param <T> generic type parameter representing <code>InHouse</code> or
     *           <code>Outsourced</code> parts
     * @return the part matching the id
     */
    private static <T extends Part> T searchPartsById(int id) {
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
     * lookupProduct-by-id method.
     *
     * @param id the <code>id</code> to be searched for
     * @return the product matching the id
     */
    private static Product searchProductsById(int id) {
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

    public static void loadParts() {
        String[] record;
        try (Scanner inFile = new Scanner(new FileInputStream(INVENTORY_FILE))) {
            while (inFile.hasNextLine()) {
                record = inFile.nextLine().split(",");
                Part part = new InHouse(Integer.parseInt(record[0]), record[1], Double.parseDouble(record[2]),
                        Integer.parseInt(record[3]), Integer.parseInt(record[4]), Integer.parseInt(record[5]),
                        Integer.parseInt(record[6]));
                Inventory.addPart(part);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void loadProducts() {
        String[] record;
        try (Scanner inFile = new Scanner(new FileInputStream(PRODUCT_INVENTORY))) {
            while (inFile.hasNextLine()) {
                record = inFile.nextLine().split(",");
                Product p = new Product(Integer.parseInt(record[0]), record[1], Double.parseDouble(record[2]),
                        Integer.parseInt(record[3]), Integer.parseInt(record[4]), Integer.parseInt(record[5]));
                Inventory.addProduct(p);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
