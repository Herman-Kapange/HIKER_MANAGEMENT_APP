package hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.ConfirmObservationActivity;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.R;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.ViewSpecificObservationActivity;

public class ObservationsAdapter extends RecyclerView.Adapter<ObservationsAdapter.ObservationViewHolder> implements Filterable {


    private Context context;
    private ArrayList observation_id, observation_name, observation_time, observation_number;
    @Override
    public Filter getFilter() {
        return null;
    }
    public ObservationsAdapter(Context context,
                        ArrayList observation_id,
                        ArrayList observation_name,
                        ArrayList observation_time,
                        ArrayList observation_number) {
        this.context = context;
        this.observation_id = observation_id;
        this.observation_name = observation_name;
        this.observation_time = observation_time;
        this.observation_number = observation_number;
    }
    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.observations_layout, parent, false);
        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Log.e("myTag", String.valueOf(observation_id.get(holder.getAdapterPosition())));
        holder.observation_id.setText(String.valueOf(observation_id.get(position)));
        holder.observation_name.setText(String.valueOf(observation_name.get(position)));
        holder.observation_time.setText(String.valueOf(observation_time.get(position)));
        holder.observation_number.setText(String.valueOf(observation_number.get(position)));
        ;
        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewSpecificObservationActivity.class);
                intent.putExtra("observation_id", "edit");
                intent.putExtra("observation_value", String.valueOf(observation_id.get(holder.getAdapterPosition())));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return observation_id.size();
    }

    public class ObservationViewHolder extends RecyclerView.ViewHolder {

        TextView observation_id, observation_name, observation_time, observation_number;
        LinearLayout cardLayout;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            observation_id = itemView.findViewById(R.id.txtHiddenID);
            observation_name = itemView.findViewById(R.id.txtObservationName);
            observation_time = itemView.findViewById(R.id.txtObservationTime);
            observation_number = itemView.findViewById(R.id.txtObservationNumber);
            cardLayout = itemView.findViewById(R.id.observation_layout);
        }

    }
}
