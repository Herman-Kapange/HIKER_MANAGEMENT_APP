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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.ConfirmObservationActivity;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.MainActivity;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Modal.MHikeModal;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.R;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Utils.DatabaseHelper;

public class MHikeAdapter extends RecyclerView.Adapter<MHikeAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList observation_id, observation_name, observation_date, observation_location, observation_number;
    private ArrayList observation_id_full, observation_name_full, observation_date_full, observation_location_full, observation_number_full;
    public MHikeAdapter(Context context,
                        ArrayList observation_id,
                        ArrayList observation_name,
                        ArrayList observation_date,
                        ArrayList observation_location,
                        ArrayList observation_number) {
        this.context = context;
        this.observation_id = observation_id;
        this.observation_name = observation_name;
        this.observation_date = observation_date;
        this.observation_location = observation_location;
        this.observation_number = observation_number;

        this.observation_id_full = new ArrayList<>(observation_id);
        this.observation_name_full = new ArrayList<>(observation_name);
        this.observation_date_full = new ArrayList<>(observation_date);
        this.observation_location_full = new ArrayList<>(observation_location);
        this.observation_number_full = new ArrayList<>(observation_number);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.observation_id.setText(String.valueOf(observation_id.get(position)));
        holder.observation_name.setText(String.valueOf(observation_name.get(position)));
        holder.observation_date.setText(String.valueOf(observation_date.get(position)));
        holder.observation_location.setText(String.valueOf(observation_location.get(position)));
        holder.observation_number.setText(String.valueOf(observation_number.get(position)));
        ;
        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConfirmObservationActivity.class);
                intent.putExtra("observation_id", "view");
                intent.putExtra("observation_value", String.valueOf(observation_id.get(holder.getAdapterPosition())));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return observation_id.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase();
                ArrayList<String> filteredNames = new ArrayList<>();
                ArrayList<String> filteredDates = new ArrayList<>();
                ArrayList<String> filteredIDs = new ArrayList<>();
                ArrayList<String> filteredLocations = new ArrayList<>();
                ArrayList<String> filteredNumbers = new ArrayList<>();
                // ... (initialize other filtered arrays)

                if (searchText.isEmpty()) {
                    // If the search text is empty, show the original data
                    filteredNames.addAll(observation_id_full);
                    filteredDates.addAll(observation_name_full);
                    filteredIDs.addAll(observation_date_full);
                    filteredLocations.addAll(observation_location_full);
                    filteredNumbers.addAll(observation_number_full);
                    // ... (add other original data to filtered arrays)
                } else {
                    // Filter the data based on the search text
                    for (int i = 0; i < observation_name_full.size(); i++) {
                        if (observation_name_full.get(i).toString().toLowerCase().contains(searchText) || observation_date_full.get(i).toString().toLowerCase().contains(searchText) || observation_location_full.get(i).toString().toLowerCase().contains(searchText)) {
                            filteredNames.add((String) observation_id_full.get(i));
                            filteredDates.add((String) observation_name_full.get(i));
                            filteredIDs.add((String) observation_date_full.get(i));
                            filteredLocations.add((String) observation_location_full.get(i));
                            filteredNumbers.add((String) observation_number_full.get(i));
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = new ArrayList[]{filteredNames, filteredDates, filteredIDs, filteredLocations, filteredNumbers};
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ArrayList[] filteredArrays = (ArrayList[]) filterResults.values;
                observation_name.clear();
                observation_name.addAll(filteredArrays[1]);
                observation_date.clear();
                observation_date.addAll(filteredArrays[2]);
                observation_id.clear();
                observation_id.addAll(filteredArrays[0]);
                observation_location.clear();
                observation_location.addAll(filteredArrays[3]);
                observation_number.clear();
                observation_number.addAll(filteredArrays[4]);

                notifyDataSetChanged(); // Notify RecyclerView about the changes
            }
        };
    }

//    PUt Data in CardView
public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView observation_id, observation_name, observation_date, observation_location, observation_number;
    LinearLayout cardLayout;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        observation_id = itemView.findViewById(R.id.txtHidedenID);
        observation_name = itemView.findViewById(R.id.txtObservationName);
        observation_date = itemView.findViewById(R.id.txtObservationLocation);
        observation_location = itemView.findViewById(R.id.txtObservationDate);
        observation_number = itemView.findViewById(R.id.txtObservationID);
        cardLayout = itemView.findViewById(R.id.cardLayout);
    }
}
}