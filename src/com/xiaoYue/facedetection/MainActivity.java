package com.xiaoYue.facedetection;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.hardware.Camera.FaceDetectionListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

	public static final String TAG = MainActivity.class.getSimpleName();
    private boolean isStartflag=true;
    private Camera mCamera;
    public String shareuri=null;
    LayoutInflater controlInflater = null;
    // We need the phone orientation to correctly draw the overlay:
    private int mOrientation;
    private int mOrientationCompensation;
    private OrientationEventListener mOrientationEventListener;
    public Uri uriTarget;
    // Let's keep track of the display rotation and orientation also:
    private int mDisplayRotation;
    private int mDisplayOrientation;
    private ImageButton betweenbtn; 
    // Holds the Face Detection result:
    private Camera.Face[] mFaces;
    private String changeflag;

    // The surface view for the camera data
    private SurfaceView mView;

    // Draw rectangles and other fancy stuff:
    private FaceOverlayView mFaceView;
    public boolean startflag=true;
    private MediaPlayer mp;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private FaceDetectionListener faceDetectionListener = new FaceDetectionListener() {
        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            Log.d("onFaceDetection", "Number of Faces:" + faces.length);
            // Update the view now!
            mFaceView.setFaces(faces);
            if (faces.length == 1&&isStartflag){
                 catuerimage();
                 shootSound();
            }



        }
    };
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

            mCamera.takePicture(myShutterCallback,
                    myPictureCallback_RAW, myPictureCallback_JPG);


            timer.cancel();




        }
    };
    Timer timer = new Timer();
