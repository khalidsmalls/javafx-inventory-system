package smalls.javafxinventorysystem.model;

public class Outsourced extends Part {

    private String companyName;

    /**
     * class constructor.
     * The <code>companyName</code> field
     * makes this class unique from the parent and sibling,
     * <code>InHouse</code> class.
     *
     * @param id the new part id number
     * @param name the new part name
     * @param price the new part price
     * @param stock the new part stock
     * @param min the new part minimum stock
     * @param max the new part maximum stock
     * @param companyName the new part company name
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * sets the <code>companyName</code> of this part.
     *
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * gets the <code>companyName</code> of this part.
     *
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }
}
