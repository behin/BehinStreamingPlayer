package edu.sharif.behin.androidstreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import edu.sharif.behin.androidstreamer.multimedia.AudioDecoder;
import edu.sharif.behin.androidstreamer.multimedia.AudioEncoder;
import edu.sharif.behin.androidstreamer.multimedia.AudioPreview;
import edu.sharif.behin.androidstreamer.multimedia.CameraPreview;
import edu.sharif.behin.androidstreamer.multimedia.FrameHandler;
import edu.sharif.behin.androidstreamer.multimedia.VideoDecoder;
import edu.sharif.behin.androidstreamer.multimedia.VideoEncoder;
import edu.sharif.behin.androidstreamer.network.LocalLoopBackStream;


public class LocalLoopBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_loop_back);
        setTitle("Local Loop Back Demo");

        final CameraPreview cameraPreview = (CameraPreview) findViewById(R.id.camera);
        final AudioPreview audioPreview = (AudioPreview) findViewById(R.id.mic);

        final SurfaceView view = (SurfaceView) findViewById(R.id.decodedView);

        ImageButton swap = (ImageButton) findViewById(R.id.button);
        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPreview.swapCamera();
            }
        });

        final LocalLoopBackStream loopBackStream= new LocalLoopBackStream();
        FrameHandler senderHandler = new FrameHandler(loopBackStream.getOutputStream());
        final FrameHandler receiverHandler = new FrameHandler(loopBackStream.getInputStream());

        try {

            VideoEncoder encoder = new VideoEncoder(cameraPreview,senderHandler);
            encoder.start();

            view.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    VideoDecoder decoder = new VideoDecoder(receiverHandler,view.getHolder().getSurface());
                    decoder.start();
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            AudioEncoder encoder = new AudioEncoder(audioPreview,senderHandler);
            encoder.start();

            AudioDecoder decoder = new AudioDecoder(receiverHandler);
            decoder.start();

        }catch (Exception e){
            e.printStackTrace();
        }





    }

}
