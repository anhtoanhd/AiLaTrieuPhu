package com.toanpt.ailatrieuphu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.toanpt.ailatrieuphu.Model.HighScore;
import com.toanpt.ailatrieuphu.R;

import java.text.DecimalFormat;
import java.util.List;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.ViewHolder> {

    private Context context;
    private List<HighScore> listHighScore;

    public HighScoreAdapter(Context context) {
        this.context = context;
    }

    public List<HighScore> getListHighScore() {
        return listHighScore;
    }

    public void setListHighScore(List<HighScore> listHighScore) {
        this.listHighScore = listHighScore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_highscore, parent,  false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HighScore highScore = listHighScore.get(position);
        holder.tvName.setText(highScore.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String highscore = decimalFormat.format(highScore.getHighscore());
        holder.tvHighScore.setText(highscore);
    }

    @Override
    public int getItemCount() {
        return listHighScore.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvHighScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvHighScore = itemView.findViewById(R.id.tvHighScore);
        }
    }
}
