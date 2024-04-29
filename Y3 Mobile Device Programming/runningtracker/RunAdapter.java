package com.example.runningtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for RecyclerView which will show the user data
 */
public class RunAdapter extends RecyclerView.Adapter<RunAdapter.RunViewHolder> {
    private List<Run> data;
    private Context context;
    private LayoutInflater layoutInflater;
    public RunAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @NonNull
    @Override
    public RunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.db_layout_view, parent, false);
        return new RunViewHolder(itemView, context, this.data);
    }
    @Override
    public void onBindViewHolder(RunViewHolder holder, int position) {
        holder.bind(data.get(position));
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public void setData(List<Run> newData) {
        if (data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    /**
     * Allow user to click each run in the recyclerview hence implemented onClickListener
     */
    class RunViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView idView;
        TextView distView;
        TextView timeView;
        TextView dateView;
        List<Run> data;


        Context context;
        RunViewHolder(View itemView, Context c, List<Run> d) {
            super(itemView);

            context = c;
            data = d;

            idView = itemView.findViewById(R.id.idView);
            distView = itemView.findViewById(R.id.distView);
            timeView = itemView.findViewById(R.id.timeView);
            dateView = itemView.findViewById(R.id.dateView);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

        }
        @SuppressLint("SetTextI18n")
        void bind(final Run run) {
            if (run != null) {
                idView.setText(Integer.toString(run.getId()));
                distView.setText(Math.round(run.getDistance()) + " M");
                timeView.setText(run.getTime() + " S");
                String formattedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date(run.getDate()));
                dateView.setText(formattedDate);


            }
        }

        /**
         * OnClick get the ID of the run and send via intent to Singular entry activity
         */
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, SingularEntryActivity.class);

            intent.putExtra("item", this.data.get(getAdapterPosition()).getId());
            context.startActivity(intent);
        }
    }
}

