package com.example.runningtracker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.runningtracker.databinding.ActivityImagePickerBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Activity which allows user to capture or select image for each run
 */
public class ImagePickerActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> galleryActivity;
    private ActivityResultLauncher<Intent> cameraActivity;

    private DatabaseViewModel viewModel;
    private ActivityImagePickerBinding binding;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_image_picker);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_picker);
        binding.galleryPick.setOnClickListener(this::openGallery);
        binding.cameraPick.setOnClickListener(this::openCamera);

        Intent intent = getIntent();
        id = intent.getIntExtra("item", -1);

        ImageView imageView = findViewById(R.id.imageView);

        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(DatabaseViewModel.class);

        //Get any existing image in the database
        viewModel.getRunById(id).observe(this, run -> {
            byte[] imageByteArray = run.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            imageView.setImageBitmap(bitmap);

        });


        //Set the image selected from gallery to view and update database
        galleryActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri imagePicked = data.getData();
                        InputStream inStream = null;
                        try {
                            inStream = getContentResolver().openInputStream(imagePicked);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                        imageView.setImageBitmap(bitmap);
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        byte[] imageByteArray = outStream.toByteArray();

                        viewModel.updateImage(id, imageByteArray);


                    }
                    else if(result.getResultCode() == Activity.RESULT_CANCELED)
                        Log.d("ERROR" , "result cancelled");
                });
        //Set the image captured from camera to view and update database
        cameraActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);


                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        byte[] imageByteArray = outStream.toByteArray();

                        viewModel.updateImage(id, imageByteArray);

                    }
                    else if(result.getResultCode() == Activity.RESULT_CANCELED)
                        Log.d("ERROR" , "result cancelled");
                });
    }

    /**
     * Method which sends an intent to select images from gallery
     */
    public void openGallery(View v){
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        if (galleryIntent.resolveActivity(getPackageManager()) != null)
            galleryActivity.launch(Intent.createChooser(galleryIntent, "Pick An Image For This Run"));    }

    /**
     * Method which sends an intent to capture an image via MediaStore
     */
    public void openCamera(View v){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null)
            cameraActivity.launch(cameraIntent);
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "No Camera Support Found", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}