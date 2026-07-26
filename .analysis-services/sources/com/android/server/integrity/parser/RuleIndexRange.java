package com.android.server.integrity.parser;

/* JADX INFO: loaded from: classes2.dex */
public class RuleIndexRange {
    private int mEndIndex;
    private int mStartIndex;

    public RuleIndexRange(int startIndex, int endIndex) {
        this.mStartIndex = startIndex;
        this.mEndIndex = endIndex;
    }

    public int getStartIndex() {
        return this.mStartIndex;
    }

    public int getEndIndex() {
        return this.mEndIndex;
    }

    public boolean equals(java.lang.Object object) {
        return this.mStartIndex == ((com.android.server.integrity.parser.RuleIndexRange) object).getStartIndex() && this.mEndIndex == ((com.android.server.integrity.parser.RuleIndexRange) object).getEndIndex();
    }

    public java.lang.String toString() {
        return java.lang.String.format("Range{%d, %d}", java.lang.Integer.valueOf(this.mStartIndex), java.lang.Integer.valueOf(this.mEndIndex));
    }
}
