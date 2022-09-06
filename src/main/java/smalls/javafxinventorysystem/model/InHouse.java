package smalls.javafxinventorysystem.model;

public class InHouse extends Part {

    private int machineId;


    /**
     * class constructor.
     * <p>
     * The <code>machineId</code>
     * field makes this class unique from the parent and
     * sibling, <code>Outsourced</code>, class.
     *
     * @param id the new part id number
     * @param name the new part name
     * @param price the new part price
     * @param stock the new part stock
     * @param min the new part minimum stock
     * @param max the new part maximum stock
     * @param machineId the new part machine id number
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * sets the <code>machineId</code> of this part.
     *
     * @param machineId the machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * gets the <code>machineId</code> of this part.
     *
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }

}
