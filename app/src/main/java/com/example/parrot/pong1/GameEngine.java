package com.example.parrot.pong1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameEngine extends SurfaceView implements Runnable {

    // -----------------------------------
    // ## ANDROID DEBUG VARIABLES
    // -----------------------------------

    // Android debug variables
    final static String TAG="PONG-GAME";

    // -----------------------------------
    // ## SCREEN & DRAWING SETUP VARIABLES
    // -----------------------------------

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;



    // -----------------------------------
    // ## GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------

    // represents the (x,y) position of the ball
    Point ballPosition;
    final int BALL_WIDTH = 45;


    // variables for setting (left, top) position of paddle
    Point racketPosition;
Point racketPosition2;
    // variables for controlling size of paddle
    final int DISTANCE_FROM_WALL = 100;
    final int PADDLE_WIDTH = 10;
    final int PADDLE_HEIGHT = 200;


    // ----------------------------
    // ## GAME STATS - number of lives, score, etc
    // ----------------------------
    int score = 0;

    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();

        // @TODO: Add your sprites to this section
        // This is optional. Use it to:
        //  - setup or configure your sprites
        //  - set the initial position of your sprites


        // set the initial position of the ball to be middle of screen
        ballPosition = new Point();
        ballPosition.x = this.screenWidth / 2;
        ballPosition.y = this.screenHeight / 2;

        // set the initial position of the racket
        racketPosition = new Point();
        racketPosition.x = DISTANCE_FROM_WALL;   // left
        racketPosition.y = (this.screenHeight/2  - PADDLE_HEIGHT);                // top
// set the initial position of the racket 2
        racketPosition2 = new Point();
        racketPosition2.x= (this.screenWidth  - DISTANCE_FROM_WALL - PADDLE_WIDTH);
        racketPosition2.y = (this.screenHeight/2 - PADDLE_HEIGHT);


        // @TODO: Any other game setup stuff goes here


    }

    // ------------------------------
    // HELPER FUNCTIONS
    // ------------------------------

    // This function prints the screen height & width to the screen.
    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }


    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------


    boolean BallMovingRight = true;
    final int BALL_SPEED = 50;

    boolean Paddle2MovingUP = true;
    final int PADDLE_DISTANCE = 50;
boolean gameOver = true;

    // 1. Tell Android the (x,y) positions of your sprites
    public void updatePositions() {
        // @TODO: Update the position of the sprites

        if (BallMovingRight == true) {
            ballPosition.x = ballPosition.x + BALL_SPEED;
        }
        else {
            ballPosition.x = ballPosition.x - BALL_SPEED;
        }


        // GAME LOSE CONDITION
        if(ballPosition.x < 0)
        {
            gameOver = true;
        }
        /// paddle 2 is moving
        if (Paddle2MovingUP == true){
            racketPosition2.y = racketPosition2.y + PADDLE_DISTANCE;

        }
        else{
            racketPosition2.y = racketPosition2.y - PADDLE_DISTANCE;
        }

        // @TODO: Collision detection code
        if (ballPosition.x <= DISTANCE_FROM_WALL) {
            Log.d(TAG, "Ball reached bottom of screen. Changing direction!");
            BallMovingRight = true;
        }

        if (ballPosition.x >= screenWidth - DISTANCE_FROM_WALL- PADDLE_WIDTH) {
            Log.d(TAG, "Ball reached TOP of screen. Changing direction!");
            BallMovingRight = false;
            this.score = this.score + 1;
        }
// collison detection for paddle
        if (racketPosition2.y > screenHeight)
            Paddle2MovingUP  = false;
        if ( racketPosition2.y < 0)
            Paddle2MovingUP = true;
        // Log.d(TAG, "Ball y-position: " + ballPosition.y);

    }




    // 2. Tell Android to DRAW the sprites at their positions
    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------
            // Put all your drawing code in this section

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,0,0,255));
            paintbrush.setColor(Color.WHITE);


            //@TODO: Draw the sprites (rectangle, circle, etc)

            int left = ballPosition.x;
            int top = ballPosition.y;
            int right = ballPosition.x + BALL_WIDTH;  // left + 45
            int bottom = ballPosition.y + BALL_WIDTH; // top + 45
            canvas.drawRect(left, top, right, bottom, paintbrush);

            // draw the paddle
            int paddleLeft = racketPosition.x;
            int paddleTop = racketPosition.y;
            int paddleRight = racketPosition.x + PADDLE_WIDTH;
            int paddleBottom = racketPosition.y + 2*PADDLE_HEIGHT;
            canvas.drawRect(paddleLeft, paddleTop, paddleRight, paddleBottom, paintbrush);
            //draw the paddle 2
            int paddle2Left = racketPosition2.x ;
            int paddle2Top = racketPosition2.y;
            int paddle2Right = racketPosition2.x + PADDLE_WIDTH;
            int paddle2Bottom = racketPosition2.y - 2*PADDLE_HEIGHT;
            canvas.drawRect(paddle2Left, paddle2Top, paddle2Right, paddle2Bottom, paintbrush);

            //@TODO: Draw game statistics (lives, score, etc)
            paintbrush.setTextSize(60);
            canvas.drawText("Score: " + this.score, 20, 100, paintbrush);

            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    // Sets the frame rate of the game
    public void setFPS() {
        try {
            gameThread.sleep(50);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {
            // user pushed down on screen

            Log.d(TAG, "The person tapped: (" + event.getX() + "," + event.getY() + ")");

            if (event.getY() < this.screenHeight / 2) {
                Log.d(TAG, "Person clicked LEFT side");
                racketPosition.y = racketPosition.y - PADDLE_DISTANCE ;
            }
            else {
                Log.d(TAG, "Person clicked RIGHT side");
                racketPosition.y = racketPosition.y + PADDLE_DISTANCE;
            }



        }
        else if (userAction == MotionEvent.ACTION_UP) {
            // user lifted their finger
            // for pong, you don't need this, so no code is in here
        }
        return true;
    }
}