package com.gorontalo.chair.pdam_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gorontalo.chair.pdam_app.R;
import com.gorontalo.chair.pdam_app.adapter.URLAdapter;
import com.squareup.picasso.Picasso;

public class FragmentDua extends Fragment {
    private ImageView imgPdam;
    private TextView txtNama, txtKepala, txtHp, txtEmail, txtAlamat, txtDeskripsi;
    private Intent intent;
    private String id, nama, kepala, hp, email, alamat, deskripsi, photo;
    private Double latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getActivity().getIntent();
        id = intent.getStringExtra("id");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dua, container, false);

        imgPdam = view.findViewById(R.id.imgFragmentDua);
        txtNama = view.findViewById(R.id.txtFragmentNama);
        txtKepala = view.findViewById(R.id.txtFragmentKepala);
        txtHp = view.findViewById(R.id.txtFragmentHp);
        txtEmail = view.findViewById(R.id.txtFragmentEmail);
        txtAlamat = view.findViewById(R.id.txtFragmentAlamat);
        txtDeskripsi = view.findViewById(R.id.txtFragmentDeskripsi);

        txtNama.setText(nama);
        txtKepala.setText(kepala);
        txtHp.setText(hp);
        txtEmail.setText(email);
        txtAlamat.setText(alamat);
        txtDeskripsi.setText(deskripsi);

        Picasso.with(getActivity())
                .load(new URLAdapter().getPhotoPdam()+photo)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(imgPdam);

        return view;
    }
}
