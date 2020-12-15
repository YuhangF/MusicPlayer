package com.hq.hqmusic.Utils;


import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hq.hqmusic.R;
import com.hq.hqmusic.UI.MainActivity;
import com.hq.hqmusic.Utils.DownloadService;

public class mainactivityofdownload extends AppCompatActivity implements View.OnClickListener{
    private DownloadService.DownloadBinder downloadBinder;
    EditText text;

    private ServiceConnection connection = new ServiceConnection () {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( com.hq.hqmusic.R.layout.activity_main2 );
        Button startDownload = (Button) findViewById (  R.id.start_download );
        Button pauseDownload = (Button) findViewById ( R.id.pause_download);
        Button cancelDownload = (Button) findViewById ( R.id.cancel_download );
        text=(EditText) findViewById(R.id.editurl);
        startDownload.setOnClickListener ( this );
        pauseDownload.setOnClickListener (this );
        cancelDownload.setOnClickListener ( this );
        Intent intent = new Intent ( this,DownloadService.class );
        startService ( intent );//启动服务
        bindService ( intent,connection,BIND_ABOVE_CLIENT );
        if(ContextCompat.checkSelfPermission ( mainactivityofdownload.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions ( mainactivityofdownload.this,new
                    String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);

        }


    }
    @Override
    public  void onClick(View v){
        if (downloadBinder==null){
            return;
        }
        switch (v.getId ()){
            case R.id.start_download:
//                String url ="https://freemusicarchive.org/track/driving-home/download";
                String url=text.getText().toString();
               if(url.getBytes().length==0) {
                  url = "https://files.freemusicarchive.org/storage-freemusicarchive-org/tracks/C8ADQUFFdQX75kEXsoNfx0PL1SYKR5S950Y0Pe1F.mp3";
                }
                downloadBinder.startDownload ( url);
                break;
            case R.id.pause_download:
                downloadBinder.pauseDownload ();
                break;
            case R.id.cancel_download:
                downloadBinder.cancelDownload ();
                break;
            default:
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int
            [] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0&& grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText ( this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT ).show ();
                    finish ();
                }
                break;
            default:
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy ();
        unbindService ( connection );
    }
}
