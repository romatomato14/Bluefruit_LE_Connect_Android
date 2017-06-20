package com.adafruit.bluefruit.le.connect.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.adafruit.bluefruit.le.connect.R;

public class QuadrantMenu extends AppCompatActivity {

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quadrant_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
*/
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


        /**
         * This is an example of using the accelerometer to integrate the device's
         * acceleration to a position using the Verlet method. This is illustrated with
         * a very simple particle system comprised of a few iron balls freely moving on
         * an inclined wooden table. The inclination of the virtual table is controlled
         * by the device's accelerometer.
         *
         * @see SensorManager
         * @see SensorEvent
         * @see Sensor
         */

        public class AccelerometerPlayActivity extends Activity {

            private SimulationView mSimulationView;
            private SensorManager mSensorManager;
            private PowerManager mPowerManager;
            private WindowManager mWindowManager;

            //private PinIOActivity mPinIOActivity;
            //public ArrayList<PinIOActivity.PinData> mPins = new ArrayList<>();

            private Display mDisplay;
            private PowerManager.WakeLock mWakeLock;

            /** Called when the activity is first created. */
            @Override
            public void onCreate(Bundle savedInstanceState) {
                //Maria added this
                System.out.println("we are in on create of AccelerometerPlayActivity in QuadrantMenu");

                setContentView(R.layout.activity_quadrant_menu);

                //PinIOActivity mPinIOActivity = new PinIOActivity();
                //int store = mPinIOActivity.whatWeNeed();
                //System.out.println("PRINT" + store);

                super.onCreate(savedInstanceState);

                // Get an instance of the SensorManager
                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

                // Get an instance of the PowerManager
                mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);

                // Get an instance of the WindowManager
                mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                mDisplay = mWindowManager.getDefaultDisplay();

                // Create a bright wake lock
                mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass()
                        .getName());

                // instantiate our simulation view and set it as the activity's content
                mSimulationView = new SimulationView(this);
                mSimulationView.setBackgroundResource(R.drawable.menuplain);
                setContentView(mSimulationView);

