package com.gorontalo.chair.pdam_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gorontalo.chair.pdam_app.R;

public class GridHomeAdapter extends BaseAdapter {
    private final Context mContext;
    private final String[] contenValues;

    public GridHomeAdapter(Context context, String[] contentValues) {
        this.mContext = context;
        this.contenValues = contentValues;
    }

    @Override
    public int getCount() {
        return contenValues.length;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = new View(mContext);
            gridView = inflater.inflate(R.layout.card_home, null);

            TextView textView = (TextView) gridView.findViewById(R.id.txtCardHome);
            ImageView imageView = (ImageView) gridView.findViewById(R.id.imgCardHome);

            textView.setText(contenValues[position]);
            String content = contenValues[position];

            if (content.equals("Tagihan")) {
                imageView.setImageResource(R.drawable.logo);
            } else if (content.equals("Riwayat Tagihan")) {
                imageView.setImageResource(R.drawable.logo);
            } else if (content.equals("Pengaduan")) {
                imageView.setImageResource(R.drawable.logo);
            } else if (content.equals("Lokasi")) {
                imageView.setImageResource(R.drawable.logo);
            } else {
                imageView.setImageResource(R.drawable.logo);
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    private int getResourseId(String pVariableName, String pResourcename, String pPackageName) throws RuntimeException {
        try {
            return mContext.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting Resource ID.", e);
        }
    }
}
