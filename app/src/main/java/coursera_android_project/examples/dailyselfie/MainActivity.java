package coursera_android_project.examples.dailyselfie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class MainActivity extends Activity {

    private String mCurrentPhotoPath, imageFileName, dataFileName;
    private SelfieListAdapter mAdapter;
    private AlarmManager mAlarmManager;
    private FragmentManager mFragmentManager;

    public static final String PREFERENCES_URLs = "URL_List_Shared_Preferences";
    public static final String PREFERENCES_NAMES = "NAMES_List_Shared_Preferences";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final long INTERVAL_TWO_MIN = 20 * 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getFragmentManager();

        mAdapter = new SelfieListAdapter(getApplicationContext(), mFragmentManager);

        ListView selfieListView = (ListView) findViewById(R.id.selfie_list);
        selfieListView.setAdapter(mAdapter);

        registerForContextMenu(selfieListView);

        SharedPreferences prefs1=getApplicationContext().getSharedPreferences(PREFERENCES_URLs,MODE_PRIVATE);
        SharedPreferences prefs2=getApplicationContext().getSharedPreferences(PREFERENCES_NAMES,MODE_PRIVATE);

        for(int i=0;;i++) {
            final String str1 = prefs1.getString(String.valueOf(i), "");
            final String str2 = prefs2.getString(String.valueOf(i), "");

            if (!str1.equals("") && !str2.equals("")) {
                Selfie newItem = new Selfie(str1, str2);
                mAdapter.add(newItem);
            } else
                break;
        }

 /*       new Thread() {
            @Override
            public void run() {

                Scanner sc = null;

                File storageDir = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                for (File f : storageDir.listFiles()) {
                    if(dataFileName == null)
                        break;
                    else if (f.getAbsolutePath() == dataFileName) {
                        try {
                            sc = new Scanner(f);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                while(sc != null && sc.hasNextLine()) {
                    String selfieName = sc.nextLine();
                    if (sc.hasNextLine()) {
                        String imagePath = sc.nextLine();
                        mAdapter.add(new Selfie(selfieName, imagePath));
                    }
                }

                if(sc != null)
                    sc.close();

                try {

                    // code runs in a thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (final Exception ex) {
                    Log.i("---", "Exception in thread");
                }
            }
        }.start();*/

/*        Scanner sc = null;

        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        for (File f : storageDir.listFiles()) {
            if(dataFileName == null)
                break;
            else if (f.getAbsolutePath() == dataFileName) {
                try {
                    sc = new Scanner(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        while(sc != null && sc.hasNextLine()) {
            String selfieName = sc.nextLine();
            if (sc.hasNextLine()) {
                String imagePath = sc.nextLine();
                mAdapter.add(new Selfie(selfieName, imagePath));
            }
        }

        if(sc != null)
            sc.close();*/////

        mAdapter.notifyDataSetChanged();

        selfieAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.start_camera) {
            dispatchTakePictureIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
  //      menu.setHeaderTitle("Select The Action");
 //       menu.add(0, v.getId(), 0, "Share");//groupId, itemId, order, title
  //      menu.add(0, v.getId(), 0, "delete");
    }

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
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            mAdapter.add(new Selfie(imageFileName, mCurrentPhotoPath));
        }
    }

    @Override
    protected void onPause(){
/*        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);

        File dataFile = null;

        for (File f : storageDir.listFiles()) {
            if (dataFileName == null)
                break;
            else if (f.getAbsolutePath() == dataFileName) {
                dataFile = f;
                dataFile.delete();
            }
        }

        String fileName = "Name_and_Url";

        try {
            dataFile = File.createTempFile(
                    fileName,  // prefix
                    ".txt",         // suffix
                    storageDir      // directory
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        dataFileName = dataFile.getAbsolutePath();

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(dataFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i<mAdapter.getCount(); i++){
            try {
                bw.write(((Selfie)mAdapter.getItem(i)).getSelfieName() + '\n');
                bw.write(((Selfie)mAdapter.getItem(i)).getImagePath() + '\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onPause();*/

        super.onPause();
        SharedPreferences prefs1=getApplicationContext().getSharedPreferences(PREFERENCES_URLs,MODE_PRIVATE);
        SharedPreferences.Editor editor1= prefs1.edit();
        editor1.clear();

        for(int i=0;i<mAdapter.getCount();i++){
            Selfie item=(Selfie) mAdapter.getItem(i);
            editor1.putString(String.valueOf(i),item.getImagePath());
        }

        editor1.commit();

        //this one is for the names

        SharedPreferences prefs2=getApplicationContext().getSharedPreferences(PREFERENCES_NAMES,MODE_PRIVATE);
        SharedPreferences.Editor editor2= prefs2.edit();
        editor2.clear();

        for(int i=0;i<mAdapter.getCount();i++){
            Selfie item=(Selfie) mAdapter.getItem(i);
            editor2.putString(String.valueOf(i),item.getImagePath());
        }

        editor2.commit();
    }

/*    private Bitmap getPicThumbnail(String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = 100;
        int targetH = 100;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }*/

    private void selfieAlarm(){
        Intent intent = new Intent(MainActivity.this, SelfieNotificationAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INTERVAL_TWO_MIN,
                INTERVAL_TWO_MIN,
                pendingIntent);
    }
}
