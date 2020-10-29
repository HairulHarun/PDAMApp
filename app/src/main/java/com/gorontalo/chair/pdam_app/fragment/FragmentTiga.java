package com.gorontalo.chair.pdam_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gorontalo.chair.pdam_app.LoginActivity;
import com.gorontalo.chair.pdam_app.ProfileActivity;
import com.gorontalo.chair.pdam_app.R;
import com.gorontalo.chair.pdam_app.adapter.SessionAdapter;
import com.gorontalo.chair.pdam_app.adapter.URLAdapter;
import com.gorontalo.chair.pdam_app.services.NotifikasiService;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentTiga extends Fragment {
    private SessionAdapter sessionAdapter;

    private Intent intent;
    private String id, nama, kepala, hp, email, alamat, deskripsi, photo;
    private Double latitude, longitude;

    private LinearLayout layoutlogin, layout2;
    private RelativeLayout layoutProfile;
    private Button btnLogin;
    private ImageView btnLogout, imgPhoto;
    private TextView txtNama, txtNo, txtKtp, txtHp, txtSex, txtAlamat;
    private CircleImageView circleImageView;

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

        sessionAdapter = new SessionAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tiga, container, false);

        layoutlogin = (LinearLayout) view.findViewById(R.id.layoutLogin);
        layout2 = (LinearLayout) view.findViewById(R.id.layout2);
        layoutProfile = (RelativeLayout) view.findViewById(R.id.profile_layout);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        txtNama = (TextView) view.findViewById(R.id.txtProfileNama);
        txtNo = (TextView) view.findViewById(R.id.txtProfileNoSambungan);
        txtKtp = (TextView) view.findViewById(R.id.txtProfileKtp);
        txtHp = (TextView) view.findViewById(R.id.txtProfileHp);
        txtSex = (TextView) view.findViewById(R.id.txtProfileSex);
        txtAlamat = (TextView) view.findViewById(R.id.txtProfileAlamat);
        btnLogout = (ImageView) view.findViewById(R.id.btnLogout);
        imgPhoto = (ImageView) view.findViewById(R.id.user_profile_photo);
        circleImageView = view.findViewById(R.id.user_profile_photo);

        txtNama.setText(sessionAdapter.getNama());
        txtNo.setText("No Sambungan : "+sessionAdapter.getNoSambungan());
        txtKtp.setText(sessionAdapter.getKtp());
        txtHp.setText(sessionAdapter.getHp());
        txtSex.setText(sessionAdapter.getSex());
        txtAlamat.setText(sessionAdapter.getAlamat());

        if (sessionAdapter.isLoggedIn()){
            layoutProfile.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layoutlogin.setVisibility(View.GONE);

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionAdapter.logoutUser();
                    getActivity().stopService(new Intent(getActivity(), NotifikasiService.class));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });

            Picasso.with(getActivity())
                    .load(new URLAdapter().getPhotoPelanggan()+sessionAdapter.getPhoto())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(circleImageView);
        }else{
            layoutProfile.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layoutlogin.setVisibility(View.VISIBLE);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.putExtra("id", id);
                    i.putExtra("nama", nama);
                    i.putExtra("kepala", kepala);
                    i.putExtra("hp", hp);
                    i.putExtra("email", email);
                    i.putExtra("alamat", alamat);
                    i.putExtra("deskripsi", deskripsi);
                    i.putExtra("lat", latitude);
                    i.putExtra("long", longitude);
                    i.putExtra("photo", photo);
                    startActivity(i);
                }
            });
        }

        return view;
    }
}
