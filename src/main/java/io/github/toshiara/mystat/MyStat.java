package io.github.toshiara.mystat;


public class MyStat {
    private static final double M_SQ_2 = 0.7071067811865475;
    private static final double a1 =  0.254829592;
    private static final double a2 = -0.284496736;
    private static final double a3 =  1.421413741;
    private static final double a4 = -1.453152027;
    private static final double a5 =  1.061405429;
    private static final double p  =  0.3275911;

    /**
     * Computes the Cumulative Distribution Function (CDF)
     * to map a quantile to a probability.
     * <p>
     * This implementation uses the approximation formula 7.1.26 from
     * Abramowitz and Stegun (1964).
     * </p>
     *
     * @param q the quantile (input value)
     * @return the cumulative probability {@code p} corresponding to {@code q},
     *  between 0.0 and 1.0
     * @see <a href="https://archive.org/details/handbookofmathem00abra">Abramowitz & Stegun (1964)</a>
     *
     * @version Precision: {@code abs(err) < 1.5e-7}
     */
    public static double normal_cdf(double x, boolean upper) {
        double sign = x < 0 ? -1 : 1;
        double xx = Math.abs(x) * M_SQ_2;  // = Math.abs(x) / Math.sqrt(2);
        double t = 1 / (1 + p * xx);
        double erf = 1 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-xx * xx);

        double signup = upper ? -1 : 1;
        return (1 + signup * sign * erf) * 0.5;
    }
}

