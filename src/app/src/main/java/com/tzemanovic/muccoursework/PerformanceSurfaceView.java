package com.tzemanovic.muccoursework;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tzemanovic.muccoursework.db.TableTeamResult;

import java.text.DateFormatSymbols;
import java.util.List;

/**
 * Created by Tomas Zemanovic on 19/12/2014.
 */
public class PerformanceSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    PerformanceDrawingThread drawingThread;
    List<TableTeamResult> results;

    public PerformanceSurfaceView(Context context, List<TableTeamResult> results) {
        super(context);
        this.results = results;
        getHolder().addCallback(this);
        drawingThread = new PerformanceDrawingThread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // start thread if it's not already running
        if (drawingThread.getState() == Thread.State.NEW)
        {
            drawingThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        drawingThread.setSurfaceSize(surfaceHolder, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stopDrawingThread();
    }

    private void stopDrawingThread() {
        boolean retry = true;
        drawingThread.interrupt();
        // keep on trying to join thread
        while (retry) {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class PerformanceDrawingThread extends Thread {

        volatile private boolean running = false;
        private Paint paint;
        private int width;
        private int height;
        private Resources resources;

        public PerformanceDrawingThread() {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            resources = getResources();
        }

        public void setSurfaceSize(SurfaceHolder surfaceHolder, int width, int height) {
            synchronized (surfaceHolder) {
                this.width = width;
                this.height = height;
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            this.running = false;
        }

        @Override
        public synchronized void start() {
            super.start();
            this.running = true;
        }

        @Override
        public void run() {
            while (running) {
                SurfaceHolder surfaceHolder = getHolder();
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        synchronized (surfaceHolder) {
                            drawGraph(canvas);
                        }
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        final int ROWS_COUNT = 10;
        final int SPACE_X = 5;
        final int SPACE_Y = 3;
        public void drawGraph(Canvas canvas) {
            if (running) {
                // clear canvas
                canvas.drawColor(resources.getColor(R.color.dark_grey));
                paint.setStyle(Paint.Style.STROKE);

                // calculate vertical axis spaces
                Rect bounds = new Rect();
                String text = "100%";
                paint.getTextBounds(text, 0, text.length(), bounds);
                final int spaceLeft = bounds.width() + SPACE_X * 2;
                final int spaceTop = bounds.height() / 2 + SPACE_Y;

                // calculate horizontal axis spaces
                final int spaceBottom = bounds.height() * 2 + SPACE_Y * 3;
                final int spaceRight = bounds.width() / 2 + SPACE_X;

                final int verticalSpace = spaceTop + spaceBottom;
                final int horizontalSpace = spaceLeft + spaceRight;

                // grid
                paint.setColor(resources.getColor(R.color.grey));
                paint.setStrokeWidth(1.0f);
                // grid rows
                for (int i = 0; i < ROWS_COUNT; ++i) {
                    int yPos = (height - verticalSpace) / ROWS_COUNT * i + spaceTop;
                    canvas.drawLine(spaceLeft, yPos, width - spaceRight, yPos, paint);
                }
                // grid columns
                int colsCount = results.size();
                int gridColsCount = colsCount - 1;
                for (int i = 0; i < gridColsCount; ++i) {
                    int xPos = (width - horizontalSpace) / gridColsCount * (i + 1) + spaceLeft;
                    canvas.drawLine(xPos, spaceTop, xPos, height - spaceBottom, paint);
                }

                // axes
                paint.setColor(resources.getColor(R.color.light_grey));
                paint.setStrokeWidth(2.0f);
                // horizontal axis
                canvas.drawLine(spaceLeft, height - spaceBottom, width - spaceRight, height - spaceBottom, paint);
                // vertical axis
                canvas.drawLine(spaceLeft, spaceTop, spaceLeft, height - spaceBottom, paint);

                // axes labels
                // horizontal axis
                paint.setColor(resources.getColor(R.color.red));
                paint.setStyle(Paint.Style.FILL);
                for (int i = 0; i < colsCount; ++i) {
                    TableTeamResult result = results.get(i);
                    String month = monthIntToString(result.getMonth());
                    String year = String.valueOf(result.getYear());
                    float xPos = (width - bounds.width() - SPACE_X - spaceLeft + spaceRight) / (colsCount - 1) * i + spaceLeft - spaceRight;
                    paint.setTypeface(Typeface.DEFAULT_BOLD);
                    canvas.drawText(month, xPos + SPACE_X / 2, height - spaceBottom + bounds.height() + SPACE_Y, paint);
                    paint.setTypeface(Typeface.DEFAULT);
                    canvas.drawText(year, xPos, height - spaceBottom + bounds.height() * 2 + SPACE_Y * 2, paint);
                }
                // vertical axis
                for (int i = 0; i <= ROWS_COUNT; ++i) {
                    String label = String.valueOf((ROWS_COUNT - i) * 100 / ROWS_COUNT) + "%";
                    float yPos = (height - verticalSpace) / ROWS_COUNT * i + bounds.height() + SPACE_Y;
                    canvas.drawText(label, (float)SPACE_X, yPos, paint);
                }

                // graph values
                paint.setStrokeWidth(3.0f);
                paint.setStyle(Paint.Style.STROKE);
                for (int i = 0; i < colsCount - 1; ++i) {
                    TableTeamResult result = results.get(i);
                    TableTeamResult nextResult = results.get(i+1);
                    float ratio = getWinLossRatio(result);
                    float nextRatio = getWinLossRatio(nextResult);
                    int xPos = (width - horizontalSpace) / gridColsCount * i + spaceLeft;
                    int yPos = Math.round((height - verticalSpace) * (1 - ratio) + spaceTop - 1);
                    int nextXPos = (width - horizontalSpace) / gridColsCount * (i + 1) + spaceLeft;
                    int nextYPos = Math.round((height - verticalSpace) * (1 - nextRatio) + spaceTop - 1);
                    canvas.drawLine(xPos, yPos, nextXPos, nextYPos, paint);
                }
            }
        }

        // 1 - Jan, 2 - Feb, etc.
        private String monthIntToString(int month) {
            final String[] shortMonths = new DateFormatSymbols().getShortMonths();
            return shortMonths[month - 1];
        }

        private float getWinLossRatio(TableTeamResult result) {
            int matchCount = result.getWin() + result.getLoss();
            float ratio = 0.0f;
            if (matchCount != 0) {
                ratio = (float) result.getWin() / matchCount;
            }
            return ratio;
        }
    }
}
