package odlare.com.movieapp2.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import odlare.com.movieapp2.R;
import odlare.com.movieapp2.model.RoomEntity.MovieRoom;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteAdapterViewHolder> {

    private List<MovieRoom> movieRooms;

    public FavoriteAdapter(List<MovieRoom> movieRooms) {
        this.movieRooms = movieRooms;
    }

    public void setMovieRooms(List<MovieRoom> movieRooms) {
        this.movieRooms = movieRooms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);

        return new FavoriteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapterViewHolder holder, int position) {

        MovieRoom movieRoom = movieRooms.get(position);

//        holder.id.setText(movieRoom.getId());
        holder.title.setText(movieRoom.getTitle());
    }

    @Override
    public int getItemCount() {
        return movieRooms.size();
    }

    public class FavoriteAdapterViewHolder extends RecyclerView.ViewHolder {

//        TextView id;
        TextView title;

        public FavoriteAdapterViewHolder(View itemView) {
            super(itemView);

//            id = itemView.findViewById(R.id.txtId);
            title = itemView.findViewById(R.id.txtTitleFavorive);
        }
    }
}
