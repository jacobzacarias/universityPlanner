package com.learnPlanner.database;
import android.app.Application;

import com.learnPlanner.dao.AssessmentDAO;
import com.learnPlanner.dao.CourseDAO;
import com.learnPlanner.dao.TermDAO;
import com.learnPlanner.entities.Assessment;
import com.learnPlanner.entities.Course;
import com.learnPlanner.entities.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private AssessmentDAO mAssessmentDAO;
    private CourseDAO mCourseDAO;
    private TermDAO mTermDAO;
    private List<Assessment> mAllAssessments;
    private List<Course> mAllCourses;
    private List<Term> mAllTerms;
    private final int TIME_IN_MILLI_SECONDS = 500;
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        DatabaseBuilder db = DatabaseBuilder.getRepository(application);
        mAssessmentDAO = db.assessmentDAO();
        mCourseDAO = db.courseDAO();
        mTermDAO = db.termDAO();
    }

    public List<Assessment> getAllAssessments() {
        databaseExecutor.execute(() -> {
                    mAllAssessments = mAssessmentDAO.getAllAssessments();
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllAssessments;
    }

    public List<Course> getAllCourses() {
        databaseExecutor.execute(() -> {
                    mAllCourses = mCourseDAO.getAllCourses();
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllCourses;
    }

    public List<Term> getAllTerms() {
        databaseExecutor.execute(() -> {
                    mAllTerms = mTermDAO.getAllTerms();
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllTerms;
    }
    //Consider removing the throw for an interrupted exception
    public void insert(Term term) {
        databaseExecutor.execute(() -> {
                    mTermDAO.insert(term);
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Course course) {
        databaseExecutor.execute(() -> {
                    mCourseDAO.insert(course);
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Assessment assessment) {
        databaseExecutor.execute(() -> {
                    mAssessmentDAO.insert(assessment);
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Term term) {
        databaseExecutor.execute(() -> {
                    mTermDAO.update(term);
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Course course) {
        databaseExecutor.execute(() -> {
                    mCourseDAO.update(course);
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Assessment assessment) {
        databaseExecutor.execute(() -> {
                    mAssessmentDAO.update(assessment);
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Term term) {
        databaseExecutor.execute(() -> {
                    mTermDAO.delete(term);
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Assessment assessment) {
        databaseExecutor.execute(() -> {
                    mAssessmentDAO.delete(assessment);
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Course course) {
        databaseExecutor.execute(() -> {
                    mCourseDAO.delete(course);
                }
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}