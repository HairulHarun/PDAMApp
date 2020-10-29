package com.gorontalo.chair.pdam_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gorontalo.chair.pdam_app.adapter.SessionAdapter;
import com.gorontalo.chair.pdam_app.adapter.URLAdapter;
import com.gorontalo.chair.pdam_app.services.NotifikasiService;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private SessionAdapter sessionAdapter;

    private ImageView btnLogout, imgPhoto;
    private TextView txtNama, txtNo, txtKtp, txtHp, txtSex, txtAlamat;
    private CircleImageView circleImageView;

    private Intent intent;
    private String id, nama, kepala, hp, email, alamat, deskripsi, photo;
    private Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile");

        intent = getIntent();
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

        sessionAdapter = new SessionAdapter(getApplicationContext());
        sessionAdapter.checkLoginMain(id, nama, kepala, hp, email, alamat, deskripsi, latitude, longitude, photo);

        txtNama = (TextView) findViewById(R.id.txtProfileNama);
        txtNo = (TextView) findViewById(R.id.txtProfileNoSambungan);
        txtKtp = (TextView) findViewById(R.id.txtProfileKtp);
        txtHp = (TextView) findViewById(R.id.txtProfileHp);
        txtSex = (TextView) findViewById(R.id.txtProfileSex);
        txtAlamat = (TextView) findViewById(R.id.txtProfileAlamat);
        btnLogout = (ImageView) findViewById(R.id.btnLogout);
        imgPhoto = (ImageView) findViewById(R.id.user_profile_photo);
        circleImageView = findViewById(R.id.user_profile_photo);

        txtNama.setText(sessionAdapter.getNama());
        txtNo.setText("No Sambungan : "+sessionAdapter.getNoSambungan());
        txtKtp.setText(sessionAdapter.getKtp());
        txtHp.setText(sessionAdapter.getHp());
        txtSex.setText(sessionAdapter.getSex());
        txtAlamat.setText(sessionAdapter.getAlamat());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionAdapter.logoutUser();
                stopService(new Intent(ProfileActivity.this, NotifikasiService.class));
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });

        Picasso.with(ProfileActivity.this)
                .load(new URLAdapter().getPhotoPelanggan()+sessionAdapter.getPhoto())
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(circleImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_back:
                Intent intent = new Intent(ProfileActivity.this, RiwayatTagihanActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("nama", nama);
                intent.putExtra("kepala", kepala);
                intent.putExtra("hp", hp);
                intent.putExtra("email", email);
                intent.putExtra("alamat", alamat);
                intent.putExtra("deskripsi", deskripsi);
                intent.putExtra("lat", latitude);
                intent.putExtra("long", longitude);
                intent.putExtra("photo", photo);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
