package org.apache.commons.math.analysis;

/* JADX INFO: loaded from: classes4.dex */
public abstract class ComposableFunction implements org.apache.commons.math.analysis.UnivariateRealFunction {
    public static final org.apache.commons.math.analysis.ComposableFunction ZERO = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.1
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return 0.0d;
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction ONE = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.2
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return 1.0d;
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction IDENTITY = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.3
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return d;
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction ABS = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.4
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.abs(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction NEGATE = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.5
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return -d;
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction INVERT = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.6
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return 1.0d / d;
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction SIN = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.7
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.sin(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction SQRT = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.8
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.sqrt(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction SINH = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.9
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.sinh(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction EXP = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.10
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.exp(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction EXPM1 = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.11
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.expm1(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction ASIN = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.12
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.asin(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction ATAN = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.13
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.atan(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction TAN = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.14
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.tan(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction TANH = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.15
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.tanh(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction CBRT = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.16
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.cbrt(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction CEIL = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.17
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.ceil(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction FLOOR = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.18
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.floor(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction LOG = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.19
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.log(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction LOG10 = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.20
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.log10(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction LOG1P = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.21
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.log1p(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction COS = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.22
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.cos(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction ACOS = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.23
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.acos(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction COSH = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.24
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.cosh(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction RINT = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.25
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.rint(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction SIGNUM = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.26
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.signum(d);
        }
    };
    public static final org.apache.commons.math.analysis.ComposableFunction ULP = new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.27
        @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
        public double value(double d) {
            return org.apache.commons.math.util.FastMath.ulp(d);
        }
    };

    @Override // org.apache.commons.math.analysis.UnivariateRealFunction
    public abstract double value(double d) throws org.apache.commons.math.FunctionEvaluationException;

    public org.apache.commons.math.analysis.ComposableFunction of(final org.apache.commons.math.analysis.UnivariateRealFunction f) {
        return new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.28
            @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
            public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
                return org.apache.commons.math.analysis.ComposableFunction.this.value(f.value(x));
            }
        };
    }

    public org.apache.commons.math.analysis.ComposableFunction postCompose(final org.apache.commons.math.analysis.UnivariateRealFunction f) {
        return new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.29
            @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
            public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
                return f.value(org.apache.commons.math.analysis.ComposableFunction.this.value(x));
            }
        };
    }

    public org.apache.commons.math.analysis.ComposableFunction combine(final org.apache.commons.math.analysis.UnivariateRealFunction f, final org.apache.commons.math.analysis.BivariateRealFunction combiner) {
        return new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.30
            @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
            public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
                return combiner.value(org.apache.commons.math.analysis.ComposableFunction.this.value(x), f.value(x));
            }
        };
    }

    public org.apache.commons.math.analysis.ComposableFunction add(final org.apache.commons.math.analysis.UnivariateRealFunction f) {
        return new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.31
            @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
            public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
                return org.apache.commons.math.analysis.ComposableFunction.this.value(x) + f.value(x);
            }
        };
    }

    public org.apache.commons.math.analysis.ComposableFunction add(final double a) {
        return new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.32
            @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
            public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
                return org.apache.commons.math.analysis.ComposableFunction.this.value(x) + a;
            }
        };
    }

    public org.apache.commons.math.analysis.ComposableFunction subtract(final org.apache.commons.math.analysis.UnivariateRealFunction f) {
        return new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.33
            @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
            public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
                return org.apache.commons.math.analysis.ComposableFunction.this.value(x) - f.value(x);
            }
        };
    }

    public org.apache.commons.math.analysis.ComposableFunction multiply(final org.apache.commons.math.analysis.UnivariateRealFunction f) {
        return new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.34
            @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
            public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
                return org.apache.commons.math.analysis.ComposableFunction.this.value(x) * f.value(x);
            }
        };
    }

    public org.apache.commons.math.analysis.ComposableFunction multiply(final double scaleFactor) {
        return new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.35
            @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
            public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
                return org.apache.commons.math.analysis.ComposableFunction.this.value(x) * scaleFactor;
            }
        };
    }

    public org.apache.commons.math.analysis.ComposableFunction divide(final org.apache.commons.math.analysis.UnivariateRealFunction f) {
        return new org.apache.commons.math.analysis.ComposableFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.36
            @Override // org.apache.commons.math.analysis.ComposableFunction, org.apache.commons.math.analysis.UnivariateRealFunction
            public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
                return org.apache.commons.math.analysis.ComposableFunction.this.value(x) / f.value(x);
            }
        };
    }

    public org.apache.commons.math.analysis.MultivariateRealFunction asCollector(final org.apache.commons.math.analysis.BivariateRealFunction combiner, final double initialValue) {
        return new org.apache.commons.math.analysis.MultivariateRealFunction() { // from class: org.apache.commons.math.analysis.ComposableFunction.37
            @Override // org.apache.commons.math.analysis.MultivariateRealFunction
            public double value(double[] point) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
                double result = initialValue;
                for (double entry : point) {
                    result = combiner.value(result, org.apache.commons.math.analysis.ComposableFunction.this.value(entry));
                }
                return result;
            }
        };
    }

    public org.apache.commons.math.analysis.MultivariateRealFunction asCollector(org.apache.commons.math.analysis.BivariateRealFunction combiner) {
        return asCollector(combiner, 0.0d);
    }

    public org.apache.commons.math.analysis.MultivariateRealFunction asCollector(double initialValue) {
        return asCollector(org.apache.commons.math.analysis.BinaryFunction.ADD, initialValue);
    }

    public org.apache.commons.math.analysis.MultivariateRealFunction asCollector() {
        return asCollector(org.apache.commons.math.analysis.BinaryFunction.ADD, 0.0d);
    }
}
