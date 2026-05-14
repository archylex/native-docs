package org.cocos2dx.javascript;

  import android.content.ActivityNotFoundException;
  import android.content.Intent;
  import android.net.Uri;

  public class WebUrl {

      public static void openUrl(String url) {
        AppActivity activity = AppActivity.getInstance();

        try {
            android.net.Uri uri = android.net.Uri.parse(url);
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);

        } catch (android.content.ActivityNotFoundException e) {
            android.net.Uri uri = android.net.Uri.parse(url);

            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        }
    }
  }
