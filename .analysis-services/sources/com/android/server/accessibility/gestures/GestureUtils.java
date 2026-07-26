package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
public final class GestureUtils {
    public static int MM_PER_CM = 10;
    public static float CM_PER_INCH = 2.54f;

    private GestureUtils() {
    }

    public static boolean isMultiTap(android.view.MotionEvent firstUp, android.view.MotionEvent secondUp, int multiTapTimeSlop, int multiTapDistanceSlop) {
        if (firstUp == null || secondUp == null) {
            return false;
        }
        return eventsWithinTimeAndDistanceSlop(firstUp, secondUp, multiTapTimeSlop, multiTapDistanceSlop);
    }

    private static boolean eventsWithinTimeAndDistanceSlop(android.view.MotionEvent first, android.view.MotionEvent second, int timeout, int distance) {
        if (isTimedOut(first, second, timeout)) {
            return false;
        }
        double deltaMove = distance(first, second);
        return deltaMove < ((double) distance);
    }

    public static double distance(android.view.MotionEvent first, android.view.MotionEvent second) {
        return android.util.MathUtils.dist(first.getX(), first.getY(), second.getX(), second.getY());
    }

    public static double distanceClosestPointerToPoint(android.graphics.PointF pointerDown, android.view.MotionEvent moveEvent) {
        float movement = Float.MAX_VALUE;
        for (int i = 0; i < moveEvent.getPointerCount(); i++) {
            float moveDelta = android.util.MathUtils.dist(pointerDown.x, pointerDown.y, moveEvent.getX(i), moveEvent.getY(i));
            if (movement > moveDelta) {
                movement = moveDelta;
            }
        }
        return movement;
    }

    public static boolean isTimedOut(android.view.MotionEvent firstUp, android.view.MotionEvent secondUp, int timeout) {
        long deltaTime = secondUp.getEventTime() - firstUp.getEventTime();
        return deltaTime >= ((long) timeout);
    }

    public static boolean isDraggingGesture(float firstPtrDownX, float firstPtrDownY, float secondPtrDownX, float secondPtrDownY, float firstPtrX, float firstPtrY, float secondPtrX, float secondPtrY, float maxDraggingAngleCos) {
        float firstDeltaX = firstPtrX - firstPtrDownX;
        float firstDeltaY = firstPtrY - firstPtrDownY;
        if (firstDeltaX == 0.0f && firstDeltaY == 0.0f) {
            return true;
        }
        float firstMagnitude = (float) java.lang.Math.hypot(firstDeltaX, firstDeltaY);
        float firstXNormalized = firstMagnitude > 0.0f ? firstDeltaX / firstMagnitude : firstDeltaX;
        float firstYNormalized = firstMagnitude > 0.0f ? firstDeltaY / firstMagnitude : firstDeltaY;
        float secondDeltaX = secondPtrX - secondPtrDownX;
        float secondDeltaY = secondPtrY - secondPtrDownY;
        if (secondDeltaX == 0.0f && secondDeltaY == 0.0f) {
            return true;
        }
        float secondMagnitude = (float) java.lang.Math.hypot(secondDeltaX, secondDeltaY);
        float secondXNormalized = secondMagnitude > 0.0f ? secondDeltaX / secondMagnitude : secondDeltaX;
        float secondYNormalized = secondMagnitude > 0.0f ? secondDeltaY / secondMagnitude : secondDeltaY;
        float angleCos = (firstXNormalized * secondXNormalized) + (firstYNormalized * secondYNormalized);
        return angleCos >= maxDraggingAngleCos;
    }

    public static int getActionIndex(android.view.MotionEvent event) {
        return (event.getAction() & 65280) >> 8;
    }
}
