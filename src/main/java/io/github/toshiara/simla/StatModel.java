package io.github.toshiara.simla;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import io.github.toshiara.mystat.MyStat;
// import java.util.Arrays;


public class StatModel {
    ////////////////////////////////////////
    // parameters of drugs
    ////////////////////////////////////////
    private final double[][] MU0 = {
        {75, 8},     // Pro
        {67, 5},     // Lid
        {43, 6},     // Mep
        {30, 10}     // Bup
    };
    private final double[][] LOG_SIGMA0 = {
        {2.2, 0.4},  // Pro
        {2.4, 0.4},  // Lid
        {2.4, 0.4},  // Mep
        {2.5, 0.5}   // Bup
    };
    private final double ADR = 0.7;

    ////////////////////////////////////////
    // Covariance Matrix for drug parameters
    ////////////////////////////////////////
    // r between mean and logSigma
    private final double r1 = -0.22;  // Pro
    private final double r2 = -0.30;  // Lid
    private final double r3 = -0.01;  // Mep
    private final double r4 = -0.16;  // Bup

    // mean among drugs
    private final double m12 = 0.57;  // Pro vs. Lid
    private final double m13 = 0.47;  // Pro vs. Mep
    private final double m14 = 0.50;  // Pro vs. Bup
    private final double m23 = 0.53;  // Lid vs. Mep
    private final double m24 = 0.53;  // Lid vs. Bup
    private final double m34 = 0.42;  // Mep vs. Bup

    //  logSigma among drugs
    private final double s12 = 0.42;  // Pro vs. Lid
    private final double s13 = 0.34;  // Pro vs. Mep
    private final double s14 = 0.26;  // Pro vs. Bup
    private final double s23 = 0.41;  // Lid vs. Mep
    private final double s24 = 0.47;  // Lid vs. Bup
    private final double s34 = 0.56;  // Mep vs. Bup

    // mean and Covariance Matrix
    private final double[] means = {0, 0, 0, 0, 0, 0, 0, 0};
    private final double[][] covariances = {
        { 1,   m12, m13, m14, r1,  0,   0,   0   },
        { m12, 1,   m23, m24, 0,   r2,  0,   0   },
        { m13, m23, 1,   m34, 0,   0,   r3,  0   },
        { m14, m24, m34, 1,   0,   0,   0,   r4  },
        { r1,  0,   0,   0,   1,   s12, s13, s14 },
        { 0,   r2,  0,   0,   s12, 1,   s23, s24 },
        { 0,   0,   r3,  0,   s13, s23, 1,   s34 },
        { 0,   0,   0,   r4,  s14, s24, s34, 1   }
    };

    private MultivariateNormalDistribution distribution =
        new MultivariateNormalDistribution(this.means, this.covariances);

    // Parameters
    private double[][] parameter = {
        {0, 0, 0},  // saline
        {0, 0, 0},  // Pro
        {0, 0, 0},  // Lid
        {0, 0, 0},  // Mep
        {0, 0, 0},  // Bup
        {0, 0, 0}   // Lid+Adr
    };

    // Constructor
    public StatModel() {
        this.setDrugParameter();
    }

    // values of saline are 0
    // set parameters for Pro, Lid, Mep, Bup
    //   with random generator following to multivariate normal distribution
    public void setDrugParameter() {
        double[] rand = distribution.sample();


        final int n = 6;
        for (int i = 1; i < n - 1; i++) {
            this.parameter[i][0] = this.MU0[i - 1][0] +
                                   this.MU0[i - 1][1] * rand[i - 1];
            this.parameter[i][1] = Math.exp(
                this.LOG_SIGMA0[i - 1][0] +
                this.LOG_SIGMA0[i - 1][1] * rand[i + 3]
            );
        }
        // Lid + Adr
        this.parameter[n - 1][0] = this.parameter[2][0];
        this.parameter[n - 1][1] = this.parameter[2][1];
        this.parameter[n - 1][2] = this.ADR;

        // // for debug
        // this.printParameter();
    }


    /**
     * Determines whether to respond based on the elapsed time (in minutes)
     * and specific parameters using a random number.
     *
     * @param drugType the type of drug represented as an integer
     * @param time the time in minutes
     * @return true if it should respond; false otherwise
     */
    public boolean isResponse(int drugType, double time) {
        var prob = getResponseProbability(drugType, time);
        return Math.random() <= prob;
    }

    /**
     * Calculates the upper probability of responding
     *  based on the elapsed time (in minutes) and the specified parameters.
     *
     * @param time the time in minutes
     * @param param an array containing the model parameters [mu, sigma, adr]
     * @return the upper probability as a value between 0.0 and 1.0 (inclusive)
     */
    private double getUpperProbability(int drugType, double time) {
        double[] param = this.parameter[drugType];
        double X = 100 - (1 - param[2]) * time;
        return MyStat.normal_cdf((X - param[0]) / param[1], true);
    }

    /**
     * Calculates the probability of responding based on the drug type,
     *  elapsed time (in minutes), and specific parameters.
     *
     * @param drugType the type of drug represented as an integer
     * @param time the time in minutes
     * @return the response probability as a value between 0.0 and 1.0
     */
    public double getResponseProbability(int drugType, double time) {
        double prob;
        if (drugType == 0) {
            // saline
            //   prob is 0.99 before 30 min
            //   prob is 1 after 30 min
            prob = (time < 30) ? 0.99 : 1;
        } else {
            prob = getUpperProbability(drugType, time);
            // not respond when probability is less than threshold
            // if (prob < ProbThreshold) {
            if (prob < 0.05) {
                return 0.0;
            }
        }

        return prob;
    }

    // // for debug
    // public void printParameter() {
    //     for (var param: this.parameter) {
    //         System.out.println(Arrays.toString(param));
    //     }
    // }
}
