package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Deprecated(level = com.android.server.permission.jarjar.kotlin.DeprecationLevel.ERROR, message = "This class is no longer supported, do not use it.")
@java.lang.Deprecated
public abstract class FunctionImpl implements com.android.server.permission.jarjar.kotlin.Function, java.io.Serializable, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3, com.android.server.permission.jarjar.kotlin.jvm.functions.Function4, com.android.server.permission.jarjar.kotlin.jvm.functions.Function5, com.android.server.permission.jarjar.kotlin.jvm.functions.Function6, com.android.server.permission.jarjar.kotlin.jvm.functions.Function7, com.android.server.permission.jarjar.kotlin.jvm.functions.Function8, com.android.server.permission.jarjar.kotlin.jvm.functions.Function9, com.android.server.permission.jarjar.kotlin.jvm.functions.Function10, com.android.server.permission.jarjar.kotlin.jvm.functions.Function11, com.android.server.permission.jarjar.kotlin.jvm.functions.Function12, com.android.server.permission.jarjar.kotlin.jvm.functions.Function13, com.android.server.permission.jarjar.kotlin.jvm.functions.Function14, com.android.server.permission.jarjar.kotlin.jvm.functions.Function15, com.android.server.permission.jarjar.kotlin.jvm.functions.Function16, com.android.server.permission.jarjar.kotlin.jvm.functions.Function17, com.android.server.permission.jarjar.kotlin.jvm.functions.Function18, com.android.server.permission.jarjar.kotlin.jvm.functions.Function19, com.android.server.permission.jarjar.kotlin.jvm.functions.Function20, com.android.server.permission.jarjar.kotlin.jvm.functions.Function21, com.android.server.permission.jarjar.kotlin.jvm.functions.Function22 {
    public abstract int getArity();

    public java.lang.Object invokeVararg(java.lang.Object... p) {
        throw new java.lang.UnsupportedOperationException();
    }

    private void checkArity(int expected) {
        if (getArity() != expected) {
            throwWrongArity(expected);
        }
    }

    private void throwWrongArity(int expected) {
        throw new java.lang.IllegalStateException("Wrong function arity, expected: " + expected + ", actual: " + getArity());
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function0
    public java.lang.Object invoke() {
        checkArity(0);
        return invokeVararg(new java.lang.Object[0]);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public java.lang.Object invoke(java.lang.Object p1) {
        checkArity(1);
        return invokeVararg(p1);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function2
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2) {
        checkArity(2);
        return invokeVararg(p1, p2);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function3
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3) {
        checkArity(3);
        return invokeVararg(p1, p2, p3);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function4
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4) {
        checkArity(4);
        return invokeVararg(p1, p2, p3, p4);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function5
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5) {
        checkArity(5);
        return invokeVararg(p1, p2, p3, p4, p5);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function6
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6) {
        checkArity(6);
        return invokeVararg(p1, p2, p3, p4, p5, p6);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function7
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7) {
        checkArity(7);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function8
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8) {
        checkArity(8);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function9
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9) {
        checkArity(9);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function10
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10) {
        checkArity(10);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function11
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11) {
        checkArity(11);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function12
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12) {
        checkArity(12);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function13
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12, java.lang.Object p13) {
        checkArity(13);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function14
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12, java.lang.Object p13, java.lang.Object p14) {
        checkArity(14);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function15
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12, java.lang.Object p13, java.lang.Object p14, java.lang.Object p15) {
        checkArity(15);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function16
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12, java.lang.Object p13, java.lang.Object p14, java.lang.Object p15, java.lang.Object p16) {
        checkArity(16);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function17
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12, java.lang.Object p13, java.lang.Object p14, java.lang.Object p15, java.lang.Object p16, java.lang.Object p17) {
        checkArity(17);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function18
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12, java.lang.Object p13, java.lang.Object p14, java.lang.Object p15, java.lang.Object p16, java.lang.Object p17, java.lang.Object p18) {
        checkArity(18);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function19
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12, java.lang.Object p13, java.lang.Object p14, java.lang.Object p15, java.lang.Object p16, java.lang.Object p17, java.lang.Object p18, java.lang.Object p19) {
        checkArity(19);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function20
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12, java.lang.Object p13, java.lang.Object p14, java.lang.Object p15, java.lang.Object p16, java.lang.Object p17, java.lang.Object p18, java.lang.Object p19, java.lang.Object p20) {
        checkArity(20);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function21
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12, java.lang.Object p13, java.lang.Object p14, java.lang.Object p15, java.lang.Object p16, java.lang.Object p17, java.lang.Object p18, java.lang.Object p19, java.lang.Object p20, java.lang.Object p21) {
        checkArity(21);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function22
    public java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4, java.lang.Object p5, java.lang.Object p6, java.lang.Object p7, java.lang.Object p8, java.lang.Object p9, java.lang.Object p10, java.lang.Object p11, java.lang.Object p12, java.lang.Object p13, java.lang.Object p14, java.lang.Object p15, java.lang.Object p16, java.lang.Object p17, java.lang.Object p18, java.lang.Object p19, java.lang.Object p20, java.lang.Object p21, java.lang.Object p22) {
        checkArity(22);
        return invokeVararg(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22);
    }
}
