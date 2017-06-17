package jatx.networkingclassloader.dx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickandjerry.dynamiclayoutinflator.DynamicLayoutInflator;

/**
 * Created by jatx on 17.06.17.
 */

public class Fragment2 extends LoadableFragment {
    private String userName;

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(getActivity());

        loadLayoutFromURL(frameLayout, "http://tabatsky.ru/testing/fragment2.xml");

        return frameLayout;
    }

    @Override
    public void onLayoutDownloadSuccess(String xmlAsString) {
        LinearLayout linearLayout = (LinearLayout) DynamicLayoutInflator.inflate(getActivity(), xmlAsString, container);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(lp);
        TextView textView = (TextView)DynamicLayoutInflator.findViewByIdString(linearLayout, "text_view");
        textView.setText("Привет, " + userName + "!");
    }

    @Override
    public void setArguments(Bundle arguments) {
        if (arguments!=null) {
            userName = arguments.getString("userName");
        }
    }
}
