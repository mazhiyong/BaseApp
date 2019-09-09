package com.lr.biyou.rongyun.utils.qrcode.barcodescanner.camera;

import com.lr.biyou.rongyun.utils.qrcode.barcodescanner.SourceData;

/**
 * Callback for camera previews.
 */
public interface PreviewCallback {
    void onPreview(SourceData sourceData);
    void onPreviewError(Exception e);
}
