package com.xiaoYue.facedetection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class ImageviewActivity extends Activity {
	
	private String fileuri;
    private ImageView priviewimage;
    private Uri filepath;
    private String falg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        priviewimage=(ImageView)findViewById(R.id.previewimageView);
        Intent datagetintent = getIntent();
        fileuri = datagetintent.getStringExtra("serchresult");
        falg = datagetintent.getStringExtra("changeflag");
        
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        final Bitmap bitmap = BitmapFactory.decodeFile(fileuri,
                options);

        Matrix matrix = new Matrix();
        if(falg.equals("1"))
        { matrix.postRotate(-90);}
        else if(falg.equals("0"))
        {matrix.postRotate(90);}
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap .getHeight(), matrix, true);
        priviewimage.setImageBitmap(rotatedBitmap);
        String pictureFilename=fileuri+".jpg";
        File pictureFile=new File(fileuri);
        if(pictureFile.exists()){
            pictureFile.delete();
            pictureFile=new File(fileuri);
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }


        ImageButton registerstartButton = (ImageButton) findViewById(R.id.faceimageButton);
        registerstartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ImageviewActivity.this, HelloFacebookSampleActivity.class);
                intent.putExtra("serchresult",fileuri);

                startActivity(intent);

            }
        });
        
        
        ImageButton bacekButton = (ImageButton) findViewById(R.id.backimageButton);
        bacekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ImageviewActivity.this, MainActivity.class);
                intent.putExtra("serchresult",fileuri);

                startActivity(intent);

            }
        });
    }

}
