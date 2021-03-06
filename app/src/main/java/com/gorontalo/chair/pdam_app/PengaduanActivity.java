package com.gorontalo.chair.pdam_app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gorontalo.chair.pdam_app.adapter.KoneksiAdapter;
import com.gorontalo.chair.pdam_app.adapter.RVPengaduanAdapter;
import com.gorontalo.chair.pdam_app.adapter.RVTagihanAdapter;
import com.gorontalo.chair.pdam_app.adapter.SessionAdapter;
import com.gorontalo.chair.pdam_app.adapter.URLAdapter;
import com.gorontalo.chair.pdam_app.adapter.VolleyAdapter;
import com.gorontalo.chair.pdam_app.model.PengaduanModel;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PengaduanActivity extends AppCompatActivity {
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_HASIL = "hasil";

    private SessionAdapter sessionAdapter;
    private KoneksiAdapter koneksiAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter adapter;
    private List<PengaduanModel> pengaduanModelList;

    private FloatingActionButton fabTambah;

    private Boolean isInternetPresent = false;

    private Intent intent;
    private String id_pdam, nama, kepala, hp, email, alamat, deskripsi, photo;
    private Double latitude, longitude;

    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaduan);

        getSupportActionBar().setTitle("Pengaduan");

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

        mRecyclerView = (RecyclerView) findViewById(R.id.rvPengaduan);

        fabTambah = findViewById(R.id.fabTambah);
        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTambahPengaduan();
            }
        });

        if (!sessionAdapter.isLoggedIn()){
            Intent i = new Intent(PengaduanActivity.this, LoginActivity.class);
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
            pengaduanModelList = new ArrayList<>();
            adapter = new RVPengaduanAdapter(PengaduanActivity.this, pengaduanModelList);

            mLayoutManager = new LinearLayoutManager(PengaduanActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(adapter);

            Dexter.withActivity(PengaduanActivity.this)
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
                                        getPengaduan(sessionAdapter.getNoSambungan());
                                    }
                                }else{
                                    SnackbarManager.show(
                                            com.nispok.snackbar.Snackbar.with(PengaduanActivity.this)
                                                    .text("No Connection !")
                                                    .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                                                    .actionLabel("Refresh")
                                                    .actionListener(new ActionClickListener() {
                                                        @Override
                                                        public void onActionClicked(com.nispok.snackbar.Snackbar snackbar) {
                                                            refresh();
                                                        }
                                                    })
                                            , PengaduanActivity.this);
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
                Intent intent = new Intent(PengaduanActivity.this, HomeActivity.class);
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

    private void DialogTambahPengaduan() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(PengaduanActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_tambah_pengaduan, null);

        Button btnKirim = dialogView.findViewById(R.id.btnDialogPengaduanKirim);
        TextView txtNo = dialogView.findViewById(R.id.txtDialogPengaduanNo);
        TextView txtPelanggan = dialogView.findViewById(R.id.txtDialogPengaduanPelanggan);
        TextView txtAlamat = dialogView.findViewById(R.id.txtDialogPengaduanAlamat);
        final EditText txtIsi = dialogView.findViewById(R.id.txtDialogPengaduanIsi);

        txtNo.setText(sessionAdapter.getNoSambungan());
        txtPelanggan.setText(sessionAdapter.getNama());
        txtAlamat.setText(sessionAdapter.getAlamat());

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtIsi.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Masukan isi pengaduan !", Toast.LENGTH_SHORT).show();
                }else{
                    simpanPengaduan(sessionAdapter.getId(), txtIsi.getText().toString());
                }
            }
        });

        dialog.setView(dialogView);
        dialog.setCancelable(true);

        dialog.show();
    }

    private void getPengaduan(final String nosambungan) {
        final ProgressDialog progressDialog = new ProgressDialog(PengaduanActivity.this);
        progressDialog.setMessage("Mengambil data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, new URLAdapter().getPengaduan(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        pengaduanModelList.clear();

                        JSONArray pekerjaan = jObj.getJSONArray(TAG_HASIL);

                        for (int i = 0; i < pekerjaan.length(); i++) {
                            try {
                                JSONObject jsonObject = pekerjaan.getJSONObject(i);

                                PengaduanModel pengaduanModel= new PengaduanModel();
                                pengaduanModel.setId(jsonObject.getString("id"));
                                pengaduanModel.setIdPelanggan(jsonObject.getString("id_pelanggan"));
                                pengaduanModel.setTanggal(jsonObject.getString("tanggal_pengaduan"));
                                pengaduanModel.setIsi(jsonObject.getString("isi_pengaduan"));
                                pengaduanModel.setStatus(jsonObject.getString("status_pengaduan"));
                                pengaduanModel.setNamaPelanggan(jsonObject.getString("nama_pelanggan"));
                                pengaduanModel.setNosambungan(jsonObject.getString("nosambungan_pelanggan"));
                                pengaduanModel.setAlamat(jsonObject.getString("alamat_pelanggan"));
                                pengaduanModel.setPhoto(jsonObject.getString("photo_pelanggan"));

                                pengaduanModelList.add(pengaduanModel);

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
                return params;
            }

        };

        VolleyAdapter.getInstance().addToRequestQueue(stringRequest, "json_pekerjaan");
    }

    private void simpanPengaduan(final String id_pelanggan, final String isi) {
        final ProgressDialog progressDialog = new ProgressDialog(PengaduanActivity.this);
        progressDialog.setMessage("Kirim data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, new URLAdapter().simpanPengaduan(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_HASIL), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        refresh();
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
                params.put("id_pelanggan", id_pelanggan);
                params.put("isi_pengaduan", isi);
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
        Intent i = new Intent(PengaduanActivity.this, PengaduanActivity.class);
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
