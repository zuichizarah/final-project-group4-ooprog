/**
 * Peak season price: 25% added to the regular price.
 */
public class PeakSeasonPricing implements PricingStrategy {
    private static final double PEAK_MULTIPLIER = 1.25;

    @Override
    public double calculatePrice(double ratePerNight, int nights) {
        return ratePerNight * nights * PEAK_MULTIPLIER;
    }

    @Override
    public String getName() {
        return "Peak Season";
    }
}
