package com.gorontalo.chair.pdam_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gorontalo.chair.pdam_app.HomeActivity;
import com.gorontalo.chair.pdam_app.R;
import com.gorontalo.chair.pdam_app.model.PDAMModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class RVPDAMAdapter extends RecyclerView.Adapter<RVPDAMAdapter.ViewHolder>   {
    private static final String TAG_HASIL = "hasil";

    private Context context;
    private List<PDAMModel> list;

    private int lastPosition = -1;

    public RVPDAMAdapter(Context context, List<PDAMModel> list){
        super();

        this.list = list;
        this.context = context;
    }

    @Override
    public RVPDAMAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pdam, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PDAMModel pdamModel = list.get(position);

        holder.txtId.setText(pdamModel.getId().toString());
        holder.txtNama.setText(pdamModel.getNama().toString());
        holder.txtKepala.setText(pdamModel.getKepala().toString());
        holder.txtHp.setText(pdamModel.getHp().toString());
        holder.txtEmail.setText(pdamModel.getEmail().toString());
        holder.txtAlamat.setText(pdamModel.getAlamat().toString());
        holder.txtDeskripsi.setText(pdamModel.getDeskripsi().toString());
        holder.txtLat.setText(pdamModel.getLatitude().toString());
        holder.txtLong.setText(pdamModel.getLongitude().toString());
        holder.txtPhoto.setText(pdamModel.getPhoto().toString());

        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("id", pdamModel.getId());
                intent.putExtra("nama", pdamModel.getNama());
                intent.putExtra("kepala", pdamModel.getKepala());
                intent.putExtra("hp", pdamModel.getHp());
                intent.putExtra("email", pdamModel.getEmail());
                intent.putExtra("alamat", pdamModel.getAlamat());
                intent.putExtra("deskripsi", pdamModel.getDeskripsi());
                intent.putExtra("lat", pdamModel.getLatitude());
                intent.putExtra("long", pdamModel.getLongitude());
                intent.putExtra("photo", pdamModel.getPhoto());
                context.startActivity(intent);
            }
        });

        Picasso.with(context)
                .load(new URLAdapter().getPhotoPdam()+pdamModel.getPhoto())
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.imgPhoto);

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

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgPhoto;
        public TextView txtId, txtNama, txtKepala, txtHp, txtEmail, txtAlamat, txtDeskripsi, txtLat, txtLong, txtPhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            imgPhoto= (ImageView) itemView.findViewById(R.id.imgCardPdam);
            txtId = (TextView) itemView.findViewById(R.id.txtCardPdamId);
            txtNama = (TextView) itemView.findViewById(R.id.txtCardPdamNama);
            txtKepala = (TextView) itemView.findViewById(R.id.txtCardPdamKepala);
            txtHp = (TextView) itemView.findViewById(R.id.txtCardPdamHp);
            txtEmail = (TextView) itemView.findViewById(R.id.txtCardPdamEmail);
            txtAlamat = (TextView) itemView.findViewById(R.id.txtCardPdamAlamat);
            txtDeskripsi = (TextView) itemView.findViewById(R.id.txtCardPdamDeskripsi);
            txtLat = (TextView) itemView.findViewById(R.id.txtCardPdamLat);
            txtLong = (TextView) itemView.findViewById(R.id.txtCardPdamLong);
            txtPhoto = (TextView) itemView.findViewById(R.id.txtCardPdamPhoto);
        }

    }
}
