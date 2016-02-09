package com.oatrice.app_indexing.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.oatrice.app_indexing.AppConstants;
import com.oatrice.app_indexing.R;

public class AppIndexingActivity extends AppCompatActivity {

    private final String TAG = AppIndexingActivity.class.getName();

    private GoogleApiClient mClient;
    private String mArticleUrl;
    private String mArticleTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appindexing);

        mClient = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .build();

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();

        Log.i(TAG, "data: " + data);

        mArticleUrl = data.substring(29, data.length());

        if (mArticleUrl.equals("/")){
            mArticleTitle = getString(R.string.app_name);
        }
        else{
            mArticleTitle = Uri.decode(mArticleUrl);
        }

        if (Intent.ACTION_VIEW.equals(action) && data != null) {

            Intent nextIntent = new Intent(this, OpenChromeCustomTabActivity.class);
            nextIntent.putExtra(AppConstants.KEY_URL, data);
            startActivity(nextIntent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getAction());
        finish();
    }

    @Override
    public void onStop() {
        AppIndex.AppIndexApi.end(mClient, getAction());
        mClient.disconnect();
        super.onStop();
    }

    private Action getAction() {
        Uri ARTICLE_URI = Uri.parse("android-app://" + getPackageName() + "/https/oatrice.wordpress.com" + mArticleUrl);

        Log.i(TAG, "mArticleTitle: " + mArticleTitle);
        Log.i(TAG, "mArticleUrl: " + mArticleUrl);
        Log.i(TAG, "ARTICLE_URI: " + ARTICLE_URI);

        Thing object = new Thing.Builder()
                .setName(mArticleTitle)
                .setDescription(mArticleUrl)
                .setUrl(ARTICLE_URI)
                .build();

        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

}