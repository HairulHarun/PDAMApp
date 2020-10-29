package com.gorontalo.chair.pdam_app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gorontalo.chair.pdam_app.adapter.KoneksiAdapter;
import com.gorontalo.chair.pdam_app.adapter.RVPDAMAdapter;
import com.gorontalo.chair.pdam_app.adapter.RVTagihanAdapter;
import com.gorontalo.chair.pdam_app.adapter.SessionAdapter;
import com.gorontalo.chair.pdam_app.adapter.URLAdapter;
import com.gorontalo.chair.pdam_app.adapter.VolleyAdapter;
import com.gorontalo.chair.pdam_app.model.PDAMModel;
import com.gorontalo.chair.pdam_app.model.TagihanModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RiwayatTagihanActivity extends AppCompatActivity {
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_HASIL = "hasil";

    private SessionAdapter sessionAdapter;
    private KoneksiAdapter koneksiAdapter;

    private Boolean isInternetPresent = false;

    private CircleImageView imgProfile;
    private TextView txtNama, txtNoSambungan;
    private Button btnProfile;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter adapter;
    private List<TagihanModel> tagihanModelList;

    private Intent intent;
    private String id_pdam, nama, kepala, hp, email, alamat, deskripsi, photo;
    private Double latitude, longitude;

    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_tagihan);
        
        getSupportActionBar().setTitle("Riwayat Tagihan");

        intent = getIntent();
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

        koneksiAdapter= new KoneksiAdapter(getApplicationContext());
        sessionAdapter = new SessionAdapter(getApplicationContext());
        sessionAdapter.checkLoginMain(id_pdam, nama, kepala, hp, email, alamat, deskripsi, latitude, longitude, photo);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvRiwayat);
        imgProfile = (CircleImageView) findViewById(R.id.img_profile);
        txtNama = (TextView) findViewById(R.id.txtProfileNama);
        txtNoSambungan = (TextView) findViewById(R.id.txtProfileNoSambungan);
        btnProfile = (Button) findViewById(R.id.btnProfile);

        txtNama.setText(sessionAdapter.getNama());
        txtNoSambungan.setText("No Sambungan : "+sessionAdapter.getNoSambungan());

        Picasso.with(RiwayatTagihanActivity.this)
                .load(new URLAdapter().getPhotoPelanggan()+sessionAdapter.getPhoto())
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(imgProfile);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RiwayatTagihanActivity.this, ProfileActivity.class);
                i.putExtra("id", id_pdam);
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

        if (!sessionAdapter.isLoggedIn()){
            Intent i = new Intent(RiwayatTagihanActivity.this, LoginActivity.class);
            i.putExtra("id", id_pdam);
            i.putExtra("nama", nama);
            i.putExtra("kepala", kepala);
            i.putExtra("hp", hp);
            i.putExtra("email", email);
            i.putExtra("alamat", alamat);
            i.putExtra("deskripsi", deskripsi);
            i.putExtra("lat", latitude);
            i.putExtra("long", longitude);
            i.putExtra("photo", photo);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }else{
            tagihanModelList = new ArrayList<>();
            adapter = new RVTagihanAdapter(RiwayatTagihanActivity.this, getApplicationContext(), tagihanModelList, sessionAdapter.getNoSambungan(), id_pdam);

            mLayoutManager = new LinearLayoutManager(RiwayatTagihanActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(adapter);

            Dexter.withActivity(RiwayatTagihanActivity.this)
                    .withPermissions(
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                if (isInternetPresent = koneksiAdapter.isConnectingToInternet()) {
                                    if (!sessionAdapter.getNoSambungan().equals("")){
                                        getRiwayatTagihan(sessionAdapter.getNoSambungan(), id_pdam);
                                    }
                                }else{
                                    SnackbarManager.show(
                                            com.nispok.snackbar.Snackbar.with(RiwayatTagihanActivity.this)
                                                    .text("No Connection !")
                                                    .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                                                    .actionLabel("Refresh")
                                                    .actionListener(new ActionClickListener() {
                                                        @Override
                                                        public void onActionClicked(com.nispok.snackbar.Snackbar snackbar) {
                                                            refresh();
                                                        }
                                                    })
                                            , RiwayatTagihanActivity.this);
                                }
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).
                    withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .onSameThread()
                    .check();
        }

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
                Intent intent = new Intent(RiwayatTagihanActivity.this, HomeActivity.class);
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getRiwayatTagihan(final String nosambungan, final String id_pdam) {
        final ProgressDialog progressDialog = new ProgressDialog(RiwayatTagihanActivity.this);
        progressDialog.setMessage("Tunggu sebentar...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, new URLAdapter().getRiwayatTagihan(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        tagihanModelList.clear();

                        JSONArray pekerjaan = jObj.getJSONArray(TAG_HASIL);

                        for (int i = 0; i < pekerjaan.length(); i++) {
                            try {
                                JSONObject jsonObject = pekerjaan.getJSONObject(i);

                                TagihanModel tagihanModel= new TagihanModel();
                                tagihanModel.setId(jsonObject.getString("id"));
                                tagihanModel.setIdPelanggan(jsonObject.getString("id_pelanggan"));
                                tagihanModel.setIdPdam(jsonObject.getString("id_pdam"));
                                tagihanModel.setIdKlasifikasi(jsonObject.getString("id_klasifikasi"));
                                tagihanModel.setNoSambunganPelanggan(jsonObject.getString("nosambungan_pelanggan"));
                                tagihanModel.setNamaPelanggan(jsonObject.getString("nama_pelanggan"));
                                tagihanModel.setKtpPelanggan(jsonObject.getString("ktp_pelanggan"));
                                tagihanModel.setHpPelanggan(jsonObject.getString("hp_pelanggan"));
                                tagihanModel.setAlamatPelanggan(jsonObject.getString("alamat_pelanggan"));
                                tagihanModel.setPhotoPelanggan(jsonObject.getString("photo_pelanggan"));
                                tagihanModel.setNamaKlarifikasi(jsonObject.getString("nama_klasifikasi"));
                                tagihanModel.setKeteranganKlarifikasi(jsonObject.getString("keterangan_klasifikasi"));
                                tagihanModel.setNamaPdam(jsonObject.getString("nama_pdam"));
                                tagihanModel.setTarif(jsonObject.getString("tarif"));
                                tagihanModel.setTunggakan(jsonObject.getString("tunggakan"));
                                tagihanModel.setJumlah(jsonObject.getString("jumlah"));
                                tagihanModel.setMeterlalu(jsonObject.getString("meterlalu"));
                                tagihanModel.setMetersekarang(jsonObject.getString("metersekarang"));
                                tagihanModel.setDenda(jsonObject.getString("denda"));
                                tagihanModel.setPeriode(jsonObject.getString("periode"));
                                tagihanModel.setAdministrasi(jsonObject.getString("administrasi"));
                                tagihanModel.setTanggalBatas(jsonObject.getString("tanggalbatas"));
                                tagihanModel.setTanggalBayar(jsonObject.getString("tanggalbayar"));
                                tagihanModel.setStatusBayar(jsonObject.getString("statusbayar"));

                                tagihanModelList.add(tagihanModel);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_HASIL), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nosambungan", nosambungan);
                params.put("id_pdam", id_pdam);
                return params;
            }

        };

        VolleyAdapter.getInstance().addToRequestQueue(stringRequest, "json_pekerjaan");
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void refresh(){
        Intent i = new Intent(RiwayatTagihanActivity.this, RiwayatTagihanActivity.class);
        i.putExtra("id", id_pdam);
        i.putExtra("nama", nama);
        i.putExtra("kepala", kepala);
        i.putExtra("hp", hp);
        i.putExtra("email", email);
        i.putExtra("alamat", alamat);
        i.putExtra("deskripsi", deskripsi);
        i.putExtra("lat", latitude);
        i.putExtra("long", longitude);
        i.putExtra("photo", photo);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }
}
