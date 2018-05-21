package leif.com.atmfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leif.com.atmfinder.R;
import leif.com.atmfinder.model.ATMModel;
import leif.com.atmfinder.ui.AtmListActivity;

public class AtmAdapter extends RecyclerView.Adapter<AtmAdapter.ClockViewHolder> {

    private AtmListActivity parent;
    private List<ATMModel> items = new ArrayList<>();

    public AtmAdapter(AtmListActivity parent) {
        this.parent = parent;
    }

    @Override
    public ClockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_atm, parent, false);
        return new ClockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ClockViewHolder holder, final int position) {
        final ATMModel item = items.get(position);

        holder.tvOperName.setText(item.getOperName());
        holder.tvPlace.setText(item.getPlace());
        holder.tvDistance.setText(String.format("%.2f", Double.valueOf(item.getDistance())) + " km");
        holder.totalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.touchItem(position);
            }
        });
    }

    public ATMModel getItem(int pos) {
        return items.get(pos);
    }

    public void clearItems() {
        items.clear();
    }

    public void addItem(ATMModel item) {
        items.add(item);
    }

    public void addItems(ArrayList<ATMModel> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ClockViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        TextView tvOperName;
        TextView tvPlace;
        TextView tvDistance;
        LinearLayout totalLayout;

        public ClockViewHolder(View view) {
            super(view);
            this.view = view;
            tvOperName = view.findViewById(R.id.tv_oper_name);
            tvPlace = view.findViewById(R.id.tv_place);
            tvDistance = view.findViewById(R.id.tv_distance);
            totalLayout = view.findViewById(R.id.total_layout);
        }
    }
}
