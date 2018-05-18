/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.samosys.paperai.activity.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.AspectRatioFragment;
import com.samosys.paperai.activity.utils.MarshMallowPermission;
import com.samosys.paperai.activity.utils.SquaredFrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;


/**
 * This demo app saves the taken picture to a constant file.
 * $ adb pull /sdcard/Android/data/com.google.android.cameraview.demo/files/Pictures/picture.jpg
 */
public class CameraPostActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        AspectRatioFragment.Listener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";
    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };
    MarshMallowPermission marshMallowPermission;
    private static final int[] FLASH_ICONS = {
            R.mipmap.home_camera,
            R.mipmap.home_camera,
            R.mipmap.home_camera,
    };
    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };
    AQuery aq;
    TextView txtNext;
    SquaredFrameLayout squareImage;
    File imageFile=new File("");
    int REQUEST_CAMERA = 0, SELECT_FILE = 1, SELECT_VIDEO = 3;
    ImageView capturedImage, imgback_camera;
    RelativeLayout rl_bottom_capture;
    private Uri picUri;
    private int mCurrentFlash;
    private ImageView take_picture, img_switchcam, img_gallary;
    private CameraView mCameraView;
    private Handler mBackgroundHandler;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        mCameraView.takePicture();
                        mCameraView.setVisibility(View.GONE);
                        rl_bottom_capture.setVisibility(View.GONE);
                        capturedImage.setVisibility(View.GONE);
                    }
                    break;

            }
        }
    };
    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {


            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "picture.jpg");

//                  Bitmap bit=  convertToBitmap(file);
//                    persistImage(bit,"picture.jpg");
                    // startCrop(Uri.fromFile(file));
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }


                    if (file.exists()) {
                        BitmapAjaxCallback cb = new BitmapAjaxCallback();
                        cb.targetWidth(500).rotate(true);
                        aq.id(capturedImage).image(new File(file.getAbsolutePath()), true, 500, cb);
                        //aq.id(imgCapturedimage).image(new File(listitem.get(position).getPost_image()), false, 500, cb);
                    } else {

                        if (file.exists()) {
                            aq.ajax(file.getAbsolutePath(), File.class, new AjaxCallback<File>() {
                                @Override
                                public void callback(String url, File bm, AjaxStatus status) {

                                    if (bm != null) {
//                                        mCameraView.setVisibility(View.GONE);
//                                        rl_bottom_capture.setVisibility(View.GONE);
//                                        squareImage.setVisibility(View.VISIBLE);
                                        BitmapAjaxCallback cb = new BitmapAjaxCallback();
                                        cb.targetWidth(1000).rotate(true);
                                        aq.id(capturedImage).image(bm, true, 500, cb);

                                    } else {
                                        capturedImage.setImageResource(R.mipmap.sign_up_project);
                                    }
                                }
                            });
                        } else {
                            capturedImage.setImageResource(R.mipmap.sign_up_project);
                        }
                    }
                    imageFile = file;
//                    Intent intent = new Intent(CameraPostActivity.this, RecordAudioActivity.class);
//                    intent.putExtra("file", file.toString());
//                    startActivity(intent);