                //Maria added this code
                //PinIOActivity.PinData.class.cast(PinIOActivity.whatWeNeed());
                //System.out.println("HELLO" + String.valueOf(PinIOActivity.whatWeNeed()));
                //System.out.println("YO WE received analog value: " );
                //int instance = new PinIOActivity().whatWeNeed();
                //System.out.println(instance + " HI, MARIA");
            }

            @Override
            protected void onResume() {
                super.onResume();
        /*
         * when the activity is resumed, we acquire a wake-lock so that the
         * screen stays on, since the user will likely not be fiddling with the
         * screen or buttons.
         */
                mWakeLock.acquire();

                // Start the simulation
                mSimulationView.startSimulation();
            }

            @Override
            protected void onPause() {
                super.onPause();
        /*
         * When the activity is paused, we make sure to stop the simulation,
         * release our sensor resources and wake locks
         */

                // Stop the simulation
                mSimulationView.stopSimulation();

                // and release our wake-lock
                mWakeLock.release();
            }

            class SimulationView extends FrameLayout implements SensorEventListener {
                // diameter of the balls in meters CHANGEBD SIZE
                private static final float sBallDiameter = 0.010f;
                private static final float sBallDiameter2 = sBallDiameter * sBallDiameter;

                private final int mDstWidth;
                private final int mDstHeight;

                private Sensor mAccelerometer;
                private long mLastT;

                private float mXDpi;
                private float mYDpi;
                private float mMetersToPixelsX;
                private float mMetersToPixelsY;
                private float mXOrigin;
                private float mYOrigin;
                private float mSensorX;
                private float mSensorY;
                private float mHorizontalBound;
                private float mVerticalBound;
                private final ParticleSystem mParticleSystem;
                /*
                 * Each of our particle holds its previous and current position, its
                 * acceleration. for added realism each particle has its own friction
                 * coefficient.
                 */
                class Particle extends View {
                    private float mPosX = (float) Math.random();
                    private float mPosY = (float) Math.random();
                    private float mVelX;
                    private float mVelY;

                    public Particle(Context context) {
                        super(context);
                    }

                    public Particle(Context context, AttributeSet attrs) {
                        super(context, attrs);
                    }

                    public Particle(Context context, AttributeSet attrs, int defStyleAttr) {
                        super(context, attrs, defStyleAttr);
                    }

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    public Particle(Context context, AttributeSet attrs, int defStyleAttr,
                                    int defStyleRes) {
                        super(context, attrs, defStyleAttr, defStyleRes);
                    }

                    public void computePhysics(float sx, float sy, float dT) {

                        final float ax = -sx/5;
                        final float ay = -sy/5;

                        mPosX += mVelX * dT + ax * dT * dT / 2;
                        mPosY += mVelY * dT + ay * dT * dT / 2;

                        mVelX += ax * dT;
                        mVelY += ay * dT;
                    }

                    /*
                     * Resolving constraints and collisions with the Verlet integrator
                     * can be very simple, we simply need to move a colliding or
                     * constrained particle in such way that the constraint is
                     * satisfied.
                     */
                    public void resolveCollisionWithBounds() {
                        final float xmax = mHorizontalBound;
                        final float ymax = mVerticalBound;
                        final float x = mPosX;
                        final float y = mPosY;
                        if (x > xmax) {
                            mPosX = xmax;
                            mVelX = 0;
                        } else if (x < -xmax) {
                            mPosX = -xmax;
                            mVelX = 0;
                        }
                        if (y > ymax) {
                            mPosY = ymax;
                            mVelY = 0;
                        } else if (y < -ymax) {
                            mPosY = -ymax;
                            mVelY = 0;
                        }
                    }
                }

                /*
                 * A particle system is just a collection of particles
                 */
                class ParticleSystem {
                    static final int NUM_PARTICLES = 1;
                    private Particle mBalls[] = new Particle[NUM_PARTICLES];

                    ParticleSystem() {
                /*
                 * Initially our particles have no speed or acceleration
                 */
                        for (int i = 0; i < mBalls.length; i++) {
                            mBalls[i] = new Particle(getContext());
                            mBalls[i].setBackgroundResource(R.drawable.transparent);
                            mBalls[i].setLayerType(LAYER_TYPE_HARDWARE, null);
                            addView(mBalls[i], new ViewGroup.LayoutParams(mDstWidth, mDstHeight));
                        }
                    }

                    /*
                     * Update the position of each particle in the system using the
                     * Verlet integrator.
                     */
                    private void updatePositions(float sx, float sy, long timestamp) {
                        final long t = timestamp;
                        if (mLastT != 0) {
                            final float dT = (float) (t - mLastT) / 1000.f /** (1.0f / 1000000000.0f)*/;
                            final int count = mBalls.length;
                            for (int i = 0; i < count; i++) {
                                Particle ball = mBalls[i];
                                ball.computePhysics(sx, sy, dT);
                            }
                        }
                        mLastT = t;
                    }

                    /*
                     * Performs one iteration of the simulation. First updating the
                     * position of all the particles and resolving the constraints and
                     * collisions.
                     */
                    public void update(float sx, float sy, long now) {
                        // update the system's positions
                        updatePositions(sx, sy, now);

                        // We do no more than a limited number of iterations
                        final int NUM_MAX_ITERATIONS = 10;

                /*
                 * Resolve collisions, each particle is tested against every
                 * other particle for collision. If a collision is detected the
                 * particle is moved away using a virtual spring of infinite
                 * stiffness.
                 */
                        boolean more = true;
                        final int count = mBalls.length;
                        for (int k = 0; k < NUM_MAX_ITERATIONS && more; k++) {
                            more = false;
                            for (int i = 0; i < count; i++) {
                                Particle curr = mBalls[i];
                                for (int j = i + 1; j < count; j++) {
                                    Particle ball = mBalls[j];
                                    float dx = ball.mPosX - curr.mPosX;
                                    float dy = ball.mPosY - curr.mPosY;
                                    float dd = dx * dx + dy * dy;
                                    // Check for collisions
                                    if (dd <= sBallDiameter2) {
                                /*
                                 * add a little bit of entropy, after nothing is
                                 * perfect in the universe.
                                 */
                                        dx += ((float) Math.random() - 0.5f) * 0.0001f;
                                        dy += ((float) Math.random() - 0.5f) * 0.0001f;
                                        dd = dx * dx + dy * dy;
                                        // simulate the spring
                                        final float d = (float) Math.sqrt(dd);
                                        final float c = (0.5f * (sBallDiameter - d)) / d;
                                        final float effectX = dx * c;
                                        final float effectY = dy * c;
                                        curr.mPosX -= effectX;
                                        curr.mPosY -= effectY;
                                        ball.mPosX += effectX;
                                        ball.mPosY += effectY;
                                        more = true;
                                    }
                                }
                                curr.resolveCollisionWithBounds();
                            }
                        }
                    }

                    public int getParticleCount() {
                        return mBalls.length;
                    }

                    public float getPosX(int i)
                    {
                        //System.out.println ("X" + mBalls[i].mPosX);
                        return mBalls[i].mPosX;
                    }

                    public float getPosY(int i)
                    {
                        //System.out.println ("Y" + mBalls[i].mPosY);
                        return mBalls[i].mPosY;
                    }


                    //Highlights different icons on the menu
                    public void onRoll()
                    {
                        float x = getPosX(0);
                        float y = getPosY(0);

                        if (y>0 && x>0) {
                            //System.out.println("quad 1");
                            //PinIOActivity.PinData.pin.getanalogValue();

                            mSimulationView.setBackgroundResource(R.drawable.quad1);
                        }
                        else if (y>0 && x<0) {
                            //System.out.println("quad 2");
                            mSimulationView.setBackgroundResource(R.drawable.quad2);
                        }
                        else if (y<0 && x<0) {
                            //System.out.println("quad 3");
                            mSimulationView.setBackgroundResource(R.drawable.quad3);
                        }
                        else {
                            //System.out.println("quad 4");
                            mSimulationView.setBackgroundResource(R.drawable.quad4);
                        }
                    }

                }


                public void startSimulation()
                {
            /*
             * It is not necessary to get accelerometer events at a very high
             * rate, by using a slower rate (SENSOR_DELAY_UI), we get an
             * automatic low-pass filter, which "extracts" the gravity component
             * of the acceleration. As an added benefit, we use less power and
             * CPU resources.
             */
                    mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
                }

                public void stopSimulation() {
                    mSensorManager.unregisterListener(this);
                }

                public SimulationView(Context context) {
                    super(context);
                    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    mXDpi = metrics.xdpi;
                    mYDpi = metrics.ydpi;
                    mMetersToPixelsX = mXDpi / 0.0254f;
                    mMetersToPixelsY = mYDpi / 0.0254f;

                    // rescale the ball so it's about 0.5 cm on screen
                    mDstWidth = (int) (sBallDiameter * mMetersToPixelsX + 3f);
                    mDstHeight = (int) (sBallDiameter * mMetersToPixelsY + 3f);
                    mParticleSystem = new ParticleSystem();

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inDither = true;
                    opts.inPreferredConfig = Bitmap.Config.RGB_565;
                }

                @Override
                protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                    // compute the origin of the screen relative to the origin of
                    // the bitmap CHANGED
                    mXOrigin = (w - mDstWidth) * 0.5f;
                    mYOrigin = (h - mDstHeight) * 0.5f;
                    mHorizontalBound = ((w / mMetersToPixelsX - sBallDiameter) * 0.25f);
                    mVerticalBound = ((h / mMetersToPixelsY - sBallDiameter) * 0.15f);
                }


                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                        return;
            /*
             * record the accelerometer data, the event's timestamp as well as
             * the current time. The latter is needed so we can calculate the
             * "present" time during rendering. In this application, we need to
             * take into account how the screen is rotated with respect to the
             * sensors (which always return data in a coordinate space aligned
             * to with the screen in its native orientation).
             */

                    switch (mDisplay.getRotation()) {
                        case Surface.ROTATION_0:
                            mSensorX = event.values[0];
                            mSensorY = event.values[1];
                            break;
                        case Surface.ROTATION_90:
                            mSensorX = -event.values[1];
                            mSensorY = event.values[0];
                            break;
                        case Surface.ROTATION_180:
                            mSensorX = -event.values[0];
                            mSensorY = -event.values[1];
                            break;
                        case Surface.ROTATION_270:
                            mSensorX = event.values[1];
                            mSensorY = -event.values[0];
                            break;
                    }
                }

                @Override
                protected void onDraw(Canvas canvas) {
            /*
             * Compute the new position of our object, based on accelerometer
             * data and present time.
             */
                    final ParticleSystem particleSystem = mParticleSystem;
                    final long now = System.currentTimeMillis();
                    final float sx = mSensorX;
                    final float sy = mSensorY;

                    particleSystem.update(sx, sy, now);
                    particleSystem.onRoll();
                    //This is what we are working on
                    //mPinIOActivity.whatWeNeed();

                    final float xc = mXOrigin;
                    final float yc = mYOrigin;
                    final float xs = mMetersToPixelsX;
                    final float ys = mMetersToPixelsY;
                    final int count = particleSystem.getParticleCount();
                    for (int i = 0; i < count; i++) {
                /*
                 * We transform the canvas so that the coordinate system matches
                 * the sensors coordinate system with the origin in the center
                 * of the screen and the unit is the meter.
                 */
                        final float x = xc + particleSystem.getPosX(i) * xs;
                        final float y = yc - particleSystem.getPosY(i) * ys;
                        particleSystem.mBalls[i].setTranslationX(x);
                        particleSystem.mBalls[i].setTranslationY(y);
                    }

                    // and make sure to redraw asap
                    invalidate();
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            }
        }


    }
