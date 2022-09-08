package com.example.fitnes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import APIParse.Exercise;

public class ExerciseAdapterDescription extends RecyclerView.Adapter<ExerciseAdapterDescription.ViewHolder> {

    private List<Exercise> data;
    private ListItemClickListener mOnClickListener;
    public ExerciseAdapterDescription(List<Exercise> data, ListItemClickListener listener){
        this.data = data;
        mOnClickListener = listener;
    }

    public void setData(List<Exercise> data){
        this.data = data;
    }

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_exercise,
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = data.get(position);
        holder.name.setText(exercise.getName());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mOnClickListener.onListItemClick(clickedPosition);
                }
            });
        }
    }
}
