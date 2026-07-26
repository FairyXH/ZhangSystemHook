package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class LaunchParamsUtil {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_LANDSCAPE_FREEFORM_HEIGHT_DP = 600;
    private static final int DEFAULT_LANDSCAPE_FREEFORM_WIDTH_DP = 1064;
    static final int DEFAULT_PORTRAIT_FREEFORM_HEIGHT_DP = 732;
    static final int DEFAULT_PORTRAIT_FREEFORM_WIDTH_DP = 412;
    private static final int DISPLAY_EDGE_OFFSET_DP = 27;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final android.graphics.Rect TMP_STABLE_BOUNDS = new android.graphics.Rect();

    private LaunchParamsUtil() {
    }

    static void centerBounds(com.android.server.wm.TaskDisplayArea displayArea, int width, int height, android.graphics.Rect inOutBounds) {
        if (inOutBounds.isEmpty()) {
            displayArea.getStableRect(inOutBounds);
        }
        int left = inOutBounds.centerX() - (width / 2);
        int top = inOutBounds.centerY() - (height / 2);
        inOutBounds.set(left, top, left + width, top + height);
    }

    static android.util.Size getDefaultFreeformSize(com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.TaskDisplayArea displayArea, android.content.pm.ActivityInfo.WindowLayout layout, int orientation, android.graphics.Rect stableBounds) {
        int layoutMinWidth;
        int adjWidth;
        int adjHeight;
        float density = displayArea.getConfiguration().densityDpi / 160.0f;
        int freeformWidthInDp = orientation == 0 ? 1064 : 412;
        int freeformHeightInDp = orientation == 0 ? 600 : DEFAULT_PORTRAIT_FREEFORM_HEIGHT_DP;
        int freeformWidth = (int) ((freeformWidthInDp * density) + 0.5f);
        int freeformHeight = (int) ((freeformHeightInDp * density) + 0.5f);
        int layoutMinHeight = -1;
        if (layout != null) {
            layoutMinWidth = layout.minWidth;
        } else {
            layoutMinWidth = -1;
        }
        if (layout != null) {
            layoutMinHeight = layout.minHeight;
        }
        int portraitHeight = java.lang.Math.min(stableBounds.width(), stableBounds.height());
        int otherDimension = java.lang.Math.max(stableBounds.width(), stableBounds.height());
        int portraitWidth = (portraitHeight * portraitHeight) / otherDimension;
        int maxWidth = orientation == 0 ? portraitHeight : portraitWidth;
        int maxHeight = orientation == 0 ? portraitWidth : portraitHeight;
        int width = java.lang.Math.min(maxWidth, java.lang.Math.max(freeformWidth, layoutMinWidth));
        int height = java.lang.Math.min(maxHeight, java.lang.Math.max(freeformHeight, layoutMinHeight));
        float aspectRatio = java.lang.Math.max(width, height) / java.lang.Math.min(width, height);
        float minAspectRatio = activityRecord.getMinAspectRatio();
        float maxAspectRatio = activityRecord.info.getMaxAspectRatio();
        if (minAspectRatio < 1.0f || aspectRatio >= minAspectRatio) {
            adjWidth = width;
            adjHeight = height;
            if (maxAspectRatio >= 1.0f && aspectRatio > maxAspectRatio) {
                if (orientation == 0) {
                    adjHeight = (int) ((adjWidth / maxAspectRatio) + 0.5f);
                } else {
                    adjWidth = (int) ((adjHeight / maxAspectRatio) + 0.5f);
                }
            }
        } else if (orientation != 0) {
            adjHeight = height;
            adjWidth = (int) ((adjHeight / minAspectRatio) + 0.5f);
        } else {
            adjWidth = width;
            adjHeight = (int) ((adjWidth / minAspectRatio) + 0.5f);
        }
        return new android.util.Size(adjWidth, adjHeight);
    }

    static void adjustBoundsToFitInDisplayArea(com.android.server.wm.TaskDisplayArea displayArea, int layoutDirection, android.content.pm.ActivityInfo.WindowLayout layout, android.graphics.Rect inOutBounds) {
        int layoutMinWidth;
        int left;
        int dx;
        int dy;
        android.graphics.Rect stableBounds = TMP_STABLE_BOUNDS;
        displayArea.getStableRect(stableBounds);
        float density = displayArea.getConfiguration().densityDpi / 160.0f;
        int displayEdgeOffset = (int) ((27.0f * density) + 0.5f);
        stableBounds.inset(displayEdgeOffset, displayEdgeOffset);
        if (stableBounds.width() < inOutBounds.width() || stableBounds.height() < inOutBounds.height()) {
            float heightShrinkRatio = stableBounds.width() / inOutBounds.width();
            float widthShrinkRatio = stableBounds.height() / inOutBounds.height();
            float shrinkRatio = java.lang.Math.min(heightShrinkRatio, widthShrinkRatio);
            int layoutMinHeight = -1;
            if (layout != null) {
                layoutMinWidth = layout.minWidth;
            } else {
                layoutMinWidth = -1;
            }
            if (layout != null) {
                layoutMinHeight = layout.minHeight;
            }
            int adjustedWidth = java.lang.Math.max(layoutMinWidth, (int) (inOutBounds.width() * shrinkRatio));
            int adjustedHeight = java.lang.Math.max(layoutMinHeight, (int) (inOutBounds.height() * shrinkRatio));
            if (stableBounds.width() < adjustedWidth || stableBounds.height() < adjustedHeight) {
                if (layoutDirection == 1) {
                    left = stableBounds.right - adjustedWidth;
                } else {
                    left = stableBounds.left;
                }
                inOutBounds.set(left, stableBounds.top, left + adjustedWidth, stableBounds.top + adjustedHeight);
                return;
            }
            inOutBounds.set(inOutBounds.left, inOutBounds.top, inOutBounds.left + adjustedWidth, inOutBounds.top + adjustedHeight);
        }
        if (inOutBounds.right > stableBounds.right) {
            dx = stableBounds.right - inOutBounds.right;
        } else {
            int dx2 = inOutBounds.left;
            if (dx2 < stableBounds.left) {
                dx = stableBounds.left - inOutBounds.left;
            } else {
                dx = 0;
            }
        }
        if (inOutBounds.top < stableBounds.top) {
            dy = stableBounds.top - inOutBounds.top;
        } else {
            int dy2 = inOutBounds.bottom;
            if (dy2 > stableBounds.bottom) {
                dy = stableBounds.bottom - inOutBounds.bottom;
            } else {
                dy = 0;
            }
        }
        inOutBounds.offset(dx, dy);
    }

    static void calculateLayoutBounds(android.graphics.Rect stableBounds, android.content.pm.ActivityInfo.WindowLayout windowLayout, android.graphics.Rect inOutBounds, android.util.Size desiredSize) {
        int defaultWidth = stableBounds.width();
        int defaultHeight = stableBounds.height();
        if (desiredSize == null) {
            desiredSize = new android.util.Size(stableBounds.width(), stableBounds.height());
        }
        int width = desiredSize.getWidth();
        if (windowLayout.width > 0 && windowLayout.width < defaultWidth) {
            width = windowLayout.width;
        } else if (windowLayout.widthFraction > 0.0f && windowLayout.widthFraction < 1.0f) {
            width = (int) (defaultWidth * windowLayout.widthFraction);
        }
        int height = desiredSize.getHeight();
        if (windowLayout.height > 0 && windowLayout.height < defaultHeight) {
            height = windowLayout.height;
        } else if (windowLayout.heightFraction > 0.0f && windowLayout.heightFraction < 1.0f) {
            height = (int) (defaultHeight * windowLayout.heightFraction);
        }
        inOutBounds.set(0, 0, width, height);
    }

    static void applyLayoutGravity(int verticalGravity, int horizontalGravity, android.graphics.Rect inOutBounds, android.graphics.Rect stableBounds) {
        float fractionOfHorizontalOffset;
        float fractionOfVerticalOffset;
        int width = inOutBounds.width();
        int height = inOutBounds.height();
        switch (horizontalGravity) {
            case 3:
                fractionOfHorizontalOffset = 0.0f;
                break;
            case 4:
            default:
                fractionOfHorizontalOffset = 0.5f;
                break;
            case 5:
                fractionOfHorizontalOffset = 1.0f;
                break;
        }
        switch (verticalGravity) {
            case 48:
                fractionOfVerticalOffset = 0.0f;
                break;
            case 80:
                fractionOfVerticalOffset = 1.0f;
                break;
            default:
                fractionOfVerticalOffset = 0.5f;
                break;
        }
        inOutBounds.offsetTo(stableBounds.left, stableBounds.top);
        int xOffset = (int) ((stableBounds.width() - width) * fractionOfHorizontalOffset);
        int yOffset = (int) ((stableBounds.height() - height) * fractionOfVerticalOffset);
        inOutBounds.offset(xOffset, yOffset);
    }
}
