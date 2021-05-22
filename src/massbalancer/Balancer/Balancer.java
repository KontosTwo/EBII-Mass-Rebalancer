package massbalancer.Balancer;

import massbalancer.Unit.Unit;

public interface Balancer {
    /**
     * Will the Balancer consider this Unit?
     * @param unit
     * @return
     */
    boolean willBalance(Unit unit);

    /**
     * Calculates the mass of the Unit, assuming that the Balancer has
     * decided that the unit is valid
     * @param unit
     * @return
     */
    double balance(Unit unit);
}
