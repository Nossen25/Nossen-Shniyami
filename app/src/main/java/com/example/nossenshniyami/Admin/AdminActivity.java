package com.example.nossenshniyami.Admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.nossenshniyami.MainActivity;
import com.example.nossenshniyami.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;

public class AdminActivity extends AppCompatActivity {
private Button btnAddBusiness;
Button btnAddPhoto;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 250206;
 private ImageView imageView;

    ActivityResultLauncher<Intent> activityResultLauncher;

    private static Bitmap photo;

    private static  final int CAMERA_REQUEST_CODE=250;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

private TextView textesek;

private EditText BusinessName,BusinessAddress,PhoneNumber,BInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askAboutCamera();
//
//
        setContentView(R.layout.activity_admin);
//


//
        BInfo=findViewById(R.id.BInfo);
        btnAddBusiness=findViewById(R.id.btnAddBusiness);
        btnAddPhoto=findViewById(R.id.btnAddPhoto);
        imageView=findViewById(R.id.imgview);
        textesek=findViewById(R.id.textesek);
        BusinessName=findViewById(R.id.BusinessName);
        BusinessAddress=findViewById(R.id.BusinessAddress);
        PhoneNumber=findViewById(R.id.PhoneNumber);

//
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {






                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                CameraResultLauncher.launch(cameraIntent);
//                saveImage(photo);
//                dispatchTakePictureIntent();
//                galleryAddPic();
//                setPic();



            }
        });



//




        btnAddBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BusinessName.getText().toString().equals("")) {
                    Toast.makeText(AdminActivity.this, "d", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseStorage storage = FirebaseStorage.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] data = outputStream.toByteArray();

                String path = "images/" + UUID.randomUUID() + ".png";
                StorageReference imagesRef = storage.getReference(path);

                UploadTask uploadTask = imagesRef.putBytes(data);

                Map<String, Object> business = new HashMap<>();
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                business.put("Image", uri.toString());
                                business.put("BusinessName", BusinessName.getText().toString().trim());
                                business.put("BusinessAddress", BusinessAddress.getText().toString().trim());
                                business.put("PhoneNumber", PhoneNumber.getText().toString().trim());
                                business.put("Basic Info", BInfo.getText().toString().trim());

                                db.collection("Business")
                                        .add(business)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });

                                Toast.makeText(AdminActivity.this, "Business Added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
            }
        });

//        user.put("BusinessName", BusinessName.getText().toString().trim());
//        user.put("BusinessAddress", BusinessAddress.getText().toString().trim());
//        user.put("PhoneNumber", PhoneNumber.getText().toString().trim());
////        user.put("Image",R.drawable.businesspic1);
//
//// Add a new document with a generated ID
//        db.collection("Business")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//        Toast.makeText(AdminActivity.this, " Business Added", Toast.LENGTH_SHORT).show();
//
//        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
//        startActivity(intent);
//    }
//});


    }
//




    ActivityResultLauncher<Intent> CameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        photo = (Bitmap) result.getData().getExtras().get("data");

                        // Calculate the width based on the aspect ratio of the image
//                        int width = (int) ((float) photo.getWidth() / ((float) photo.getHeight() / (float) imageView.getHeight()));
//
//                        // Resize the image bitmap with the calculated width
//                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(photo, width, imageView.getHeight(), true);
//
//                        // Set the resized bitmap to the ImageView
                        imageView.setImageBitmap(photo);
//                        imageView.setTag("Pic");
                    }
                }
            });

    private void askAboutCamera(){

        EasyPermissions.requestPermissions(this, "A partir deste ponto a permissão de câmera é necessária.", CAMERA_REQUEST_CODE, Manifest.permission.CAMERA );

    }


    private void saveImage(Bitmap imageBitmap) {
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            try {
                FileOutputStream fos = new FileOutputStream(photoFile);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




// התשובות שקיבלתי הן reqcode = 1  result code = -1 data = null צריך רק להבין למה ולתקן וזה יעבוד
 //   @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
//        {
//            if (data != null && data.getExtras() != null )//פה הבעיה כי האלס נפתח גם הדאטא וגם הגט אקסטרס שווה לnull
//            {
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                if (imageBitmap != null)
//                {
//                    imageView.setImageBitmap(imageBitmap);
//                    setPic();
//                }
//                else {
//                    // Handle case where imageBitmap is null
//                    // Perhaps show a message or perform some other action
//                    Toast.makeText(this, "nullphoto", Toast.LENGTH_SHORT).show();
//                }
//            }
//            else {
//                // Handle case where data or extras are null
//                // Perhaps show a message or perform some other action
//                Toast.makeText(this, "lo yodea", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

  //  @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            setPic();
//        }
//    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    //זה מה שצאט הציע
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CAMERA: {
//                // If request is cancelled, the result arrays are empty
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    // Permissions are granted, proceed with the camera operation
//                    dispatchTakePictureIntent();
//                } else {
//                    // Permissions are denied, handle accordingly (e.g., show a message or disable camera functionality)
//                    Toast.makeText(this, "Camera and storage permissions are required to take a picture.", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//        }
//    }
private boolean isValidAddress(String address) {
    // Perform comprehensive validation checks

    // Check if the address is not empty
    if (address.isEmpty()) {
        return false;
    }

    // Check if the address contains necessary components (e.g., street, city, state)
    if (!address.contains(",")) {
        return false;
    }

    // Check if the address contains numeric characters (assumes there should be at least one numeric character in the address)
    if (!address.matches(".*\\d.*")) {
        return false;
    }

    // Check if the address contains Hebrew characters
    if (!address.matches(".*[א-ת].*")) {
        return false;
    }

    // Additional checks can be added here based on your validation requirements

    // If all checks pass, return true to indicate a valid address
    return true;
}
}

