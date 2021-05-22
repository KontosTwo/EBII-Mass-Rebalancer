package massbalancer.Balancer;

public final class BalancerException extends Exception{
    public BalancerException(String message){
        super(String.format("Error while rebalancing mass: %s", message));
    }
}
