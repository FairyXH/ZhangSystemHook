package org.apache.commons.math.optimization.linear;

/* JADX INFO: loaded from: classes4.dex */
public enum Relationship {
    EQ("="),
    LEQ("<="),
    GEQ(">=");

    private final java.lang.String stringValue;

    Relationship(java.lang.String stringValue) {
        this.stringValue = stringValue;
    }

    @Override // java.lang.Enum
    public java.lang.String toString() {
        return this.stringValue;
    }

    public org.apache.commons.math.optimization.linear.Relationship oppositeRelationship() {
        switch (ordinal()) {
            case 1:
                return GEQ;
            case 2:
                return LEQ;
            default:
                return EQ;
        }
    }
}
