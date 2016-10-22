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

	/** 生成二维码图片大小 */
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
		//生成二维码按钮
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// 获取值
				String pdata = et1.getText().toString();
				if(!pdata.equals("")){
				iv1.setImageBitmap(createQRCodeBitmap(pdata));
				//屏幕下方显示通知
				Toast.makeText(getApplicationContext(), "成功生成",
				Toast.LENGTH_SHORT).show();
				}else{
				Toast.makeText(getApplicationContext(), "不能为空哟",
				Toast.LENGTH_SHORT).show();
				}
			}
		});
		//保存图片
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//重新使用了生成，这样有冗余
				String pdata = et1.getText().toString();
				if(!pdata.equals("")){
				Bitmap bitmap_to = createQRCodeBitmap(pdata);
				saveBitmapFile(bitmap_to);
				}else{
					//频繁调用，应该写成函数调用方式
					Toast.makeText(getApplicationContext(), "不能为空哟",
					Toast.LENGTH_SHORT).show();
				}
			}
		});
		//退出程序
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				System.exit(0);
			}
		});
	}

//保存
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
                Toast.makeText(getApplicationContext(), "保存成功",
        		Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
                e.printStackTrace();
        }
		}else{
			Toast.makeText(getApplicationContext(), "你的内存好像有点问题啊",
			Toast.LENGTH_SHORT).show();
		}
}	
	/**
	 * 创建QR二维码图片,传入string 返回bitmap
	 */
	private Bitmap createQRCodeBitmap(String content) {
		// 用于设置QR二维码参数
		Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别——这里选择最高H级别
		qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 设置编码方式
		qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, qrParam);

			// 开始利用二维码数据创建Bitmap图片，分别设为黑白两色
			int w = bitMatrix.getWidth();
			int h = bitMatrix.getHeight();
			int[] data = new int[w * h];

			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					if (bitMatrix.get(x, y))
						data[y * w + x] = 0xff000000;// 黑色
					else
						data[y * w + x] = -1;// 白色
				}
			}
			// 创建一张bitmap图片，采用最高的图片效果ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			// 将上面的二维码颜色数组传入，生成图片颜色
			bitmap.setPixels(data, 0, w, 0, 0, w, h);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}
}
