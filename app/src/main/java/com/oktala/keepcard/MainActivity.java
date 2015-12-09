package com.oktala.keepcard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView mPhotoCapturedImageView;
    private String mImageFileLocation = "";
    private String GALLERY_LOCATION = "Cards";
    private File mGalleryFolder;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createImageGallery();

        mRecyclerView = (RecyclerView) findViewById(R.id.galleryRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter imageAdapter = new ImageAdapter(mGalleryFolder);
        mRecyclerView.setAdapter(imageAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
                Intent callCameraAppIntent = new Intent();
                callCameraAppIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                File photoFile = null;
                try {
                    photoFile = createImageFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                callCameraAppIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(callCameraAppIntent, ACTIVITY_START_CAMERA_APP);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode==RESULT_OK){
            //Toast.makeText(this, "Foto OK!", Toast.LENGTH_SHORT).show();
            //Bundle extras = data.getExtras();
            //Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
            //mPhotoCapturedImageView.setImageBitmap(photoCapturedBitmap);

            //Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            //mPhotoCapturedImageView.setImageBitmap(photoCapturedBitmap);
            //setReducedImageSize();
            RecyclerView.Adapter newImageAdapter = new ImageAdapter(mGalleryFolder);
            mRecyclerView.swapAdapter(newImageAdapter, false);
        }
    }
    private void createImageGallery(){
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mGalleryFolder = new File(storageDirectory, GALLERY_LOCATION);
        if(!mGalleryFolder.exists()){
            mGalleryFolder.mkdirs();
        }
    }
    File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "CARD_"+timeStamp+"_";


        File image = File.createTempFile(imageFileName,".jpg", mGalleryFolder);
        mImageFileLocation = image.getAbsolutePath();

        return image;
    }
    void setReducedImageSize(){
        int targetImageViewWidth = mPhotoCapturedImageView.getWidth();
        int targetImageViewHeight = mPhotoCapturedImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        mPhotoCapturedImageView.setImageBitmap(photoReducedSizeBitmap);
    }







    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
