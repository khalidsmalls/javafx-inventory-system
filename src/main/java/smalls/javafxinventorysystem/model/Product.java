package smalls.javafxinventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product class, similar to <code>Part</code>,
 * with the addition of a list of associated parts.
 * <p>
 * The class members are similar with the exception
 * of an associated parts list, an <code>ObservableList</code>,
 * added to the <code>Product</code> class to track the
 * product's associated parts.
 *
 * @author Khalid Smalls
 */
public class Product {

    private final ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * class constructor.
     *
     * @param id the new product id number
     * @param name the new product name
     * @param price the new product price
     * @param stock the new product stock
     * @param min the new product minimum stock
     * @param max the new product maximum stock
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        associatedParts = FXCollections.observableArrayList();
    }

    /**
     * sets the <code>id</code> of this product.
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * sets the <code>name</code> of this product.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the <code>price</code> of this product.
     *
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * sets the <code>stock</code> of this product.
     *
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * sets the <code>min</code>,
     * minimum stock of this product.
     *
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * sets the <code>max</code>,
     * maximum stock of this product.
     *
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * gets the <code>id</code> of this product.
     *
     * @return the <code>id</code>
     */
    public int getId() {
        return id;
    }

    /**
     * gets the <code>name</code> of this product.
     *
     * @return the <code>name</code>
     */
    public String getName() {
        return name;
    }

    /**
     * gets the <code>price</code> of this product.
     *
     * @return the <code>price</code>
     */
    public double getPrice() {
        return price;
    }

    /**
     * gets the <code>stock</code> of this product.
     *
     * @return the <code>stock</code>
     */
    public int getStock() {
        return stock;
    }

    /**
     * gets the <code>min</code>,
     * minimum stock of this product.
     *
     * @return the <code>min</code>
     */
    public int getMin() {
        return min;
    }

    /**
     * gets the <code>max</code>,
     * maximum stock of this product.
     *
     * @return the <code>max</code>
     */
    public int getMax() {
        return max;
    }

    /**
     * adds a part to this product's <code>associatedParts</code>
     * <code>ObservableList</code>.
     *
     * @param part the part to add to <code>associatedParts</code>
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * adds an <code>ObservableList</code> of parts to this product's
     * <code>associatedParts</code> <code>ObservableList</code>.
     *
     * @param parts an <code>ObservableList</code> of parts to add
     */
    public void addAssociatedParts(ObservableList<Part> parts) {
        associatedParts.addAll(parts);
    }

    /**
     * removes a selected part from this product's <code>associatedParts</code>
     * <code>ObservableList</code>.
     *
     * @param selectedAssociatedPart the part to remove from <code>associatedParts</code>>
     * @return <code>true</code> if part is removed, <code>false</code> if not
     */
    public boolean deleteAssociatedParts(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * removes selected associated parts from this product's
     * <code>associatedParts</code> <code>ObservableList</code>.
     *
     * @param selectedAssociatedParts an <code>ObservableList</code> of parts to remove from
     *                                the product's associated parts observable list
     * @return <code>true</code> if parts are removed, <code>false</code> if not
     */
    public boolean deleteAssociatedParts(ObservableList<Part> selectedAssociatedParts) {
        return associatedParts.removeAll(selectedAssociatedParts);
    }

    /**
     * returns this product's <code>ObservableList</code>
     * of associated parts.
     *
     * @return the <code>associatedParts</code> <code>ObservableList</code>
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
