package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public class DormandPrince853Integrator extends org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator {
    private static final double E1_01 = 0.01312004499419488d;
    private static final double E1_06 = -1.2251564463762044d;
    private static final double E1_07 = -0.4957589496572502d;
    private static final double E1_08 = 1.6643771824549864d;
    private static final double E1_09 = -0.35032884874997366d;
    private static final double E1_10 = 0.3341791187130175d;
    private static final double E1_11 = 0.08192320648511571d;
    private static final double E1_12 = -0.022355307863886294d;
    private static final double E2_01 = -0.18980075407240762d;
    private static final double E2_09 = -0.42268232132379197d;
    private static final double E2_12 = 0.022651792198360825d;
    private static final java.lang.String METHOD_NAME = "Dormand-Prince 8 (5, 3)";
    private static final double[] STATIC_C = {(12.0d - (org.apache.commons.math.util.FastMath.sqrt(6.0d) * 2.0d)) / 135.0d, (6.0d - org.apache.commons.math.util.FastMath.sqrt(6.0d)) / 45.0d, (6.0d - org.apache.commons.math.util.FastMath.sqrt(6.0d)) / 30.0d, (org.apache.commons.math.util.FastMath.sqrt(6.0d) + 6.0d) / 30.0d, 0.3333333333333333d, 0.25d, 0.3076923076923077d, 0.6512820512820513d, 0.6d, 0.8571428571428571d, 1.0d, 1.0d};
    private static final double E2_06 = 4.450312892752409d;
    private static final double E2_07 = 1.8915178993145003d;
    private static final double E2_08 = -5.801203960010585d;
    private static final double E2_10 = -0.1521609496625161d;
    private static final double E2_11 = 0.20136540080403034d;
    private static final double[][] STATIC_A = {new double[]{(12.0d - (org.apache.commons.math.util.FastMath.sqrt(6.0d) * 2.0d)) / 135.0d}, new double[]{(6.0d - org.apache.commons.math.util.FastMath.sqrt(6.0d)) / 180.0d, (6.0d - org.apache.commons.math.util.FastMath.sqrt(6.0d)) / 60.0d}, new double[]{(6.0d - org.apache.commons.math.util.FastMath.sqrt(6.0d)) / 120.0d, 0.0d, (6.0d - org.apache.commons.math.util.FastMath.sqrt(6.0d)) / 40.0d}, new double[]{((org.apache.commons.math.util.FastMath.sqrt(6.0d) * 107.0d) + 462.0d) / 3000.0d, 0.0d, ((-402.0d) - (org.apache.commons.math.util.FastMath.sqrt(6.0d) * 197.0d)) / 1000.0d, ((org.apache.commons.math.util.FastMath.sqrt(6.0d) * 73.0d) + 168.0d) / 375.0d}, new double[]{0.037037037037037035d, 0.0d, 0.0d, (org.apache.commons.math.util.FastMath.sqrt(6.0d) + 16.0d) / 108.0d, (16.0d - org.apache.commons.math.util.FastMath.sqrt(6.0d)) / 108.0d}, new double[]{0.037109375d, 0.0d, 0.0d, ((org.apache.commons.math.util.FastMath.sqrt(6.0d) * 23.0d) + 118.0d) / 1024.0d, (118.0d - (org.apache.commons.math.util.FastMath.sqrt(6.0d) * 23.0d)) / 1024.0d, -0.017578125d}, new double[]{0.03709200011850479d, 0.0d, 0.0d, ((org.apache.commons.math.util.FastMath.sqrt(6.0d) * 4784.0d) + 51544.0d) / 371293.0d, (51544.0d - (org.apache.commons.math.util.FastMath.sqrt(6.0d) * 4784.0d)) / 371293.0d, -0.015319437748624402d, 0.008273789163814023d}, new double[]{0.6241109587160757d, 0.0d, 0.0d, ((-1.324889724104E12d) - (org.apache.commons.math.util.FastMath.sqrt(6.0d) * 3.18801444819E11d)) / 6.265569375E11d, ((org.apache.commons.math.util.FastMath.sqrt(6.0d) * 3.18801444819E11d) - 1.324889724104E12d) / 6.265569375E11d, 27.59209969944671d, 20.154067550477894d, -43.48988418106996d}, new double[]{0.47766253643826434d, 0.0d, 0.0d, ((-4521408.0d) - (org.apache.commons.math.util.FastMath.sqrt(6.0d) * 1137963.0d)) / 2937500.0d, ((org.apache.commons.math.util.FastMath.sqrt(6.0d) * 1137963.0d) - 4521408.0d) / 2937500.0d, 21.230051448181193d, 15.279233632882423d, -33.28821096898486d, -0.020331201708508627d}, new double[]{-0.9371424300859873d, 0.0d, 0.0d, ((org.apache.commons.math.util.FastMath.sqrt(6.0d) * 94326.0d) + 354216.0d) / 112847.0d, (354216.0d - (org.apache.commons.math.util.FastMath.sqrt(6.0d) * 94326.0d)) / 112847.0d, -8.149787010746927d, -18.52006565999696d, 22.739487099350505d, 2.4936055526796523d, -3.0467644718982196d}, new double[]{2.273310147516538d, 0.0d, 0.0d, ((-3457480.0d) - (org.apache.commons.math.util.FastMath.sqrt(6.0d) * 960905.0d)) / 551636.0d, ((org.apache.commons.math.util.FastMath.sqrt(6.0d) * 960905.0d) - 3457480.0d) / 551636.0d, -17.9589318631188d, 27.94888452941996d, -2.8589982771350235d, -8.87285693353063d, 12.360567175794303d, 0.6433927460157636d}, new double[]{0.054293734116568765d, 0.0d, 0.0d, 0.0d, 0.0d, E2_06, E2_07, E2_08, 0.3111643669578199d, E2_10, E2_11, 0.04471061572777259d}};
    private static final double[] STATIC_B = {0.054293734116568765d, 0.0d, 0.0d, 0.0d, 0.0d, E2_06, E2_07, E2_08, 0.3111643669578199d, E2_10, E2_11, 0.04471061572777259d, 0.0d};

    public DormandPrince853Integrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(METHOD_NAME, true, STATIC_C, STATIC_A, STATIC_B, (org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator) new org.apache.commons.math.ode.nonstiff.DormandPrince853StepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public DormandPrince853Integrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(METHOD_NAME, true, STATIC_C, STATIC_A, STATIC_B, (org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator) new org.apache.commons.math.ode.nonstiff.DormandPrince853StepInterpolator(), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    @Override // org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator
    public int getOrder() {
        return 8;
    }

    @Override // org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double d;
        double d2;
        double error1 = 0.0d;
        double error2 = 0.0d;
        for (int j = 0; j < this.mainSetDimension; j++) {
            double errSum1 = (yDotK[0][j] * E1_01) + (yDotK[5][j] * E1_06) + (yDotK[6][j] * E1_07) + (yDotK[7][j] * E1_08) + (yDotK[8][j] * E1_09) + (yDotK[9][j] * E1_10) + (yDotK[10][j] * E1_11) + (yDotK[11][j] * E1_12);
            double errSum2 = (yDotK[0][j] * E2_01) + (yDotK[5][j] * E2_06) + (yDotK[6][j] * E2_07) + (yDotK[7][j] * E2_08) + (yDotK[8][j] * E2_09) + (yDotK[9][j] * E2_10) + (yDotK[10][j] * E2_11) + (yDotK[11][j] * E2_12);
            double yScale = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(y0[j]), org.apache.commons.math.util.FastMath.abs(y1[j]));
            if (this.vecAbsoluteTolerance == null) {
                d = this.scalAbsoluteTolerance;
                d2 = this.scalRelativeTolerance;
            } else {
                d = this.vecAbsoluteTolerance[j];
                d2 = this.vecRelativeTolerance[j];
            }
            double tol = d + (d2 * yScale);
            double ratio1 = errSum1 / tol;
            error1 += ratio1 * ratio1;
            double ratio2 = errSum2 / tol;
            error2 += ratio2 * ratio2;
        }
        double den = (0.01d * error2) + error1;
        if (den <= 0.0d) {
            den = 1.0d;
        }
        return (org.apache.commons.math.util.FastMath.abs(h) * error1) / org.apache.commons.math.util.FastMath.sqrt(((double) this.mainSetDimension) * den);
    }
}
