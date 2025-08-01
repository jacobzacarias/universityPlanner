package com.learnPlanner.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.learnPlanner.dao.AssessmentDAO;
import com.learnPlanner.dao.CourseDAO;
import com.learnPlanner.dao.TermDAO;
import com.learnPlanner.entities.Assessment;
import com.learnPlanner.entities.Course;
import com.learnPlanner.entities.Term;

@Database(entities = {Assessment.class, Course.class, Term.class}, version = 17, exportSchema = false)
public abstract class DatabaseBuilder extends RoomDatabase{
    public abstract AssessmentDAO assessmentDAO();
    public abstract CourseDAO courseDAO();
    public abstract TermDAO termDAO();
    private static volatile DatabaseBuilder INSTANCE;

    static DatabaseBuilder getRepository(final Context context) {
        if(INSTANCE == null) {
            synchronized (DatabaseBuilder.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "MyDatabaseBuilder.db")
                        .fallbackToDestructiveMigration()
                        .build();
                }
            }
        }
        return INSTANCE;
    }
}