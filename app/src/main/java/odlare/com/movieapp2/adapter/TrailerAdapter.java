package odlare.com.movieapp2.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import odlare.com.movieapp2.R;
import odlare.com.movieapp2.model.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final TrailerAdapterOnClickHandler trailerAdapterOnClickHandler;

    private List<Trailer> trailers;

    public TrailerAdapter(TrailerAdapterOnClickHandler trailerAdapterOnClickHandler, List<Trailer> trailers) {
        this.trailerAdapterOnClickHandler = trailerAdapterOnClickHandler;
        this.trailers = trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public interface TrailerAdapterOnClickHandler {
        void onClick(int position);
    }

    public Trailer getTrailer(int position) {
        return trailers.get(position);
    }

    @NonNull
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder holder, int position) {

        holder.trailerNumber.setText(String.format("Tap to play trailer %s", (position + 1)));
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView trailerNumber;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);

            trailerNumber = itemView.findViewById(R.id.trailer_number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trailerAdapterOnClickHandler.onClick(getAdapterPosition());
                }
            });
        }
    }
}
