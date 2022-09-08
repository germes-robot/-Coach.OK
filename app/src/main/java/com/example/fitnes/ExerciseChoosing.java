package com.example.fitnes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import APIParse.Exercise;

public class ExerciseChoosing extends AppCompatActivity implements ExerciseAdapter.ListItemClickListener {

    private List<Exercise> exerciseList = new ArrayList<>();
    private RecyclerView exerciseRecView;
    private View viewExercise;
    private Toolbar toolbarExercise;
    private ExerciseChoosing current;
    private List<Exercise> inTraining = new ArrayList<>();
    private List<Exercise> chosen;
    private ExerciseAdapter adapter;
    private int trainingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_choose);
        viewExercise = findViewById(R.id.coordinator_exercise);
        toolbarExercise = findViewById(R.id.toolbar_exercise);
        setSupportActionBar(toolbarExercise);
        exerciseRecView = findViewById(R.id.list_exercise);
        exerciseRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        current = this;

        Intent intent = getIntent();
        trainingID = intent.getIntExtra("ID", 0);

        adapter = new ExerciseAdapter(exerciseList, inTraining, current);

        DBRoom.getExerciseForTraining(new DBRoom.OnCallbackGetAllExercise() {
            @Override
            public void onCallback(List<Exercise> exercises) {
                inTraining = exercises;
                chosen = inTraining;
                DBRoom.getAllExerciseDB(new DBRoom.OnCallbackGetAllExercise() {
                    @Override
                    public void onCallback(List<Exercise> exercises) {
                        exerciseList = exercises;
                        adapter = new ExerciseAdapter(exerciseList, inTraining, current);
                        exerciseRecView.setAdapter(adapter);
                    }
                });
            }
        }, trainingID);

    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        Exercise exercise = exerciseList.get(clickedItemIndex);
        if (chosen.contains(exercise)) {
            chosen.remove(exercise);
        } else {
            chosen.add(exercise);
        }
        adapter.setData(chosen);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_training) {
            DBRoom.getTrainingForId(new DBRoom.OnCallbackTraining() {
                @Override
                public void onCallbackTraining(Training trainings) {

                    final Training training = trainings;
                    DBRoom.deleteAllExerciseOfTraining(new DBRoom.OnCallbackComplete() {
                        @Override
                        public void OmComplete() {

                            DBRoom.trainingAndExerciseDB(chosen, training, new DBRoom.OnCallbackComplete() {
                                @Override
                                public void OmComplete() {
                                    inTraining = chosen;
                                    adapter.setData(inTraining);
                                    adapter.notifyDataSetChanged();
                                    Snackbar.make(TrainingDescription.getView(), "You have saved your training", Snackbar.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }
                    }, trainingID);

                }
            }, trainingID);
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose_exercise_menu, menu);
        return true;
    }
}
