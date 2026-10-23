/**
 * Regular price: rate x nights.
 */
public class StandardPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double ratePerNight, int nights) {
        return ratePerNight * nights;
    }

    @Override
    public String getName() {
        return "Standard";
    }
}
