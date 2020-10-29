package com.gorontalo.chair.pdam_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.gorontalo.chair.pdam_app.LokasiActivity;
import com.gorontalo.chair.pdam_app.PengaduanActivity;
import com.gorontalo.chair.pdam_app.R;
import com.gorontalo.chair.pdam_app.RiwayatTagihanActivity;
import com.gorontalo.chair.pdam_app.TagihanActivity;
import com.gorontalo.chair.pdam_app.adapter.GridHomeAdapter;

public class FragmentSatu extends Fragment {
    private GridView gridView;
    private Intent intent;
    private String id_pdam, nama, kepala, hp, email, alamat, deskripsi, photo;
    private Double latitude, longitude;

    static final String[] CONTENT = new String[] {"Tagihan", "Riwayat Tagihan","Pengaduan", "Lokasi"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDataIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_satu, container, false);

        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(new GridHomeAdapter(getActivity(), CONTENT));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0){
                    intent = new Intent(getActivity(), TagihanActivity.class);
                    intent.putExtra("id", id_pdam);
                    intent.putExtra("nama", nama);
                    intent.putExtra("kepala", kepala);
                    intent.putExtra("hp", hp);
                    intent.putExtra("email", email);
                    intent.putExtra("alamat", alamat);
                    intent.putExtra("deskripsi", deskripsi);
                    intent.putExtra("lat", latitude);
                    intent.putExtra("long", longitude);
                    intent.putExtra("photo", photo);
                    startActivity(intent);
                }else if (position == 1){
                    intent = new Intent(getActivity(), RiwayatTagihanActivity.class);
                    intent.putExtra("id", id_pdam);
                    intent.putExtra("nama", nama);
                    intent.putExtra("kepala", kepala);
                    intent.putExtra("hp", hp);
                    intent.putExtra("email", email);
                    intent.putExtra("alamat", alamat);
                    intent.putExtra("deskripsi", deskripsi);
                    intent.putExtra("lat", latitude);
                    intent.putExtra("long", longitude);
                    intent.putExtra("photo", photo);
                    startActivity(intent);
                }else if (position == 2){
                    intent = new Intent(getActivity(), PengaduanActivity.class);
                    intent.putExtra("id", id_pdam);
                    intent.putExtra("nama", nama);
                    intent.putExtra("kepala", kepala);
                    intent.putExtra("hp", hp);
                    intent.putExtra("email", email);
                    intent.putExtra("alamat", alamat);
                    intent.putExtra("deskripsi", deskripsi);
                    intent.putExtra("lat", latitude);
                    intent.putExtra("long", longitude);
                    intent.putExtra("photo", photo);
                    startActivity(intent);
                }else{
                    intent = new Intent(getActivity(), LokasiActivity.class);
                    intent.putExtra("id", id_pdam);
                    intent.putExtra("nama", nama);
                    intent.putExtra("kepala", kepala);
                    intent.putExtra("hp", hp);
                    intent.putExtra("email", email);
                    intent.putExtra("alamat", alamat);
                    intent.putExtra("deskripsi", deskripsi);
                    intent.putExtra("lat", latitude);
                    intent.putExtra("long", longitude);
                    intent.putExtra("photo", photo);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private void getDataIntent(){
        intent = getActivity().getIntent();
        id_pdam = intent.getStringExtra("id");
        nama = intent.getStringExtra("nama");
        kepala = intent.getStringExtra("kepala");
        hp = intent.getStringExtra("hp");
        email = intent.getStringExtra("email");
        alamat = intent.getStringExtra("alamat");
        deskripsi = intent.getStringExtra("deskripsi");
        latitude = intent.getDoubleExtra("lat", 0);
        longitude = intent.getDoubleExtra("long", 0);
        photo = intent.getStringExtra("photo");
    }

}
