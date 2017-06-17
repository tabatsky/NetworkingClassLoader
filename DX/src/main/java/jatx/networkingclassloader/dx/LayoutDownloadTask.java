package jatx.networkingclassloader.dx;

import android.os.AsyncTask;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by jatx on 17.06.17.
 */

public class LayoutDownloadTask extends AsyncTask<Void, Object, Object> {
    private String url;
    private WeakReference<LoadableFragment> fragmentRef;

    public LayoutDownloadTask(LoadableFragment fragment, String url) {
        fragmentRef = new WeakReference<LoadableFragment>(fragment);
        this.url = url;
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            Scanner sc = new Scanner(new URL(url).openStream());
            String str = sc.useDelimiter("\\A").next();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return e;
        }
    }

    @Override
    public void onPostExecute(Object obj) {
        LoadableFragment fragment = fragmentRef.get();
        if (fragment != null) {
            if (obj instanceof String) {
                fragment.onLayoutDownloadSuccess((String) obj);
            } else if (obj instanceof Exception) {
                fragment.onLayoutDownloadError();
            }
        }
    }
}
