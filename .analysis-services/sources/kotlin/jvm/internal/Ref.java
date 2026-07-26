package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class Ref {
    private Ref() {
    }

    public static final class ObjectRef<T> implements java.io.Serializable {
        public T element;

        public java.lang.String toString() {
            return java.lang.String.valueOf(this.element);
        }
    }

    public static final class ByteRef implements java.io.Serializable {
        public byte element;

        public java.lang.String toString() {
            return java.lang.String.valueOf((int) this.element);
        }
    }

    public static final class ShortRef implements java.io.Serializable {
        public short element;

        public java.lang.String toString() {
            return java.lang.String.valueOf((int) this.element);
        }
    }

    public static final class IntRef implements java.io.Serializable {
        public int element;

        public java.lang.String toString() {
            return java.lang.String.valueOf(this.element);
        }
    }

    public static final class LongRef implements java.io.Serializable {
        public long element;

        public java.lang.String toString() {
            return java.lang.String.valueOf(this.element);
        }
    }

    public static final class FloatRef implements java.io.Serializable {
        public float element;

        public java.lang.String toString() {
            return java.lang.String.valueOf(this.element);
        }
    }

    public static final class DoubleRef implements java.io.Serializable {
        public double element;

        public java.lang.String toString() {
            return java.lang.String.valueOf(this.element);
        }
    }

    public static final class CharRef implements java.io.Serializable {
        public char element;

        public java.lang.String toString() {
            return java.lang.String.valueOf(this.element);
        }
    }

    public static final class BooleanRef implements java.io.Serializable {
        public boolean element;

        public java.lang.String toString() {
            return java.lang.String.valueOf(this.element);
        }
    }
}
