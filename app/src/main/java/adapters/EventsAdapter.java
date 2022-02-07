package adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarandmapapp.databinding.FragmentEventListItemBinding;
import com.example.calendarandmapapp.models.Event;

//
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    ObservableArrayList<Event> events;
    OnEventSelectedListener listener;

    public static interface OnEventSelectedListener {
        public void onSelected(Event event);
    }

    public EventsAdapter(ObservableArrayList<Event> events, OnEventSelectedListener listener ) {
        this.events = events;
        this.listener = listener;
        events.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Event>>() {
            @Override
            public void onChanged(ObservableList<Event> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<Event> sender, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<Event> sender, int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<Event> sender, int fromPosition, int toPosition, int itemCount) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Event> sender, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentEventListItemBinding binding = FragmentEventListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getBinding().eventListItemTitle.setText(events.get(position).getTitle() + "");
        holder.getBinding().eventListItemDate.setText(events.get(position).getDateString() + "");
        holder.getBinding().eventListItemStartTime.setText("Starts " + events.get(position).getStartTimeString());
        holder.getBinding().eventListItemEndTime.setText("Ends " + events.get(position).getEndTimeString());

        holder.itemView.setOnClickListener(view -> {
            this.listener.onSelected(events.get(position));
        });


    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FragmentEventListItemBinding binding;
        public ViewHolder(@NonNull FragmentEventListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public FragmentEventListItemBinding getBinding(){
            return binding;
        }
    }
}

