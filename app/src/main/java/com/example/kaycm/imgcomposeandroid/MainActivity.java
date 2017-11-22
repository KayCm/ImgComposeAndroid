package com.example.kaycm.imgcomposeandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.in;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    List<Map<String,Object>> iList = new ArrayList<Map<String,Object>>();


    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    Bitmap bmp = null;
                    Bundle ble = msg.getData();
                    bmp = (Bitmap) ble.get("bmp");
                    imageView.setImageBitmap(bmp);

                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("img",bmp);
                    map.put("type","img");
                    iList.add(map);

                    if (iList.size()==5){

                        System.out.println("------------------------------------------------->:"+iList.size());

                        Bitmap bm = createWaterMaskBitmap(10,10);

                        imageView.setImageBitmap(bm);
                    }

                    break;

                default:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);



        LoadImg();
//        iList.add()



    }


    void LoadImg(){


        for (int i =0;i<5;i++){

            final String url = "http://ww4.sinaimg.cn/bmiddle/9e58dccejw1e6xow22oc6j20c80gyaav.jpg";

            new Thread(){
                @Override
                public void run(){
                    try {

                        InputStream inputStream = HttpUtils.getImageViewInputStream(url);

                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        //imageView.setImageBitmap(bitmap);

                        Message msg = new Message();
                        Bundle bun = new Bundle();
                        bun.putParcelable("bmp", bitmap);
                        msg.what = 1;
                        msg.setData(bun);

                        uiHandler.sendMessage(msg);

                    }catch (Exception e){

                        System.out.println("Error:"+e);

                    }
                }
            }.start();

        }
    }

    Bitmap createWaterMaskBitmap(int paddingLeft, int paddingTop) {


        int width = 200;
        int height = 100;


        //创建一个bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);

        //在画布 0，0坐标上开始绘制原始图片
      //  canvas.drawBitmap(src, 0, 0, null);


        //开始绘制图片
        for (Map<String,Object> map : iList){

            String type = map.get("type").toString();

            if ("img".equals(type)){
                Bitmap bitmap = (Bitmap)map.get("img");
                canvas.drawBitmap(bitmap, 0, 0, null);
            }

        }

        //开始绘制文字
        for (Map<String,Object> map : iList){

            String type = map.get("type").toString();

//            iList.get(0)

            if ("img".equals(type)){

                String testString = "测试:ABC123def";
                Paint mPaint = new Paint();
                mPaint.setStrokeWidth(3);
                mPaint.setAntiAlias(true);
                mPaint.setTextSize(20);
                mPaint.setColor(Color.RED);
                mPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(testString, 50, 50,mPaint);
            }



        }



        // 保存
        canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        canvas.restore();

        return newb;
    }
}
