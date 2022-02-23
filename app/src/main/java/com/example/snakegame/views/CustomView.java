package com.example.snakegame.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.snakegame.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CustomView extends View {
    private Rect rect;
    private Paint paint;
    private final Random Rnd = new Random();

    private static class Settings {
        private static int SquareSize;
        private static int Speed;

        private static class Field {
            private static int HEIGHT;
            private static int WIDTH;
        }
    }

    private static class Snake {
        private static List<int[]> Body;

        private static Direction Direction;

        private static Direction NextDirection;

        private static boolean Grow;

        private static boolean Dead;
    }

    private static class Apple {
        private static int[] Location;
    }

    Timer timer = new Timer();

    public CustomView(Context context) {
        super(context);

        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        init(set, true);
    }

    private void init(@Nullable AttributeSet set, boolean firstInit) {
        rect = new Rect();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Settings.SquareSize = 100;
        Settings.Field.WIDTH = 10;
        Settings.Field.HEIGHT = 10;
        Settings.Speed = 500;
        Snake.Body = new ArrayList<>();
        Snake.Direction = Snake.NextDirection = Direction.RIGHT;
        Snake.Grow = false;
        Snake.Dead = false;
        Apple.Location = new int[2];

        if (firstInit) {
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    postInvalidate();
                }
            }, 0, Settings.Speed);
        } else {
            MainActivity.GameOverText.setVisibility(View.INVISIBLE);
            MainActivity.TryAgainButton.setVisibility(View.INVISIBLE);
            MainActivity.PointsView.setText(String.valueOf(0));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = getMeasuredWidth();
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        nextFrame(canvas);
    }

    private void drawSnake(Canvas canvas) {
        paint.setColor(Color.GREEN);

        for (int i = 0; i < Snake.Body.size(); i++) {
            rect.left = Snake.Body.get(i)[0] * Settings.SquareSize;
            rect.top = Snake.Body.get(i)[1] * Settings.SquareSize;
            rect.right = rect.left + Settings.SquareSize;
            rect.bottom = rect.top + Settings.SquareSize;

            canvas.drawRect(rect, paint);
        }
    }

    private void drawApple(Canvas canvas) {
        paint.setColor(Color.RED);
        rect.left = Apple.Location[0] * Settings.SquareSize;
        rect.top = Apple.Location[1] * Settings.SquareSize;
        rect.right = rect.left + Settings.SquareSize;
        rect.bottom = rect.top + Settings.SquareSize;

        canvas.drawRect(rect, paint);
    }

    private void drawField(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
    }

    private void nextFrame(Canvas canvas)
    {
        if (Snake.Dead) {
            MainActivity.GameOverText.setVisibility(View.VISIBLE);
            MainActivity.TryAgainButton.setVisibility(View.VISIBLE);
            return;
        }

        if (Snake.Body.size() == 0) {
            Snake.Body.add(new int[] {0, 0});
            Settings.SquareSize = canvas.getWidth() / Settings.Field.WIDTH;
            moveApple(canvas);
            MainActivity.TryAgainButton.setOnClickListener(v -> init(null, false));
        }

        drawField(canvas);

        Snake.Direction = Snake.NextDirection;

        switch (Snake.Direction) {
            case UP:
                Snake.Body.add(0, new int[] {Snake.Body.get(0)[0], Snake.Body.get(0)[1] - 1});
                break;
            case RIGHT:
                Snake.Body.add(0, new int[] {Snake.Body.get(0)[0] + 1, Snake.Body.get(0)[1]});
                break;
            case DOWN:
                Snake.Body.add(0, new int[] {Snake.Body.get(0)[0], Snake.Body.get(0)[1] + 1});
                break;
            case LEFT:
                Snake.Body.add(0, new int[] {Snake.Body.get(0)[0] - 1, Snake.Body.get(0)[1]});
                break;
        }

        if (onSnakeBody(Snake.Body.get(0)[0], Snake.Body.get(0)[1]) || snakeOutsideField()) {
            Snake.Dead = true;
        }

        if (Snake.Dead) {
            return;
        }

        if (Snake.Body.get(0)[0] == Apple.Location[0] && Snake.Body.get(0)[1] == Apple.Location[1]) {
            moveApple(canvas);
            Snake.Grow = true;
        } else {
            drawApple(canvas);
        }

        if (!Snake.Grow) {
            Snake.Body.remove(Snake.Body.size() - 1);
        } else {
            Snake.Grow = false;
            MainActivity.PointsView.setText(String.valueOf(Snake.Body.size() - 1));
        }

        drawSnake(canvas);
    }

    private void moveApple(Canvas canvas) {
        do {
            Apple.Location[0] = Rnd.nextInt(Settings.Field.WIDTH);
            Apple.Location[1] = Rnd.nextInt(Settings.Field.HEIGHT);
        } while (onSnakeBody(Apple.Location[0], Apple.Location[1]));

        drawApple(canvas);
    }

    private boolean onSnakeBody(int x, int y) {
        // Skip head
        for (int i = 1; i < Snake.Body.size(); i++) {
            if (Snake.Body.get(i)[0] == x && Snake.Body.get(i)[1] == y) {
                return true;
            }
        }

        return false;
    }

    private boolean snakeOutsideField() {
        if (Snake.Body.get(0)[0] <= -1 || Snake.Body.get(0)[0] >= Settings.Field.WIDTH || Snake.Body.get(0)[1] >= Settings.Field.HEIGHT  || Snake.Body.get(0)[1] <= -1)
        {
            Snake.Dead = true;
        }

        return false;
    }

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    public static boolean switchDirection(Direction direction) {
        switch (direction) {
            case UP:
                if (CustomView.Snake.Direction == Direction.DOWN) return true;
                CustomView.Snake.NextDirection = Direction.UP;
                return true;
            case RIGHT:
                if (CustomView.Snake.Direction == Direction.LEFT) return true;
                CustomView.Snake.NextDirection = Direction.RIGHT;
                return true;
            case DOWN:
                if (CustomView.Snake.Direction == Direction.UP) return true;
                CustomView.Snake.NextDirection = Direction.DOWN;
                return true;
            case LEFT:
                if (CustomView.Snake.Direction == Direction.RIGHT) return true;
                CustomView.Snake.NextDirection = Direction.LEFT;
                return true;
            default:
                return false;
        }
    }
}