public void catuerimage(){

    isStartflag=false;
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
    alertDialog.setTitle("messgae");
    alertDialog.setMessage("Are you sure you want social site?");
    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int which) {
        	
            timer.schedule(timerTask, 5000, 5000);
            dialog.cancel();
            // Write your code here to invoke YES event

        }
    });

    // Setting Negative "NO" Button
    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            // Write your code here to invoke NO event

            isStartflag=true;
            dialog.cancel();
        }
    });
    alertDialog.show();

    //timer.cancel();
}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new SurfaceView(this);

        setContentView(mView);
        // Now create the OverlayView:
        mFaceView = new FaceOverlayView(this);
        addContentView(mFaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // Create and Start the OrientationListener:
        mOrientationEventListener = new SimpleOrientationEventListener(this);
        mOrientationEventListener.enable();
        //catuerimage();

        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.background, null);
        LayoutParams layoutParamsControl
         = new LayoutParams(LayoutParams.FILL_PARENT,
         LayoutParams.FILL_PARENT);
        this.addContentView(viewControl, layoutParamsControl);
        betweenbtn = (ImageButton)findViewById(R.id.takepicture);
        betweenbtn.setOnClickListener(new ImageButton.OnClickListener(){

    @Override
    public void onClick(View arg0) {
     // TODO Auto-generated method stub
    	if(camId==Camera.CameraInfo.CAMERA_FACING_BACK)
    	{
    		
    		mCamera.stopPreview();
            mCamera.release();
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
           

            mCamera.setDisplayOrientation(90);
            SurfaceHolder holder = mView.getHolder();
            try {
    			mCamera.setPreviewDisplay(holder);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}               
            mCamera.setFaceDetectionListener(faceDetectionListener);
            mCamera.startFaceDetection();
            mCamera.startPreview();
            camId=Camera.CameraInfo.CAMERA_FACING_FRONT;
    	}
    	else
    	{
        mCamera.stopPreview();
        mCamera.release();
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
       

        mCamera.setDisplayOrientation(90);
        SurfaceHolder holder = mView.getHolder();
        try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}               
        mCamera.setFaceDetectionListener(faceDetectionListener);
        mCamera.startFaceDetection();
        mCamera.startPreview();
        camId=Camera.CameraInfo.CAMERA_FACING_BACK;
    	}

     
    }});
        LinearLayout layoutBackground = (LinearLayout)findViewById(R.id.background);
        layoutBackground.setOnClickListener(new LinearLayout.OnClickListener(){

    @Override
    public void onClick(View arg0) {
     // TODO Auto-generated method stub

     
    	mCamera.autoFocus(myAutoFocusCallback);
    	mCamera.takePicture(myShutterCallback,
                myPictureCallback_RAW, myPictureCallback_JPG);
    }});

        // Showing Alert Message

    }
    public void shootSound()
    {
        AudioManager meng = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

        if (volume != 0)
        {
            if (mp== null)
                mp = MediaPlayer.create(getBaseContext(), Uri.parse("file:///system/media/audio/ui/Auto_focus.ogg"));
            if (mp != null)
                mp.start();
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SurfaceHolder holder = mView.getHolder();
        holder.addCallback(this);
    }

    @Override
    protected void onPause() {
        mOrientationEventListener.disable();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mOrientationEventListener.enable();
        super.onResume();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mCamera = Camera.open(camId);
        mCamera.setFaceDetectionListener(faceDetectionListener);
        mCamera.startFaceDetection();
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (Exception e) {
            Log.e(TAG, "Could not preview the image.", e);
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        // We have no surface, return immediately:
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        // Try to stop the current preview:
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore...
        }
        // Get the supported preview sizes:
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);
        // And set them:
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setParameters(parameters);
        // Now set the display orientation for the camera. Can we do this differently?
        mDisplayRotation = Util.getDisplayRotation(MainActivity.this);
        mDisplayOrientation = Util.getDisplayOrientation(mDisplayRotation, 0);
        mCamera.setDisplayOrientation(mDisplayOrientation);

        if (mFaceView != null) {
            mFaceView.setDisplayOrientation(mDisplayOrientation);
        }

        // Finally start the camera preview again:
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        mCamera.setFaceDetectionListener(null);
        mCamera.stopFaceDetection();
        mCamera.stopPreview();
        timer.cancel();
        mCamera.release();



    }


    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {


        }};

    ShutterCallback myShutterCallback = new ShutterCallback(){

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }};

   PictureCallback myPictureCallback_RAW = new PictureCallback(){

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub

        }};
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    PictureCallback myPictureCallback_JPG = new PictureCallback(){

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
   /*Bitmap bitmapPicture
    = BitmapFactory.decodeByteArray(arg0, 0, arg0.length); */

            File mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_NAME);

//             uriTarget = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
             shareuri = mediaFile.getPath();
            Log.d("show",shareuri);
            OutputStream imageFileOS;
            try {
//                imageFileOS = getContentResolver().openOutputStream(uriTarget);
                imageFileOS = new FileOutputStream(mediaFile);
                imageFileOS.write(arg0);
                imageFileOS.flush();
                imageFileOS.close();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Intent intent;
            intent = new Intent(MainActivity.this, ImageviewActivity.class);
            String flag=Integer.toString(camId);
            intent.putExtra("serchresult",shareuri);
            intent.putExtra("changeflag",flag);
            
            Log.d("show",flag);
            startActivity(intent);

            finish();

//            mCamera.startPreview();

        }};



    /**
     * We need to react on OrientationEvents to rotate the screen and
     * update the views.
     */
    private class SimpleOrientationEventListener extends OrientationEventListener {

        public SimpleOrientationEventListener(Context context) {
            super(context, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onOrientationChanged(int orientation) {

            if (orientation == ORIENTATION_UNKNOWN) return;
            mOrientation = Util.roundOrientation(orientation, mOrientation);

            int orientationCompensation = mOrientation
                    + Util.getDisplayRotation(MainActivity.this);
            if (mOrientationCompensation != orientationCompensation) {
                mOrientationCompensation = orientationCompensation;
                mFaceView.setOrientation(mOrientationCompensation);
            }
        }
    }
}
