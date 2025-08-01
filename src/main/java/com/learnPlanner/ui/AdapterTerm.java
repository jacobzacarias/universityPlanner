package com.learnPlanner.ui;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learnPlanner.R;
import com.learnPlanner.entities.Term;
import java.util.List;
public class AdapterTerm extends RecyclerView.Adapter<AdapterTerm.TermViewHolder>{
    private List<Term> mTerms;
    private final Context context;
    private final LayoutInflater mInflater;

    public AdapterTerm(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termItemView;
        private TermViewHolder(View itemView){
            super(itemView);
            termItemView=itemView.findViewById(R.id.term_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getAdapterPosition();
                        final Term currentTerm=mTerms.get(position);
                        Intent intent = new Intent(context, DetailedTerm.class);
                        intent.putExtra("id",currentTerm.getTermId());
                        intent.putExtra("title", currentTerm.getTitle());
                        intent.putExtra("start date", currentTerm.getStartDate());
                        intent.putExtra("end date", currentTerm.getEndDate());
                        context.startActivity(intent);
                    }
                }
            );
        }
    }

    @NonNull
    @Override
    public AdapterTerm.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.activity_list_term,parent,false);
        return new TermViewHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTerm.TermViewHolder holder, int position) {
        if(mTerms != null){
            Term currentTerm = mTerms.get(position);
            String title = currentTerm.getTitle();
            holder.termItemView.setText(title);
        }
        else{
            holder.termItemView.setText("There were no terms found.");
        }
    }

    @Override
    public int getItemCount() {
        return mTerms.size();
    }

    public void setTerm(List<Term> terms){
        mTerms = terms;
        notifyDataSetChanged();
    }
}