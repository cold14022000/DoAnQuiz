package com.example.doanquiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanquiz.Quiz;
import com.example.doanquiz.R;

import java.util.List;

public class quizRecyclerAdapter extends RecyclerView.Adapter<quizRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Quiz> quizList;
    private RecyclerViewClickListener listener;

    public quizRecyclerAdapter(Context context, List<Quiz> itemList, RecyclerViewClickListener listener) {
        this.context = context;
        this.quizList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public quizRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.quiz_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull quizRecyclerAdapter.ViewHolder holder, int position) {
            Quiz quiz=quizList.get(position);

            holder.name.setText(quiz.getName());
            holder.point.setText(quiz.getPoint()+"/"+quiz.getNumQuestion());
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name,point;
        public ConstraintLayout quizBox;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            name=itemView.findViewById(R.id.quiz_Name_quizRow);
            point=itemView.findViewById(R.id.quiz_Point_quizRow);
            quizBox=itemView.findViewById(R.id.quiz_BackGround_RecyclerView);
            quizBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }
}
