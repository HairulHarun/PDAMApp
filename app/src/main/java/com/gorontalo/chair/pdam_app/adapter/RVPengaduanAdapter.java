package com.gorontalo.chair.pdam_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gorontalo.chair.pdam_app.R;
import com.gorontalo.chair.pdam_app.model.PengaduanModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class RVPengaduanAdapter extends RecyclerView.Adapter<RVPengaduanAdapter.ViewHolder> {
    private Context context;
    private List<PengaduanModel> list;

    private int lastPosition = -1;

    public RVPengaduanAdapter(Context context, List<PengaduanModel> list){
        super();

        this.list = list;
        this.context = context;
    }

    @Override
    public RVPengaduanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pengaduan, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PengaduanModel pengaduanModel = list.get(position);

        holder.txtNo.setText(pengaduanModel.getNosambungan().toString());
        holder.txtNama.setText(pengaduanModel.getNamaPelanggan().toString());
        holder.txtIsi.setText(pengaduanModel.getIsi().toString());

        if (pengaduanModel.getStatus().equals("Yes")){
            Picasso.with(context)
                    .load(R.drawable.cek)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(holder.imgPhoto);
        }else{
            Picasso.with(context)
                    .load(R.drawable.close)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(holder.imgPhoto);
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

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgPhoto;
        public TextView txtNo, txtNama, txtIsi;

        public ViewHolder(View itemView) {
            super(itemView);

            imgPhoto = (ImageView) itemView.findViewById(R.id.img_pengaduan);
            txtNo = (TextView) itemView.findViewById(R.id.txtPengaduanNoSambungan);
            txtNama = (TextView) itemView.findViewById(R.id.txtPengaduanNama);
            txtIsi = (TextView) itemView.findViewById(R.id.txtPengaduanIsi);
        }

    }
}
