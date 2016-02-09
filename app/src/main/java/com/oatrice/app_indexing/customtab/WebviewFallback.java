package com.oatrice.app_indexing.customtab;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.oatrice.app_indexing.activity.WebviewActivity;


/**
 * A Fallback that opens a Webview when Custom Tabs is not available
 */
public class WebviewFallback implements CustomTabActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, WebviewActivity.class);
        intent.putExtra("KEY_URL", uri.toString());
        activity.startActivity(intent);
    }
}