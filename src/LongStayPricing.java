/**
 * Long-stay price: 10% discount when the guest stays 5 nights or more.
 */
public class LongStayPricing implements PricingStrategy {
    private static final int MIN_NIGHTS = 5;
    private static final double DISCOUNT = 0.10;

    @Override
    public double calculatePrice(double ratePerNight, int nights) {
        double total = ratePerNight * nights;
        if (nights >= MIN_NIGHTS) {
            total = total - (total * DISCOUNT);
        }
        return total;
    }

    @Override
    public String getName() {
        return "Long Stay";
    }
}
