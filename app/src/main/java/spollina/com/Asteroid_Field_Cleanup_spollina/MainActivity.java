package spollina.com.Asteroid_Field_Cleanup_spollina;

import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity
{


    Timer timer = new Timer();
    List<Asteroid> asteroidList = new ArrayList<Asteroid>();
    List<LaserBlast> laserList = new ArrayList<LaserBlast>();

    TextView scoreTextView;
    TextView replayTextView;

    static ImageView playerCharImage;
    ImageView asteroidImage1;
    ImageView asteroidImage2;
    ImageView asteroidImage3;
    ImageView asteroidImage4;
    ImageView asteroidImage5;
    ImageView asteroidImage6;
    ImageView asteroidImage7;
    ImageView asteroidImage8;
    ImageView asteroidImage9;
    ImageView asteroidImage10;
    ImageView asteroidImage11;
    ImageView asteroidImage12;
    ImageView asteroidImage13;
    ImageView asteroidImage14;
    ImageView asteroidImage15;
    ImageView asteroidImage16;
    ImageView asteroidImage17;
    ImageView asteroidImage18;
    ImageView asteroidImage19;
    ImageView asteroidImage20;

    ImageView laserBlast1;
    ImageView laserBlast2;
    ImageView laserBlast3;
    ImageView laserBlast4;
    ImageView laserBlast5;
    ImageView laserBlast6;
    ImageView laserBlast7;
    ImageView laserBlast8;
    ImageView energyShieldEffect;
    static BugView bugView;

    Button replayButton;
    Button joystickHead;
    ImageButton shieldButton;
    ImageButton laserButton;

    float playerCharX = 0, playerCharY = 0;
    float screenX, screenY;

    boolean blocking = false;
    boolean shieldOnCooldown = false;

    int score = 0;
    int asteroidNum = 1;
    int laserNum = 0;

    boolean gameRunning = true;

    final int FPS = 30;

    // Declared here instead of in onCreate because the garbage collector was cleaning it up after onCreate had finished firing, resulting in the music only playing halfway through and not looping
    static MediaPlayer gameMusic;

    private static final float MAX_BUG_SPEED_DP_PER_S = 220f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        replayTextView = (TextView) findViewById(R.id.replayTextView);
        playerCharImage = (ImageView) findViewById(R.id.playerCharacter);
        asteroidImage1 = (ImageView) findViewById(R.id.asteroid1);
        asteroidImage2 = (ImageView) findViewById(R.id.asteroid2);
        asteroidImage3 = (ImageView) findViewById(R.id.asteroid3);
        asteroidImage4 = (ImageView) findViewById(R.id.asteroid4);
        asteroidImage5 = (ImageView) findViewById(R.id.asteroid5);
        asteroidImage6 = (ImageView) findViewById(R.id.asteroid6);
        asteroidImage7 = (ImageView) findViewById(R.id.asteroid7);
        asteroidImage8 = (ImageView) findViewById(R.id.asteroid8);
        asteroidImage9 = (ImageView) findViewById(R.id.asteroid9);
        asteroidImage10 = (ImageView) findViewById(R.id.asteroid10);
        asteroidImage11 = (ImageView) findViewById(R.id.asteroid11);
        asteroidImage12 = (ImageView) findViewById(R.id.asteroid12);
        asteroidImage13 = (ImageView) findViewById(R.id.asteroid13);
        asteroidImage14 = (ImageView) findViewById(R.id.asteroid14);
        asteroidImage15 = (ImageView) findViewById(R.id.asteroid15);
        asteroidImage16 = (ImageView) findViewById(R.id.asteroid16);
        asteroidImage17 = (ImageView) findViewById(R.id.asteroid17);
        asteroidImage18 = (ImageView) findViewById(R.id.asteroid18);
        asteroidImage19 = (ImageView) findViewById(R.id.asteroid19);
        asteroidImage20 = (ImageView) findViewById(R.id.asteroid20);
        laserBlast1 = (ImageView) findViewById(R.id.laserBlast1);
        laserBlast2 = (ImageView) findViewById(R.id.laserBlast2);
        laserBlast3 = (ImageView) findViewById(R.id.laserBlast3);
        laserBlast4 = (ImageView) findViewById(R.id.laserBlast4);
        laserBlast5 = (ImageView) findViewById(R.id.laserBlast5);
        laserBlast6 = (ImageView) findViewById(R.id.laserBlast6);
        laserBlast7 = (ImageView) findViewById(R.id.laserBlast7);
        laserBlast8 = (ImageView) findViewById(R.id.laserBlast8);
        energyShieldEffect = (ImageView) findViewById(R.id.energyShieldEffect);
        replayButton = (Button) findViewById(R.id.replayButton);
        joystickHead = (Button) findViewById(R.id.joystickHead);
        shieldButton = (ImageButton) findViewById(R.id.shieldButton);
        laserButton = (ImageButton) findViewById(R.id.fireBlasterButton);
        bugView = (BugView) findViewById(R.id.bugview);

        TimerTask updateGame = new UpdateGameTask();
        timer.scheduleAtFixedRate(updateGame, 0, 1000 / FPS);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenX = size.x;
        screenY = size.y;

        Joystick joystick = (Joystick) findViewById(R.id.joystick);
        joystick.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {
            }
            @Override
            public void onDrag(float degrees, float offset)
            {
                playerCharX = bugView.position.x;
                playerCharY = bugView.position.y;
                bugView.setVelocity((float) Math.cos(degrees * Math.PI / 180f) * offset * MAX_BUG_SPEED_DP_PER_S,
                                    -(float) Math.sin(degrees * Math.PI / 180f) * offset * MAX_BUG_SPEED_DP_PER_S);
            }
            @Override
            public void onUp() {
                bugView.setVelocity(0, 0);
            }
        });

        createLaserList();

        // Loop game music
        gameMusic = MediaPlayer.create(this, R.raw.game_music);
        gameMusic.setLooping(true);
        gameMusic.start();

        // Move images off screen until called
        asteroidImage1.setX(screenX * 2);
        asteroidImage2.setX(screenX * 2);
        asteroidImage3.setX(screenX * 2);
        asteroidImage4.setX(screenX * 2);
        asteroidImage5.setX(screenX * 2);
        asteroidImage6.setX(screenX * 2);
        asteroidImage7.setX(screenX * 2);
        asteroidImage8.setX(screenX * 2);
        asteroidImage9.setX(screenX * 2);
        asteroidImage10.setX(screenX * 2);
        asteroidImage11.setX(screenX * 2);
        asteroidImage12.setX(screenX * 2);
        asteroidImage13.setX(screenX * 2);
        asteroidImage14.setX(screenX * 2);
        asteroidImage15.setX(screenX * 2);
        asteroidImage16.setX(screenX * 2);
        asteroidImage17.setX(screenX * 2);
        asteroidImage18.setX(screenX * 2);
        asteroidImage19.setX(screenX * 2);
        asteroidImage20.setX(screenX * 2);
        laserBlast1.setVisibility(View.INVISIBLE);
        laserBlast2.setVisibility(View.INVISIBLE);
        laserBlast3.setVisibility(View.INVISIBLE);
        laserBlast4.setVisibility(View.INVISIBLE);
        laserBlast5.setVisibility(View.INVISIBLE);
        laserBlast6.setVisibility(View.INVISIBLE);
        laserBlast7.setVisibility(View.INVISIBLE);
        laserBlast8.setVisibility(View.INVISIBLE);
        energyShieldEffect.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (gameMusic.isPlaying())
        {
            gameMusic.pause();
        }
        gameRunning = false;
    }


    @Override
    public void onResume() {
        gameMusic.seekTo(0);
        gameMusic.start();
        gameRunning = true;
        super.onResume();
    }

    @Override
    public void onRestart() {
        gameMusic.seekTo(0);
        gameMusic.start();
        gameRunning = true;
        super.onRestart();
    }


    public void shield_click(View view)
    {
        if(!shieldOnCooldown)
        {
            // Handle sound
            final MediaPlayer shieldStartMP = MediaPlayer.create(this, R.raw.shield_start);
            shieldStartMP.start();
            shieldStartMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    shieldStartMP.release();
                }
            });
            blocking = true;
            shieldOnCooldown = true;
            energyShieldEffect.setVisibility(View.VISIBLE);
            shieldButton.setImageResource(R.drawable.energy_shield_button_greyed);

            // Deflect Asteroid for 2 seconds
            int invincibilityTime = 2000;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    blocking = false;
                    energyShieldEffect.setVisibility(View.INVISIBLE);
                    shieldCooldown();
                }
            }, invincibilityTime);
        }
    }

    public void shieldCooldown()
    {
        // Shield unusable for 2 seconds
        int shieldCooldownTime = 2000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shieldOnCooldown = false;
                shieldButton.setImageResource(R.drawable.energy_shield_button);
            }
        }, shieldCooldownTime);
    }

    public void fireBlaster_click(View view)
    {
        final MediaPlayer laserBlastMP = MediaPlayer.create(this, R.raw.laser_blast);
        laserBlastMP.start();
        laserBlastMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                laserBlastMP.release();

            }
        });


        LaserBlast currentLaser;
        switch(laserNum)
        {
            case 0 :
                currentLaser = laserList.get(laserNum);
                currentLaser.speed = 50f;
                currentLaser.posX = (playerCharX);
                currentLaser.posY = (playerCharY + (currentLaser.view.getWidth() / 2));
                currentLaser.view.setX(currentLaser.posX);
                currentLaser.view.setY(currentLaser.posY);
                currentLaser.active = true;
                currentLaser.view.setVisibility(View.VISIBLE);
                laserNum++;
                break;
            case 1 :
                currentLaser = laserList.get(laserNum);
                currentLaser.speed = 50f;
                currentLaser.posX = (playerCharX);
                currentLaser.posY = (playerCharY + (currentLaser.view.getWidth() / 2));
                currentLaser.view.setX(currentLaser.posX);
                currentLaser.view.setY(currentLaser.posY);
                currentLaser.active = true;
                currentLaser.view.setVisibility(View.VISIBLE);
                laserNum++;
                break;
            case 2 :
                currentLaser = laserList.get(laserNum);
                currentLaser.speed = 50f;
                currentLaser.posX = (playerCharX);
                currentLaser.posY = (playerCharY + (currentLaser.view.getWidth() / 2));
                currentLaser.view.setX(currentLaser.posX);
                currentLaser.view.setY(currentLaser.posY);
                currentLaser.active = true;
                currentLaser.view.setVisibility(View.VISIBLE);
                laserNum++;
                break;
            case 3 :
                currentLaser = laserList.get(laserNum);
                currentLaser.speed = 50f;
                currentLaser.posX = (playerCharX);
                currentLaser.posY = (playerCharY + (currentLaser.view.getWidth() / 2));
                currentLaser.view.setX(currentLaser.posX);
                currentLaser.view.setY(currentLaser.posY);
                currentLaser.active = true;
                currentLaser.view.setVisibility(View.VISIBLE);
                laserNum++;
                break;
            case 4 :
                currentLaser = laserList.get(laserNum);
                currentLaser.speed = 50f;
                currentLaser.posX = (playerCharX);
                currentLaser.posY = (playerCharY + (currentLaser.view.getWidth() / 2));
                currentLaser.view.setX(currentLaser.posX);
                currentLaser.view.setY(currentLaser.posY);
                currentLaser.active = true;
                currentLaser.view.setVisibility(View.VISIBLE);
                laserNum++;
                break;
            case 5 :
                currentLaser = laserList.get(laserNum);
                currentLaser.speed = 50f;
                currentLaser.posX = (playerCharX);
                currentLaser.posY = (playerCharY + (currentLaser.view.getWidth() / 2));
                currentLaser.view.setX(currentLaser.posX);
                currentLaser.view.setY(currentLaser.posY);
                currentLaser.active = true;
                currentLaser.view.setVisibility(View.VISIBLE);
                laserNum++;
                break;
            case 6 :
                currentLaser = laserList.get(laserNum);
                currentLaser.speed = 50f;
                currentLaser.posX = (playerCharX);
                currentLaser.posY = (playerCharY + (currentLaser.view.getWidth() / 2));
                currentLaser.view.setX(currentLaser.posX);
                currentLaser.view.setY(currentLaser.posY);
                currentLaser.active = true;
                currentLaser.view.setVisibility(View.VISIBLE);
                laserNum++;
                break;
            case 7 :
                currentLaser = laserList.get(laserNum);
                currentLaser.speed = 50f;
                currentLaser.posX = (playerCharX);
                currentLaser.posY = (playerCharY + (currentLaser.view.getWidth() / 2));
                currentLaser.view.setX(currentLaser.posX);
                currentLaser.view.setY(currentLaser.posY);
                currentLaser.active = true;
                currentLaser.view.setVisibility(View.VISIBLE);
                laserNum = 0;
                break;
        }
    }

    public void resetGame()
    {
        // Reset score
        score = 0;

        // Reset Asteroids
        asteroidNum = 1;
        for(Asteroid asteroid : asteroidList)
        {
            asteroid.view.setX(screenX * 2);
        }
        asteroidList.clear();

        // Reset LaserBlasts
        for(LaserBlast laser : laserList)
        {
            laser.active = false;
            laser.view.setY(screenY * 2);
        }

        // Reset player
        bugView.position.x = 0;
        bugView.position.y = 0;
        playerCharX = 0;
        playerCharY = 0;
        playerCharImage.setX(playerCharX);
        playerCharImage.setY(playerCharY);
        shipExplosionPlayed = false;

        // Re-enable buttons
        enableButtons();
        gameRunning = true;
    }

    public void gameVictory()
    {
        replayTextView.setText(R.string.victory_message);
        replayTextView.setVisibility(View.VISIBLE);
        replayButton.setVisibility(View.VISIBLE);

        for(Asteroid asteroids : asteroidList)
        {
            asteroids.speed = 0;
        }
        disableButtons();
        gameRunning = false;
    }

    public void replayButton_click(View view)
    {
        replayTextView.setVisibility(View.INVISIBLE);
        replayButton.setVisibility(View.INVISIBLE);
        resetGame();
    }

    public void enableButtons()
    {
        shieldButton.setEnabled(true);
        laserButton.setEnabled(true);
    }

    public void disableButtons()
    {
        shieldButton.setEnabled(false);
        laserButton.setEnabled(false);
    }

    public void createAsteroid(int currentAsteroid)
    {
        Asteroid newAsteroidToAdd;

        switch(currentAsteroid)
        {
            case 1 :
                newAsteroidToAdd = new Asteroid(asteroidImage1, screenX + (screenX / 4), (screenY / 2),((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 2 :
                newAsteroidToAdd = new Asteroid(asteroidImage2, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 3 :
                newAsteroidToAdd = new Asteroid(asteroidImage3, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 4 :
                newAsteroidToAdd = new Asteroid(asteroidImage4, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 5 :
                newAsteroidToAdd = new Asteroid(asteroidImage5, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 6 :
                newAsteroidToAdd = new Asteroid(asteroidImage6, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 7 :
                newAsteroidToAdd = new Asteroid(asteroidImage7, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 8 :
                newAsteroidToAdd = new Asteroid(asteroidImage8, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 9 :
                newAsteroidToAdd = new Asteroid(asteroidImage9, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 10 :
                newAsteroidToAdd = new Asteroid(asteroidImage10, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 11 :
                newAsteroidToAdd = new Asteroid(asteroidImage11, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 12 :
                newAsteroidToAdd = new Asteroid(asteroidImage12, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 13 :
                newAsteroidToAdd = new Asteroid(asteroidImage13, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 14 :
                newAsteroidToAdd = new Asteroid(asteroidImage14, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 15 :
                newAsteroidToAdd = new Asteroid(asteroidImage15, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 16 :
                newAsteroidToAdd = new Asteroid(asteroidImage16, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 17 :
                newAsteroidToAdd = new Asteroid(asteroidImage17, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 18 :
                newAsteroidToAdd = new Asteroid(asteroidImage18, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 19 :
                newAsteroidToAdd = new Asteroid(asteroidImage19, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum++;
                break;
            case 20:
                newAsteroidToAdd = new Asteroid(asteroidImage20, screenX + (screenX / 4), (screenY / 2), ((-1) * randomNumByRange(0.25, 0.85)), randomNumByRange(-0.9, 0.9), (float)randomNumByRange(25.0, 40));
                asteroidList.add(newAsteroidToAdd);
                asteroidNum = 1;
                break;
        }
    }

    public void createLaserList()
    {
        LaserBlast laser1 = new LaserBlast(laserBlast1, screenX * 2, screenY * 3, 1, 0, 50f);
        laserList.add(laser1);

        LaserBlast laser2 = new LaserBlast(laserBlast2, screenX * 2, screenY * 3, 1, 0, 50f);
        laserList.add(laser2);

        LaserBlast laser3 = new LaserBlast(laserBlast3, screenX * 2, screenY * 3, 1, 0, 50f);
        laserList.add(laser3);

        LaserBlast laser4 = new LaserBlast(laserBlast4, screenX * 2, screenY * 3, 1, 0, 50f);
        laserList.add(laser4);

        LaserBlast laser5 = new LaserBlast(laserBlast5, screenX * 2, screenY * 3, 1, 0, 50f);
        laserList.add(laser5);

        LaserBlast laser6 = new LaserBlast(laserBlast6, screenX * 2, screenY * 3, 1, 0, 50f);
        laserList.add(laser6);

        LaserBlast laser7 = new LaserBlast(laserBlast7, screenX * 2, screenY * 3, 1, 0, 50f);
        laserList.add(laser7);

        LaserBlast laser8 = new LaserBlast(laserBlast8, screenX * 2, screenY * 3, 1, 0, 50f);
        laserList.add(laser8);
    }

    class UpdateGameTask extends TimerTask
    {
        long timeRun = 0;
        @Override
        public void run()
        {
            MainActivity.this.runOnUiThread(new Runnable()
            {
                @Override public void run()
                {

                    if(gameRunning)
                    {

                        // every 1 seconds-ish add asteroids
                        if ((timeRun % 25 == 0))
                        {
                            createAsteroid(asteroidNum);
                        }

                        // Handle Player Movement
                        playerCharImage.setX(bugView.position.x);
                        playerCharImage.setY(bugView.position.y);


                        // Keep shield effect aligned with ship during movement
                        if(blocking)
                        {
                            // Handle shield visual effect
                            energyShieldEffect.setX(playerCharX - (playerCharImage.getWidth() / 5) + 2);
                            energyShieldEffect.setY(playerCharY - (playerCharImage.getHeight() / 5) + 2);
                        }

                        for (Asteroid asteroid : asteroidList)
                        {
                            // Handle Asteroid movement
                            asteroid.posX += (asteroid.speed * asteroid.directionX);
                            asteroid.posY += (asteroid.speed * asteroid.directionY);
                            asteroid.view.setX(asteroid.posX);
                            asteroid.view.setY(asteroid.posY);

                            // Detect/handle hitting top/bottom of screen
                            if ((asteroid.posY + asteroid.view.getWidth() + 70) > screenY || asteroid.posY < 0)
                            {
                                asteroid.directionY *= -1;
                            }

                            // Collision handling
                            Rect asteroidRect = new Rect();
                            Rect playerCharRect = new Rect();
                            asteroid.view.getHitRect(asteroidRect);
                            playerCharImage.getHitRect(playerCharRect);

                            if (asteroidRect.intersect(playerCharRect))
                            {
                                asteroidCollisionHandling(asteroid, asteroidRect, playerCharRect);
                            }

                            for (LaserBlast laser : laserList)
                            {
                                if (laser.active)
                                {
                                    // Handle laser movement
                                    if (laser.posX < screenX)
                                    {
                                        laser.posX += (laser.speed * laser.directionX) / asteroidList.size();
                                        laser.view.setX(laser.posX);

                                        // Handle laser collision
                                        Rect laserRect = new Rect();
                                        laser.view.getHitRect(laserRect);
                                        laserCollisionHandling(asteroid, asteroidRect, laser, laserRect);
                                    }
                                    else
                                    {
                                        laser.active = false;

                                    }
                                }
                            }
                        }
                        scoreTextView.setText(getString(R.string.score_text, Integer.toString(score)));
                        timeRun++;
                    }
                }

            });
        }
    }

    boolean shipExplosionPlayed = false;
    public void asteroidCollisionHandling(Asteroid asteroid, Rect asteroidRect, Rect playerCharRect)
    {
        boolean collision;

        collision = asteroidRect.intersect(playerCharRect);
        if(collision)
        {
            if (blocking)
            {
                // Detect direction and redirect Asteroid if it collides with ship
                double width = 0.5 * (asteroidRect.width() + playerCharRect.width());
                double height = 0.5 * (asteroidRect.height() + playerCharRect.height());
                double directionX = asteroidRect.centerX() - playerCharRect.centerX();
                double directionY = asteroidRect.centerY() - playerCharRect.centerY();

                if (abs(directionX) <= width && abs(directionY) <= height)
                {
                    // Play deflection noise
                    final MediaPlayer shieldUseMP = MediaPlayer.create(this, R.raw.shield_use);
                    shieldUseMP.start();
                    shieldUseMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            shieldUseMP.release();

                        }
                    });
                    score -= 25;

                    double widthY = width * directionY;
                    double heightX = height * directionX;

                    if (widthY > heightX)
                    {
                        // Collision at the bottom
                        if (widthY > -heightX)
                        {
                            asteroid.directionY *= -1;
                        }
                        // Collision on the left
                        else
                        {
                            asteroid.directionX *= -1;
                        }
                    }
                    else
                    {
                        // Collision on the right
                        if (widthY > -heightX)
                        {
                            asteroid.directionX *= -1;
                        }

                        // Collision at the top
                        else
                        {
                            asteroid.directionY *= -1;
                        }
                    }
                }
            }

            else
            {
                if(!shipExplosionPlayed)
                {
                    // Play ship explosion animation
                    final MediaPlayer shipExplosionMP = MediaPlayer.create(this, R.raw.ship_explosion);
                    shipExplosionMP.setVolume(0.5f, 0.5f);
                    shipExplosionMP.start();
                    shipExplosionMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            shipExplosionMP.release();
                        }
                    });

                    shipExplosionPlayed = true;
                }

                replayTextView.setText(R.string.death_message);
                replayTextView.setVisibility(View.VISIBLE);
                replayButton.setVisibility(View.VISIBLE);

                for(Asteroid asteroids : asteroidList)
                {
                    asteroids.speed = 0;
                }
                disableButtons();
                gameRunning = false;
            }
        }
    }

    public void laserCollisionHandling(Asteroid asteroid, Rect asteroidRect, LaserBlast laser, Rect laserRect)
    {
        if(laser.active)
        {
            if (asteroidRect.intersect(laserRect))
            {
                // Play asteroid explosion noise
                final MediaPlayer asteroidExplosionMP = MediaPlayer.create(this, R.raw.asteroid_explosion);
                asteroidExplosionMP.start();
                asteroidExplosionMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        asteroidExplosionMP.release();
                    }
                });

                asteroid.posX = screenX * 2;
                asteroid.view.setX(asteroid.posX);
                asteroid.posY = screenY * 2;
                asteroid.view.setY(asteroid.posY);

                laser.active = false;
                laser.view.setVisibility(View.INVISIBLE);

                score += 1000;

                // Get 3000 points for victory
                if(score >= 3000)
                {
                    gameVictory();
                }
            }
        }
    }

    //Generate a number between the min and max values provided
    public double randomNumByRange(double min, double max)
    {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }






}
