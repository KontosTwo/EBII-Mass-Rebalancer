package massbalancer.Balancer;

import massbalancer.Unit.Unit;
import massbalancer.Unit.UnitAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum BalancerDefinition {
    DEFAULT(new Balancer() {
        @Override
        public boolean willBalance(Unit unit) {
            return true;
        }

        @Override
        public double balance(Unit unit) {
            final int armor = Integer.parseInt(unit.getDataFrom(UnitAttribute.ARMOR));
            if(armor < 3){
                return 0.9;
            }else if(armor < 6){
                return 1;
            }else if(armor < 9){
                return 1.1;
            }else{
                return 1.2;
            }
        }
    }),
    // @QuintusSertorius you wanted a special case for Celtic longswords?
    CELTIC_SWORD(new Balancer() {
        @Override
        public boolean willBalance(Unit unit) {
            return true;
        }

        @Override
        public double balance(Unit unit) {
            return 0;
        }
    })
    ;

    private final Balancer balancer;

    BalancerDefinition(final Balancer balancer){
        this.balancer = balancer;
    }

    public Balancer getBalancer() {
        return balancer;
    }

    /**
     * BalancerDefinitions earlier in the list will be the only one to execute
     * if its willBalance returns true, effectively denying later BalancerDefintions
     * from executing
     * @param overrides
     * @return
     */
    public static List<Balancer> userOverrides(final BalancerDefinition... overrides){
        final List<BalancerDefinition> definitions = new ArrayList<>();
        definitions.addAll(List.of(overrides));
        return definitions.stream().map(BalancerDefinition::getBalancer).collect(Collectors.toList());
    }

    /**
     * Uses the DEFAULT balancer for testing purposes.
     * @return
     */
    public static List<Balancer> useDefault(){
        return List.of(DEFAULT.getBalancer());
    }

    public static double balance(Unit unit, List<Balancer> balancers) throws BalancerException {
        final Optional<Balancer> balancerToUse = balancers.stream().filter(balancer -> balancer.willBalance(unit)).findFirst();
        if(balancerToUse.isEmpty()){
            throw new BalancerException(String.format("Unit %s couldn't find a balancer to use", unit.getDataFrom(UnitAttribute.NAME)));
        }else{
            return balancerToUse.get().balance(unit);
        }
    }
}
