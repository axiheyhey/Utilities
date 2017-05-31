package com.example.axiheyhey.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axiheyhey.library.utils.ViewGroupInflater;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ViewGroupInflater.OnItemClickListener<String> {

    private String makeTestString(int index) {
        return String.format(Locale.US, "Test text: %1$d", index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> stringList = new ArrayList<>();
        for (int index = 0, count = 20; index < count; index++) {
            stringList.add(makeTestString(index));
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_main);
        ViewGroupInflater.with(this)
                .dataSource(new MainAdapter(stringList))
                .setOnItemClickListener(this)
                .into(linearLayout);
    }

    @Override
    public void onItemClick(@NonNull View view, @NonNull String itemObject, int position) {
        Toast.makeText(this, itemObject, Toast.LENGTH_LONG).show();
    }

    private static class MainAdapter extends ViewGroupInflater.Adapter<String, LinearLayout> {

        @NonNull
        private List<String> mStringList;

        MainAdapter(@NonNull List<String> stringList) {
            mStringList = ImmutableList.copyOf(stringList);
        }

        @Override
        protected int getCount() {
            return mStringList.size();
        }

        @NonNull
        @Override
        protected String getItem(int position) {
            return mStringList.get(position);
        }

        @NonNull
        @Override
        protected View onCreateView(@NonNull Context context, @NonNull LinearLayout parent, int position) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_main, parent, false);
            TextView textView = (TextView) view.findViewById(R.id.tv_item_title);
            textView.setText(getItem(position));
            return view;
        }
    }
}
