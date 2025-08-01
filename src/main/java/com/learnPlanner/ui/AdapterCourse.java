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
import com.learnPlanner.entities.Course;
import java.util.List;

public class AdapterCourse extends RecyclerView.Adapter<AdapterCourse.CourseViewHolder> {
    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;
        private CourseViewHolder(@NonNull View itemView){
            //Inflates the layout and gives the look to each row
            super(itemView);
            courseItemView=itemView.findViewById(R.id.course_item_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int coursePosition = getAdapterPosition();
                        final Course currentCourse = mCourses.get(coursePosition);
                        Intent intent = new Intent(context, DetailedCourse.class);
                        intent.putExtra("courseId", currentCourse.getCourseId());
                        intent.putExtra("course_title", currentCourse.getTitle());
                        intent.putExtra("start_date", currentCourse.getStartDate());
                        intent.putExtra("end_date", currentCourse.getEndDate());
                        intent.putExtra("instructor_name", currentCourse.getInstructorName());
                        intent.putExtra("number", currentCourse.getInstructorPhone());
                        intent.putExtra("email", currentCourse.getInstructorEmail());
                        intent.putExtra("note", currentCourse.getNotes());
                        intent.putExtra("termId", currentCourse.getTermId());
                        intent.putExtra("list_progress", currentCourse.getStatus());
                        context.startActivity(intent);
                    }
                }
            );
        }
    }

    @NonNull
    @Override
    public AdapterCourse.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseItemView = mInflater.inflate(R.layout.activity_list_course, parent, false);
        return new CourseViewHolder(courseItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCourse.CourseViewHolder holder, int position) {
        //Assigns values to rows as they come back to the screen
        if(mCourses == null){
            holder.courseItemView.setText("There were no courses found for this term.");
        }
        else{
            Course currentCourse = mCourses.get(position);
            String courseName = currentCourse.getTitle();
            holder.courseItemView.setText(courseName);
        }
    }
    public void setCourse(List<Course> courses){
        mCourses = courses;
        notifyDataSetChanged();
    }

    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;
    public AdapterCourse(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

//    @Override
//    public int getItemCount() {
//        if(mCourses!=null) {
//            return mCourses.size();
//        }
//        else return 0;
//    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }
}