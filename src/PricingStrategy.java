/**
 * Strategy interface: each pricing rule calculates the total cost differently.
 */
public interface PricingStrategy {
    double calculatePrice(double ratePerNight, int nights);

    String getName();
}
