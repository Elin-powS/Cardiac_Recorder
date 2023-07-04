package com.example.cardiacrecorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class health_info_Adapter extends RecyclerView.Adapter<health_info_Adapter.ViewHolder> {

    private static LongClickListener longClickListener ;
    Context context;

    ArrayList<health_info_model> list;

    public health_info_Adapter(Context context, ArrayList<health_info_model> list){

        this.context = context;
        this.list = list;
    }

    public health_info_model getItemAt(int pos){
        try{
            return  list.get(pos);
        }
        catch (Exception e){
            return null;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.health_info_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        health_info_model user = list.get(position);

        holder.date.setText(user.getdate());
        holder.time.setText(user.gettime());
        holder.heart_rate.setText(user.getheart_rate());
        holder.systolic_pressure.setText(user.getsystolic_pressure());
        holder.diastolic_pressure.setText(user.getdiastolic_pressure());
        holder.status.setText(user.getstatus());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onItemClick(holder.getAdapterPosition(),null);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView heart_rate,systolic_pressure,diastolic_pressure, status,date,time;

        public ViewHolder(View itemView){

            super(itemView);

            heart_rate= itemView.findViewById(R.id.hr);
            systolic_pressure= itemView.findViewById(R.id.sp);
            diastolic_pressure= itemView.findViewById(R.id.dp);
            status=itemView.findViewById(R.id.status);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);


        }
    }
    public interface LongClickListener{
        View.OnLongClickListener  onItemClick(int position, View view);
    }

    public void setOnItemClickListener(health_info_Adapter.LongClickListener longClickListener)
    {
        health_info_Adapter.longClickListener= longClickListener;
    }
}
