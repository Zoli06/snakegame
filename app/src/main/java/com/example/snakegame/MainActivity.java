package com.example.snakegame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.example.snakegame.views.CustomView;

public class MainActivity extends AppCompatActivity {
    public static TextView PointsView;
    public static Button TryAgainButton;
    public static TextView GameOverText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PointsView = (TextView) findViewById(R.id.points);
        TryAgainButton = (Button) findViewById(R.id.tryAgain);
        GameOverText = (TextView) findViewById(R.id.gameOver);

        CustomView customView = new CustomView(this);

        Button button1 = (Button) findViewById(R.id.up);
        button1.setOnClickListener(v -> {
            CustomView.switchDirection(CustomView.Direction.UP);
        });

        Button button2 = (Button) findViewById(R.id.right);
        button2.setOnClickListener(v -> {
            CustomView.switchDirection(CustomView.Direction.RIGHT);
        });

        Button button3 = (Button) findViewById(R.id.down);
        button3.setOnClickListener(v -> {
            CustomView.switchDirection(CustomView.Direction.DOWN);
        });

        Button button4 = (Button) findViewById(R.id.left);
        button4.setOnClickListener(v -> {
            CustomView.switchDirection(CustomView.Direction.LEFT);
        });
    }

    public void SetPoints(int points) {
        PointsView.setText(String.valueOf(points));
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_W:
                return CustomView.switchDirection(CustomView.Direction.UP);
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_D:
                return CustomView.switchDirection(CustomView.Direction.RIGHT);
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_S:
                return CustomView.switchDirection(CustomView.Direction.DOWN);
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_A:
                return CustomView.switchDirection(CustomView.Direction.LEFT);
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}