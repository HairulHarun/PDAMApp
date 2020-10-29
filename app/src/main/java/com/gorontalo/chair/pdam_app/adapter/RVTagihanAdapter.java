package com.gorontalo.chair.pdam_app.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gorontalo.chair.pdam_app.R;
import com.gorontalo.chair.pdam_app.RiwayatTagihanActivity;
import com.gorontalo.chair.pdam_app.model.AlarmModel;
import com.gorontalo.chair.pdam_app.model.TagihanModel;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RVTagihanAdapter extends RecyclerView.Adapter<RVTagihanAdapter.ViewHolder>{
    private Context context;
    private Activity activity;
    private List<TagihanModel> list;
    private AlarmModel localData;

    private String NO, ID_PDAM;

    private int lastPosition = -1;

    public RVTagihanAdapter(Activity activity, Context context, List<TagihanModel> list, String no, String id_pdam){
        super();

        this.list = list;
        this.context = context;
        this.activity = activity;
        this.NO = no;
        this.ID_PDAM= id_pdam;
    }

    @Override
    public RVTagihanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_riwayat, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TagihanModel tagihanModel = list.get(position);

        int tarif = Integer.parseInt(tagihanModel.getTarif());
        int pemakaian = Integer.parseInt(tagihanModel.getJumlah());
        final int total = tarif*pemakaian;

        holder.txtNo.setText(tagihanModel.getNoSambunganPelanggan().toString());
        holder.txtPelanggan.setText(tagihanModel.getNamaPelanggan().toString());
        holder.txtAlamat.setText(tagihanModel.getAlamatPelanggan().toString());
        holder.txtTunggakan.setText("Rp. "+tagihanModel.getTunggakan().toString());
        holder.txtTarif.setText("Rp. "+tagihanModel.getTarif().toString()+" /M3");
        holder.txtTotalPemakaian.setText(tagihanModel.getJumlah().toString());
        holder.txtMeterLalu.setText(tagihanModel.getMeterlalu().toString()+" M3");
        holder.txtMeterBulanIni.setText(tagihanModel.getMetersekarang().toString()+" M3");
        holder.txtDenda.setText("Rp. "+tagihanModel.getDenda().toString());
        holder.txtPeriode.setText(tagihanModel.getPeriode().toString());
        holder.txtTotalTagihan.setText("Rp. "+total);
        if (tagihanModel.getStatusBayar().toString().equals("Yes")){
            holder.txtStatusBayar.setText("Lunas");
        }else{
            holder.txtStatusBayar.setText("Belum Lunas");
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDetail(tagihanModel.getNoSambunganPelanggan(),
                        tagihanModel.getNamaPelanggan(),
                        tagihanModel.getAlamatPelanggan(),
                        "Rp. "+tagihanModel.getTunggakan(),
                        "Rp. "+tagihanModel.getTarif()+" /Kwh",
                        tagihanModel.getJumlah(),
                        tagihanModel.getMeterlalu()+" Kwh",
                        tagihanModel.getMetersekarang()+" Kwh",
                        "Rp. "+tagihanModel.getDenda(),
                        tagihanModel.getPeriode(),
                        tagihanModel.getAdministrasi(),
                        "Rp. "+total);
            }
        });

        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String getCurrentDateTime = sdf.format(c.getTime());
            String tanggalBayar = tagihanModel.getTanggalBayar();
            Date tglA = sdf.parse(getCurrentDateTime);
            Date tglB = sdf.parse(tanggalBayar);

            localData = new AlarmModel(context);

            if (tglA.compareTo(tglB) <= 0){
                final String[] date = tanggalBayar.split("-");
                if (!localData.getReminderStatus()){
                    holder.imgNotif.setImageResource(R.drawable.ic_notif_off);
                    holder.imgNotif.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            localData.setReminderStatus(true);
                            localData.set_year(Integer.parseInt(date[0]));
                            localData.set_month(Integer.parseInt(date[1]));
                            localData.set_day(Integer.parseInt(date[2]));
                            localData.set_hour(9);
                            localData.set_min(55);

                            AlarmNotificationAdapter.setReminder(context, AlarmAdapter.class, localData.get_code(), localData.get_year(), localData.get_month(), localData.get_day(), localData.get_hour(), localData.get_min());

                            notifyItemChanged(position);
                        }
                    });
                }else{
                    holder.imgNotif.setImageResource(R.drawable.ic_notif_active);
                    holder.imgNotif.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            localData.setReminderStatus(false);
                            localData.reset();
                            AlarmNotificationAdapter.cancelReminder(context, AlarmAdapter.class, localData.get_code(), true);

                            notifyItemChanged(position);
                        }
                    });
                }
            }else{
                holder.imgNotif.setVisibility(View.INVISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        if(list!= null) {
            return list.size();
        }else{
            return 0;
        }

    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));
            viewToAnimate.startAnimation(anim);
        }

    }

    private void DialogDetail(String no, String pelanggan, String alamat, String tunggakan, String tarif, String pemakaian, String meterlalu, String metersekarang, String denda, String periode, String administrasi, String tagihan) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_riwayat_tagihan, null);
        TextView txtNo = dialogView.findViewById(R.id.txtDialogTagihanNo);
        TextView txtPelanggan = dialogView.findViewById(R.id.txtDialogTagihanPelanggan);
        TextView txtAlamat = dialogView.findViewById(R.id.txtDialogTagihanAlamat);
        TextView txtTunggakan = dialogView.findViewById(R.id.txtDialogTagihanTunggakan);
        TextView txtTarif = dialogView.findViewById(R.id.txtDialogTagihanTarif);
        TextView txtPemakaian = dialogView.findViewById(R.id.txtDialogTagihanTotalPemakaian);
        TextView txtMeterlalu = dialogView.findViewById(R.id.txtDialogTagihanMeterLalu);
        TextView txtMetersekarang = dialogView.findViewById(R.id.txtDialogTagihanMeterBulanIni);
        TextView txtDenda = dialogView.findViewById(R.id.txtDialogTagihanDenda);
        TextView txtPeriode = dialogView.findViewById(R.id.txtDialogTagihanPeriode);
        TextView txtAdministrasi = dialogView.findViewById(R.id.txtDialogTagihanAdministrasi);
        TextView txtTotalTagihan = dialogView.findViewById(R.id.txtDialogTagihanTotalTagihan);

        txtNo.setText(no);
        txtPelanggan.setText(pelanggan);
        txtAlamat.setText(alamat);
        txtTunggakan.setText(tunggakan);
        txtTarif.setText(tarif);
        txtPemakaian.setText(pemakaian);
        txtMeterlalu.setText(meterlalu);
        txtMetersekarang.setText(metersekarang);
        txtDenda.setText(denda);
        txtPeriode.setText(periode);
        txtAdministrasi.setText(administrasi);
        txtTotalTagihan.setText(tagihan);

        dialog.setView(dialogView);
        dialog.setCancelable(true);

        dialog.show();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout layout;
        public ImageView imgNotif;
        public TextView txtNo, txtPelanggan, txtAlamat, txtTunggakan, txtTarif, txtTotalPemakaian, txtMeterLalu, txtMeterBulanIni, txtDenda, txtPeriode, txtTotalTagihan, txtStatusBayar;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            imgNotif = itemView.findViewById(R.id.imgRiwayatNotif);
            txtNo = itemView.findViewById(R.id.txtRiwayatNoSambungan);
            txtPelanggan = itemView.findViewById(R.id.txtRiwayatPelanggan);
            txtAlamat = itemView.findViewById(R.id.txtRiwayatAlamat);
            txtTunggakan = itemView.findViewById(R.id.txtRiwayatTunggakan);
            txtTarif = itemView.findViewById(R.id.txtRiwayatTarif);
            txtTotalPemakaian = itemView.findViewById(R.id.txtRiwayatTotalPemakaian);
            txtMeterLalu = itemView.findViewById(R.id.txtRiwayatMeterLalu);
            txtMeterBulanIni = itemView.findViewById(R.id.txtRiwayatMeterBulanIni);
            txtDenda = itemView.findViewById(R.id.txtRiwayatDenda);
            txtPeriode = itemView.findViewById(R.id.txtRiwayatPeriode);
            txtTotalTagihan = itemView.findViewById(R.id.txtRiwayatJumlah);
            txtStatusBayar = itemView.findViewById(R.id.txtRiwayatStatus);
        }

    }
}
