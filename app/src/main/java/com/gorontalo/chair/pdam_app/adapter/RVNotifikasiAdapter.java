package com.gorontalo.chair.pdam_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.gorontalo.chair.pdam_app.R;
import com.gorontalo.chair.pdam_app.model.NotifikasiModel;

import java.util.List;
import java.util.Random;

public class RVNotifikasiAdapter extends RecyclerView.Adapter<RVNotifikasiAdapter.ViewHolder> {
    private static final String TAG_HASIL = "hasil";

    private Context context;
    private List<NotifikasiModel> list;

    private int lastPosition = -1;

    public RVNotifikasiAdapter(Context context, List<NotifikasiModel> list){
        super();

        this.list = list;
        this.context = context;
    }

    @Override
    public RVNotifikasiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notifikasi, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final NotifikasiModel notifikasiModel = list.get(position);

        holder.txtId.setText(notifikasiModel.getId().toString());
        holder.txtTanggal.setText(notifikasiModel.getTanggal().toString());
        holder.txtIsi.setText(notifikasiModel.getIsi().toString());
        holder.txtStatus.setText(notifikasiModel.getStatus().toString());

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
        public TextView txtId, txtTanggal, txtIsi, txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            txtId = (TextView) itemView.findViewById(R.id.txtCardNotifikasiId);
            txtTanggal = (TextView) itemView.findViewById(R.id.txtCardNotifikasiTanggal);
            txtIsi = (TextView) itemView.findViewById(R.id.txtCardNotifikasiIsi);
            txtStatus = (TextView) itemView.findViewById(R.id.txtCardNotifikasiStatus);
        }

    }
}
