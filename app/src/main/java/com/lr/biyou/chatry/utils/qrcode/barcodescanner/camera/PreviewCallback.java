package com.lr.biyou.chatry.utils.qrcode.barcodescanner.camera;

import com.lr.biyou.chatry.utils.qrcode.barcodescanner.SourceData;

/**
 * Callback for camera previews.
 */
public interface PreviewCallback {
    void onPreview(SourceData sourceData);
    void onPreviewError(Exception e);
}
