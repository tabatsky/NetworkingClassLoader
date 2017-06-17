package jatx.networkingclassloader.loader;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by jatx on 17.06.17.
 */

public class DownloadTask extends AsyncTask<Void, Object, Object> {
    private static final String url = "http://tabatsky.ru/testing/classes.dex";
    private WeakReference<NetworkingActivity> activityRef;
    String dataDir;

    public DownloadTask(NetworkingActivity activity, String dataDir) {
        activityRef = new WeakReference<NetworkingActivity>(activity);
        this.dataDir = dataDir;
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            InputStream inputStream = new URL(url).openStream();
            File dir = new File(dataDir);
            dir.mkdirs();
            File dexFile = new File(dir, "classes.dex");
            if (dexFile.exists()) {
                dexFile.delete();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(dexFile);
            byte[] buffer = new byte[2048];
            int count;
            while ((count = inputStream.read(buffer, 0, 2048)) != -1) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return e;
        }

        return null;
    }

    @Override
    public void onPostExecute(Object obj) {
        NetworkingActivity activity = activityRef.get();
        if (activity!=null) {
            if (obj == null) {
                activity.downloadReady();
            } else {
                activity.downloadError();
            }
        }
    }
}
