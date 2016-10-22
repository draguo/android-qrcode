package com.example.qrcode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@SuppressLint("SdCardPath")
public class MainActivity extends Activity {

	/** ���ɶ�ά��ͼƬ��С */
	private static final int QRCODE_SIZE = 900;
	Button bt1,bt2,bt3;
	EditText et1;
	ImageView iv1;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bt1=(Button)findViewById(R.id.button1);
		bt2=(Button)findViewById(R.id.button2);
		bt3=(Button)findViewById(R.id.button3);
		et1=(EditText)findViewById(R.id.editText1);
		iv1=(ImageView)findViewById(R.id.image1);
		//���ɶ�ά�밴ť
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// ��ȡֵ
				String pdata = et1.getText().toString();
				if(!pdata.equals("")){
				iv1.setImageBitmap(createQRCodeBitmap(pdata));
				//��Ļ�·���ʾ֪ͨ
				Toast.makeText(getApplicationContext(), "�ɹ�����",
				Toast.LENGTH_SHORT).show();
				}else{
				Toast.makeText(getApplicationContext(), "����Ϊ��Ӵ",
				Toast.LENGTH_SHORT).show();
				}
			}
		});
		//����ͼƬ
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//����ʹ�������ɣ�����������
				String pdata = et1.getText().toString();
				if(!pdata.equals("")){
				Bitmap bitmap_to = createQRCodeBitmap(pdata);
				saveBitmapFile(bitmap_to);
				}else{
					//Ƶ�����ã�Ӧ��д�ɺ������÷�ʽ
					Toast.makeText(getApplicationContext(), "����Ϊ��Ӵ",
					Toast.LENGTH_SHORT).show();
				}
			}
		});
		//�˳�����
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				System.exit(0);
			}
		});
	}

//����
	public void saveBitmapFile(Bitmap bitmap){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.US);
		String  fname = "/sdcard/"+sdf.format(new Date())+".jpg";
        File file=new File(fname);
        try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                Toast.makeText(getApplicationContext(), "����ɹ�",
        		Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
                e.printStackTrace();
        }
		}else{
			Toast.makeText(getApplicationContext(), "����ڴ�����е����Ⱑ",
			Toast.LENGTH_SHORT).show();
		}
}	
	/**
	 * ����QR��ά��ͼƬ,����string ����bitmap
	 */
	private Bitmap createQRCodeBitmap(String content) {
		// ��������QR��ά�����
		Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
		// ����QR��ά��ľ����𡪡�����ѡ�����H����
		qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// ���ñ��뷽ʽ
		qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// ����˳��ֱ�Ϊ���������ݣ��������ͣ�����ͼƬ��ȣ�����ͼƬ�߶ȣ����ò���
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, qrParam);

			// ��ʼ���ö�ά�����ݴ���BitmapͼƬ���ֱ���Ϊ�ڰ���ɫ
			int w = bitMatrix.getWidth();
			int h = bitMatrix.getHeight();
			int[] data = new int[w * h];

			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					if (bitMatrix.get(x, y))
						data[y * w + x] = 0xff000000;// ��ɫ
					else
						data[y * w + x] = -1;// ��ɫ
				}
			}
			// ����һ��bitmapͼƬ��������ߵ�ͼƬЧ��ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			// ������Ķ�ά����ɫ���鴫�룬����ͼƬ��ɫ
			bitmap.setPixels(data, 0, w, 0, 0, w, h);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}
}
