package com.gorontalo.chair.pdam_app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gorontalo.chair.pdam_app.adapter.KoneksiAdapter;
import com.gorontalo.chair.pdam_app.adapter.SessionAdapter;
import com.gorontalo.chair.pdam_app.adapter.URLAdapter;
import com.gorontalo.chair.pdam_app.adapter.VolleyAdapter;
import com.gorontalo.chair.pdam_app.model.PDAMModel;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagihanActivity extends AppCompatActivity {
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_HASIL = "hasil";

    private KoneksiAdapter koneksiAdapter;
    private SessionAdapter sessionAdapter;
    private Boolean isInternetPresent = false;

    private ScrollView scrollView;
    private EditText txtInputNomor;
    private Button btnOk;
    private TextView txtNo, txtPelanggan, txtAlamat, txtTunggakan, txtTarif, txtTotalPemakaian, txtMeterLalu, txtMeterBulanIni, txtDenda, txtPeriode, txtAdministrasi, txtTotalTagihan, txtTagihanStatus;

    int success;

    private Intent intent;
    private String id, nama, kepala, hp, email, alamat, deskripsi, photo;
    private Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagihan);

        getSupportActionBar().setTitle("Cek Tagihan");

        koneksiAdapter = new KoneksiAdapter(getApplicationContext());
        sessionAdapter = new SessionAdapter(getApplicationContext());

        scrollView = findViewById(R.id.scrollTagihan);
        txtInputNomor = findViewById(R.id.txtInputNomor);
        btnOk = findViewById(R.id.btnOk);
        txtNo = findViewById(R.id.txtTagihanNo);
        txtPelanggan = findViewById(R.id.txtTagihanPelanggan);
        txtAlamat = findViewById(R.id.txtTagihanAlamat);
        txtTunggakan = findViewById(R.id.txtTagihanTunggakan);
        txtTarif = findViewById(R.id.txtTagihanTarif);
        txtTotalPemakaian = findViewById(R.id.txtTagihanTotalPemakaian);
        txtMeterLalu = findViewById(R.id.txtTagihanMeterLalu);
        txtMeterBulanIni = findViewById(R.id.txtTagihanMeterBulanIni);
        txtDenda = findViewById(R.id.txtTagihanDenda);
        txtPeriode = findViewById(R.id.txtTagihanPeriode);
        txtAdministrasi = findViewById(R.id.txtTagihanAdministrasi);
        txtTotalTagihan = findViewById(R.id.txtTagihanTotalTagihan);
        txtTagihanStatus = findViewById(R.id.txtTagihanStatus);

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

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(TagihanActivity.this)
                        .withPermissions(
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    if (isInternetPresent = koneksiAdapter.isConnectingToInternet()) {
                                        if (!txtInputNomor.getText().equals("") || txtInputNomor.getText().equals(" ")){
                                            if (sessionAdapter.isLoggedIn()){
                                                if (sessionAdapter.getNoSambungan().equals(txtInputNomor.getText().toString())){
                                                    getTagihan(txtInputNomor.getText().toString(), id);
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Anda tidak dapat melakukan pengecekan dengan No Sambungan Lain !", Toast.LENGTH_LONG).show();
                                                }
                                            }else{
                                                getTagihan(txtInputNomor.getText().toString(), id);
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Input No Sambungan !", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        SnackbarManager.show(
                                                com.nispok.snackbar.Snackbar.with(TagihanActivity.this)
                                                        .text("No Connection !")
                                                        .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                                                        .actionLabel("Refresh")
                                                        .actionListener(new ActionClickListener() {
                                                            @Override
                                                            public void onActionClicked(com.nispok.snackbar.Snackbar snackbar) {
                                                                refresh();
                                                            }
                                                        })
                                                , TagihanActivity.this);
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
        });
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
                Intent intent = new Intent(TagihanActivity.this, HomeActivity.class);
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

    private void getTagihan(final String nosambungan, final String id_pdam) {
        final ProgressDialog progressDialog = new ProgressDialog(TagihanActivity.this);
        progressDialog.setMessage("Mengambil data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, new URLAdapter().getTagihan(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        JSONArray pekerjaan = jObj.getJSONArray(TAG_HASIL);

                        for (int i = 0; i < pekerjaan.length(); i++) {
                            try {
                                JSONObject jsonObject = pekerjaan.getJSONObject(i);

                                if (jsonObject.getString("statusbayar").equals("Yes")){
                                    txtTagihanStatus.setVisibility(View.VISIBLE);
                                    scrollView.setVisibility(View.INVISIBLE);
                                }else{
                                    txtTagihanStatus.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.VISIBLE);

                                    int tarif = Integer.parseInt(jsonObject.getString("tarif"));
                                    int pemakaian = Integer.parseInt(jsonObject.getString("jumlah"));
                                    int administrasi = Integer.parseInt(jsonObject.getString("administrasi"));
                                    int total = (tarif*pemakaian);

                                    txtNo.setText(jsonObject.getString("nosambungan_pelanggan"));
                                    txtPelanggan.setText(jsonObject.getString("nama_pelanggan"));
                                    txtAlamat.setText(jsonObject.getString("alamat_pelanggan"));
                                    txtTunggakan.setText("Rp. "+jsonObject.getString("tunggakan"));
                                    txtTarif.setText(jsonObject.getString("tarif")+" /M3");
                                    txtTotalPemakaian.setText(jsonObject.getString("jumlah"));
                                    txtMeterLalu.setText(jsonObject.getString("meterlalu")+" M3");
                                    txtMeterBulanIni.setText(jsonObject.getString("metersekarang")+" M3");
                                    txtDenda.setText("Rp. "+jsonObject.getString("denda"));
                                    txtPeriode.setText(jsonObject.getString("periode"));
                                    txtAdministrasi.setText("Rp. "+administrasi);
                                    txtTotalTagihan.setText("Rp. "+total);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                        progressDialog.dismiss();
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
        Intent i = new Intent(TagihanActivity.this, TagihanActivity.class);
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
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }
}
