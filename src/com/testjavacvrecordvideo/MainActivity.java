package com.testjavacvrecordvideo;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class MainActivity extends Activity {

	private Button btnRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRecord = (Button) findViewById(R.id.btn_record);
		btnRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				record();
			}
		});

	}

	private void record() {

		Log.d("Record", "Environment.getExternalStorageDirectory().getPath()  : " + Environment.getExternalStorageDirectory().getPath());
		String path = Environment.getExternalStorageDirectory().getPath() + "/Video_images"; // You
																								// can
																								// provide
																								// SD
																								// Card
																								// path
																								// here.

		File folder = new File(path);

		File[] listOfFiles = folder.listFiles();
		IplImage[] iplimage = null;
		if (listOfFiles.length > 0) {

			iplimage = new opencv_core.IplImage[listOfFiles.length];

			for (int j = 0; j < listOfFiles.length; j++) {

				String files = "";

				if (listOfFiles[j].isFile()) {
					files = listOfFiles[j].getName();
					System.out.println(" j " + j + listOfFiles[j]);
				}

				String[] tokens = files.split("\\.(?=[^\\.]+$)");
				String name = tokens[0];

				Toast.makeText(getBaseContext(), "size" + listOfFiles.length, Toast.LENGTH_SHORT).show();

				iplimage[j] = cvLoadImage(Environment.getExternalStorageDirectory().getPath() + "/Video_images/" + name + ".jpg");

			}

		}

		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(Environment.getExternalStorageDirectory().getPath() + "/Video_images/output" + System.currentTimeMillis() + ".mp4", 200, 150);

		try {
			recorder.setVideoCodec(13); // CODEC_ID_MPEG4 //CODEC_ID_MPEG1VIDEO
										// //http://stackoverflow.com/questions/14125758/javacv-ffmpegframerecorder-properties-explanation-needed

			recorder.setFrameRate(1); // This is the frame rate for video. If
										// you really want to have good video
										// quality you need to provide large set
										// of images.
			recorder.setPixelFormat(0); // PIX_FMT_YUV420P

			recorder.start();

			for (int i = 0; i < iplimage.length; i++) {

				recorder.record(iplimage[i]);

			}
			recorder.stop();

			Toast.makeText(MainActivity.this, "Record Completed", Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
