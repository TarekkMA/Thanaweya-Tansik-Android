package com.tmaprojects.tansik.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tmaprojects.tansik.R;
import com.tmaprojects.tansik.utils.Utils;
import com.tmaprojects.tansik.model.TableItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tarekkma on 8/20/17.
 */

public class TansikTableAdapter extends RecyclerView.Adapter<TansikTableAdapter.VH> {


    List<TableItem> items = new ArrayList<>();
    double scoreD;
    boolean colorize = false;
    final double SAFE_LIMIT = 0.5;
    boolean percentage = false;

    public TansikTableAdapter(List<TableItem> items) {
        this.items = items;
    }

    public void colorizeList(double scoreD){
        this.scoreD = scoreD;
        colorize = true;
        notifyDataSetChanged();
    }

    public void setPercent(boolean b){
        percentage = b;
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.tansik_table_item,parent,false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        @BindView(R.id.collage_name)
        TextView collageText;
        @BindView(R.id.collage_score)
        TextView scoreText;
        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(TableItem item){
            Context context = itemView.getContext();


            collageText.setText(item.getCollage());
            if(percentage)scoreText.setText(String.valueOf(Utils.round(item.getScore()/410 * 100,2)));
            else scoreText.setText(String.valueOf(item.getScore()));


            if(colorize) {
                double diff = scoreD - item.getScore();
                double adiff = Math.abs(diff);

                if (adiff <= SAFE_LIMIT) {
                    itemView.setBackgroundColor(context.getResources().getColor(R.color.ok));
                } else if (diff > SAFE_LIMIT) {
                    itemView.setBackgroundColor(context.getResources().getColor(R.color.good));
                } else if (diff < SAFE_LIMIT) {
                    itemView.setBackgroundColor(context.getResources().getColor(R.color.bad));
                }
            }
        }
    }
}
