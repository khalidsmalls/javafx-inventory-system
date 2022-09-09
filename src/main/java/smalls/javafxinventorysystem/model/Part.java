package smalls.javafxinventorysystem.model;

/**
 * abstract part class -
 * parent of <code>InHouse</code> and
 * <code>Outsourced</code> classes
 *
 * @author Khalid Smalls
 */

public abstract class Part {

    /**
     * this part's id number
     */
    private int id;

    /**
     * this part's name
     */
    private String name;

    /**
     * this part's price
     */
    private double price;

    /**
     * this part's stock
     */
    private int stock;

    /**
     * this part's minimum stock
     */
    private int min;

    /**
     * this part's maximum stock
     */
    private int max;

    /**
     * class constructor.
     *
     * @param id the new part id
     * @param name the new part name
     * @param price the new part price
     * @param stock the new part stock
     * @param min the new part minimum stock
     * @param max the new part maximum stock
     */
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }


}
