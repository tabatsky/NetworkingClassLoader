package jatx.networkingclassloader.dx;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by jatx on 17.06.17.
 */

public class LoadableFragment extends Fragment {
    protected FrameLayout container = null;

    public void showFragment(String className, Bundle args) {
        Intent intent = new Intent("jatx.networkingclassloader.ShowFragment");
        intent.putExtra("className", className);
        intent.putExtra("args", args);
        getActivity().sendBroadcast(intent);
    }

    protected void loadLayoutFromURL(FrameLayout container, String url) {
        this.container = container;
        LayoutDownloadTask layoutDownloadTask = new LayoutDownloadTask(this, url);
        layoutDownloadTask.execute(null, null, null);
    }

    public void onLayoutDownloadSuccess(String xmlAsString) {

    }

    public void onLayoutDownloadError() {
        Toast.makeText(getActivity(), "Ошибка загрузки разметки", Toast.LENGTH_SHORT).show();
    }
}