//               Uri selectedImage= Uri.fromFile(file);
//
//
//
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(CameraPostActivity.this.getContentResolver(), selectedImage);
//
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        // Compress image to lower quality scale 1 - 100
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                        byte[] image = stream.toByteArray();
//
//
//                        // Create the ParseFile
//
//                        ParseFile parseFile = new ParseFile("postfile.jpg", image);
//                        createPostImage(parseFile);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                }
            });
        }

    };



    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        AppConstants.getTranparentstatusbar(CameraPostActivity.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        aq = new AQuery(CameraPostActivity.this);
        marshMallowPermission=new MarshMallowPermission(CameraPostActivity.this);
        capturedImage = (ImageView) findViewById(R.id.capturedImage);
        mCameraView = (CameraView) findViewById(R.id.camera);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        txtNext = (TextView) findViewById(R.id.txtNext);
        rl_bottom_capture = (RelativeLayout) findViewById(R.id.rl_bottom_capture);
        // squareImage = (SquaredFrameLayout) findViewById(R.id.squareImage);
        take_picture = (ImageView) findViewById(R.id.take_picture);
        img_switchcam = (ImageView) findViewById(R.id.img_switchcam);
        imgback_camera = (ImageView) findViewById(R.id.imgback_camera);
        img_gallary = (ImageView) findViewById(R.id.img_gallary);
        if (take_picture != null) {
            take_picture.setOnClickListener(mOnClickListener);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        img_switchcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
            }
        });

        img_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askForCameraPermission();
            }
        });
        take_picture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(CameraPostActivity.this, "lonmg", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        imgback_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraView.getVisibility() == View.VISIBLE) {
                    onBackPressed();
                } else {
                    mCameraView.setVisibility(View.VISIBLE);
                    capturedImage.setVisibility(View.GONE);

                    rl_bottom_capture.setVisibility(View.VISIBLE);

                }
            }
        });

        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("imageFile", "imageFile==" + imageFile);
                if (imageFile.exists()){
                Intent intent = new Intent(CameraPostActivity.this, RecordAudioActivity.class);
                intent.putExtra("file", imageFile.toString());
                startActivity(intent);}
                else {
                    Toast.makeText(CameraPostActivity.this, "Please capture photo", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void askForCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            } else {
                //  showDialog();
                opengallary();
            }
        } else {
            //  showDialog();
            opengallary();
        }
    }

    private void opengallary() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                // This method is call for getting image from gallary
                onSelectFromGalleryResult(data);
                //    getdata(data);
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {


        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(selectedImage, filePath,
                null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String selectedImagePath = c.getString(columnIndex);
        c.close();


        Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
        Log.e("MEW_BITMAP12", "" + selectedImagePath);


            File file=new File(selectedImagePath);
            mCameraView.setVisibility(View.GONE);
            rl_bottom_capture.setVisibility(View.GONE);
            capturedImage.setVisibility(View.GONE);
        if (file.exists()) {
            BitmapAjaxCallback cb = new BitmapAjaxCallback();
            cb.targetWidth(500).rotate(true);
            aq.id(capturedImage).image(new File(file.getAbsolutePath()), true, 500, cb);
            //aq.id(imgCapturedimage).image(new File(listitem.get(position).getPost_image()), false, 500, cb);
        } else {

            if (file.exists()) {
                aq.ajax(file.getAbsolutePath(), File.class, new AjaxCallback<File>() {
                    @Override
                    public void callback(String url, File bm, AjaxStatus status) {

                        if (bm != null) {
//                                        mCameraView.setVisibility(View.GONE);
//                                        rl_bottom_capture.setVisibility(View.GONE);
//                                        squareImage.setVisibility(View.VISIBLE);
                            BitmapAjaxCallback cb = new BitmapAjaxCallback();
                            cb.targetWidth(1000).rotate(true);
                            aq.id(capturedImage).image(bm, true, 500, cb);

                        } else {
                            capturedImage.setImageResource(R.mipmap.sign_up_project);
                        }
                    }
                });
            } else {
                capturedImage.setImageResource(R.mipmap.sign_up_project);
            }
        }
        imageFile = file;


//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(CameraPostActivity.this.getContentResolver(), selectedImage);
//
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            // Compress image to lower quality scale 1 - 100
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            byte[] image = stream.toByteArray();
//
//
//            // Create the ParseFile
//
//            ParseFile parseFile = new ParseFile("postfile.jpg", image);
//            createPostImage(parseFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void createPostImage(ParseFile parseFile) {

        final ProgressDialog dialog = AppConstants.showProgressDialog(CameraPostActivity.this, "Please wait...");

        ParseObject gameScore = new ParseObject("Post");
        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
//
        gameScore.put("text", "android post test");
        gameScore.put("worksapce", ParseObject.createWithoutData("WorkSpace", AppConstants.loadPreferences(CameraPostActivity.this, "workid")));

        gameScore.put("postImage", parseFile);

        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (e == null) {
                    Toast.makeText(CameraPostActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CameraPostActivity.this, HomeFeedActivity.class);
                    fileList();
                    startActivity(intent);

                } else {

                    Toast.makeText(CameraPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                // No need to start camera here; it is handled by onResume
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aspect_ratio:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (mCameraView != null
                        && fragmentManager.findFragmentByTag(FRAGMENT_DIALOG) == null) {
                    final Set<AspectRatio> ratios = mCameraView.getSupportedAspectRatios();
                    final AspectRatio currentRatio = mCameraView.getAspectRatio();
                    AspectRatioFragment.newInstance(ratios, currentRatio)
                            .show(fragmentManager, FRAGMENT_DIALOG);
                }
                return true;
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraView != null) {
            Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show();
            mCameraView.setAspectRatio(ratio);
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                                                             String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }

    }

}
