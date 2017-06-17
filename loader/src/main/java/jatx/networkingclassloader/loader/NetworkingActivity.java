package jatx.networkingclassloader.loader;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;

import dalvik.system.DexClassLoader;

public class NetworkingActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    String dataDir;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                oldHandler.uncaughtException(t, e);
            }
        });

        dataDir = getApplicationInfo().dataDir;
        frameLayout = (FrameLayout) findViewById(R.id.main_frame);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Загружаем классы из сети");
        progressDialog.show();

        DownloadTask downloadTask = new DownloadTask(this, dataDir);
        downloadTask.execute(null, null, null);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String className = intent.getStringExtra("className");
                Bundle args = intent.getBundleExtra("args");
                showFragment(className, args);
            }
        };
        IntentFilter filter = new IntentFilter("jatx.networkingclassloader.ShowFragment");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onBackPressed() {
        //finish(); - getting SIGBUS on next launch this way
        System.exit(0); // its bad, but I have not found another way to fix this issue
    }

    public void downloadReady() {
        Toast.makeText(this, "Классы из сети загружены", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        showFragment("jatx.networkingclassloader.dx.Fragment0", null);
    }

    public void downloadError() {
        Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    public void showFragment(String className, Bundle arguments) {
        File dexFile = new File(dataDir, "classes.dex");
        Log.e("Networking activity", "Loading from dex: " + dexFile.getAbsolutePath());
        File codeCacheDir = new File(getCacheDir() + File.separator + "codeCache");
        codeCacheDir.mkdirs();
        DexClassLoader dexClassLoader = new DexClassLoader(
                dexFile.getAbsolutePath(), codeCacheDir.getAbsolutePath(), null, getClassLoader());
        try {
            Class clazz = dexClassLoader.loadClass(className);
            Fragment fragment = (Fragment) clazz.newInstance();
            fragment.setArguments(arguments);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main_frame, fragment);
            fragmentTransaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
