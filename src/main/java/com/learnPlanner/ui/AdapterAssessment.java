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
import com.learnPlanner.entities.Assessment;
import java.util.List;

public class AdapterAssessment extends RecyclerView.Adapter<AdapterAssessment.AssessmentViewHolder> {
    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView;
        private AssessmentViewHolder(View itemView){
            super(itemView);
            assessmentItemView = itemView.findViewById(R.id.assessment_item_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        final Assessment currentAssessment = mAssessment.get(position);
                        Intent intent = new Intent(context, DetailedAssessment.class);
                        intent.putExtra("assessmentId", currentAssessment.getAssessmentId());
                        intent.putExtra("assessmentName", currentAssessment.getTitle());
                        intent.putExtra("assessmentType", currentAssessment.getType());
                        intent.putExtra("assessmentStart", currentAssessment.getStartDate());
                        intent.putExtra("assessmentEnd", currentAssessment.getEndDate());
                        intent.putExtra("courseId", currentAssessment.getCourseId());
                        intent.putExtra("assessmentId", currentAssessment.getAssessmentId());
                        context.startActivity(intent);
                    }
                }
            );
        }
    }

    @NonNull
    @Override
    public AdapterAssessment.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View assessmentItemView = mInflater.inflate(R.layout.activity_list_assessment, parent, false);
        return new AssessmentViewHolder(assessmentItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAssessment.AssessmentViewHolder holder, int position) {
        if(mAssessment == null){
            holder.assessmentItemView.setText("There were no assessments found.");
        }
        else{
            Assessment currentAssessment = mAssessment.get(position);
            String assessmentName = currentAssessment.getTitle();
            holder.assessmentItemView.setText(assessmentName);
        }
    }

    @Override
    public int getItemCount() {
        return mAssessment.size();
    }

    List<Assessment> mAssessment;
    private Context context;
    private final LayoutInflater mInflater;
    public AdapterAssessment(Context context){
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    public void setAssessments(List<Assessment> assessments){
        mAssessment = assessments;
        notifyDataSetChanged();
    }
}