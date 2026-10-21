package com.arunava.networktool.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.arunava.networktool.R;
import com.arunava.networktool.databinding.ItemPingResultBinding;
import com.arunava.networktool.model.PingResult;
import java.util.ArrayList;
import java.util.List;

public class PingAdapter extends RecyclerView.Adapter<PingAdapter.PingViewHolder> {

    private final List<PingResult> list = new ArrayList<>();

    public void updateList(List<PingResult> newList) {
        list.clear();
        if (newList != null) list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPingResultBinding binding = ItemPingResultBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PingViewHolder holder, int position) {
        PingResult item = list.get(position);
        holder.binding.tvHostName.setText(item.getHostName());
        holder.binding.tvIpAddress.setText(item.getIpAddress());

        if (item.isReachable()) {
            holder.binding.tvLatency.setText(item.getLatencyMs() + " ms");
            int colorRes = item.getLatencyMs() < 50 ? R.color.ping_fast 
                    : (item.getLatencyMs() < 120 ? R.color.ping_medium : R.color.ping_slow);
            holder.binding.tvLatency.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), colorRes));
        } else {
            holder.binding.tvLatency.setText("Timeout");
            holder.binding.tvLatency.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.ping_slow));
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class PingViewHolder extends RecyclerView.ViewHolder {
        final ItemPingResultBinding binding;
        PingViewHolder(ItemPingResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
