package jatx.networkingclassloader.dx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nickandjerry.dynamiclayoutinflator.DynamicLayoutInflator;

public class Fragment1 extends LoadableFragment {
    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(getActivity());

        loadLayoutFromURL(frameLayout, "http://tabatsky.ru/testing/fragment1.xml");

        return frameLayout;
    }

    @Override
    public void onLayoutDownloadSuccess(String xmlAsString) {
        LinearLayout linearLayout = (LinearLayout) DynamicLayoutInflator.inflate(getActivity(), xmlAsString, container);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(lp);
        final EditText editText = (EditText)DynamicLayoutInflator.findViewByIdString(linearLayout, "edit_text");
        Button button = (Button)DynamicLayoutInflator.findViewByIdString(linearLayout, "button");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("userName", editText.getText().toString());
                showFragment("jatx.networkingclassloader.dx.Fragment2", args);
            }
        });
    }

}
