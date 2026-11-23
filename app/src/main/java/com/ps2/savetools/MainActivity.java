package com.ps2.savetools;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.io.OutputStream;

public class MainActivity extends Activity {
    private WebView wv;
    private ValueCallback<Uri[]> fileCb;

    // Turns the page's blob "download" links into real saves to the Downloads folder.
    private static final String INJECT =
        "(function(){if(window.__apk)return;window.__apk=1;" +
        "var oc=HTMLAnchorElement.prototype.click;" +
        "HTMLAnchorElement.prototype.click=function(){var a=this;" +
        "if(a.download&&a.href&&a.href.indexOf('blob:')===0){" +
        "var x=new XMLHttpRequest();x.open('GET',a.href);x.responseType='blob';" +
        "x.onload=function(){var r=new FileReader();r.onloadend=function(){" +
        "AndroidSave.save(a.download,String(r.result).split(',')[1]||'')};r.readAsDataURL(x.response)};" +
        "x.send();return}" +
        "return oc.apply(this,arguments)}})();";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        wv = new WebView(this);
        setContentView(wv);
        WebSettings s = wv.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        wv.addJavascriptInterface(new Bridge(), "AndroidSave");
        wv.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView v, String u) { v.evaluateJavascript(INJECT, null); }
        });
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> cb, FileChooserParams p) {
                if (fileCb != null) fileCb.onReceiveValue(null);
                fileCb = cb;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                try { startActivityForResult(Intent.createChooser(i, "Select file"), 1); }
                catch (Exception e) { fileCb = null; cb.onReceiveValue(null); return false; }
                return true;
            }
        });
        wv.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onActivityResult(int req, int res, Intent d) {
        if (req == 1 && fileCb != null) {
            Uri[] r = null;
            if (res == RESULT_OK && d != null && d.getData() != null) r = new Uri[]{d.getData()};
            fileCb.onReceiveValue(r);
            fileCb = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (wv.canGoBack()) wv.goBack(); else super.onBackPressed();
    }

    private class Bridge {
        @JavascriptInterface
        public void save(String name, String b64) {
            try {
                byte[] data = Base64.decode(b64, Base64.DEFAULT);
                ContentValues v = new ContentValues();
                v.put(MediaStore.Downloads.DISPLAY_NAME, name);
                v.put(MediaStore.Downloads.MIME_TYPE, name.endsWith(".zip") ? "application/zip" : "application/octet-stream");
                Uri u = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, v);
                OutputStream o = getContentResolver().openOutputStream(u);
                o.write(data);
                o.close();
                toast("Saved to Downloads: " + name);
            } catch (Exception e) {
                toast("Save failed: " + e.getMessage());
            }
        }
        private void toast(final String m) {
            runOnUiThread(new Runnable() { public void run() { Toast.makeText(MainActivity.this, m, Toast.LENGTH_LONG).show(); } });
        }
    }
}
