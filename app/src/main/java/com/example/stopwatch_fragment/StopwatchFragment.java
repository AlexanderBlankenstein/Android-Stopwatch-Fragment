package com.example.stopwatch_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Locale;

public class StopwatchFragment extends Fragment implements View.OnClickListener {

    //number of seconds displayed on the stopwatch
    private int seconds = 0;
    //is the stopwatch running?
    private boolean running;
    //was stopwatch running before onStop method call?
    private boolean wasRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //If app was destroyed prior, reload from saved state.
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        runTimer(layout);
        Button startButton = layout.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        Button stopButton = layout.findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);
        Button resetButton = layout.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.start_button:
                onClickStart();
                break;
            case R.id.stop_button:
                onClickStop();
                break;
            case R.id.reset_button:
                onClickReset();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

    //If activity is paused, stop the stopwatch
    @Override
    public void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    //If stopwatch was running when it was paused, then set it to running again.
    @Override
    public void onResume() {
        super.onResume();
        if (wasRunning) {
           running = true;
        }
    }

    //Start the stopwatch running when the Start button is clicked.
    private void onClickStart() {
        running = true;
    }

    //Stop the stopwatch running when the Stop button is clicked
    private void onClickStop() {
        running = false;
    }

    //Reset the stopwatch when the Reset button
    private void onClickReset() {
        running = false;
        seconds = 0;
    }

    //Sets the number of seconds on the timer
    private void runTimer(View view) {
        final TextView timeView = view.findViewById(R.id.time_view);
        final Handler handler = new Handler();

        //Update the stopwatch using a handler with a delay of 1 second
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds/3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds%60;
                String time = String.format(Locale.getDefault(),
                        "%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }

                //Delay post so that display only changes after a second.
                // This makes it not very accurate but shows example of a delay.
                handler.postDelayed(this, 1000);
            }
        });
    }
}