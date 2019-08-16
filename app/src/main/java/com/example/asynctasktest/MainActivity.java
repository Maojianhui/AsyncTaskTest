package com.example.asynctasktest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
1.AsyncTask的类必须在主线程中加载，这意味着第一次访问AsyncTask必须放生在主线程
2.AsyncTask的对象必须在主线程中创建
3.execute()方法必须在ui线程中执行
4.不要在程序中直接调用onPreExecute()、onPostExecute()、onProgressUpdate()、onCancelled()、doInBackground()方法，系统会自动调用
5.一个AsyncTask对象只能调用一次execute()方法，否则会抛出异常
6.
 */
public class MainActivity extends AppCompatActivity {
    private Button download;
    private Button cancel;
    private TextView text;
    private ProgressBar bar;
    private MyTask task;

    //创建AsyncTask的子类，三个参数分别表示输入的参数、执行进度类型、执行结果类型
    public class MyTask extends AsyncTask<String,Integer,Long>{

        //onPreExecute()线程任务开始前的准备工作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            text.setText("等待加载");
        }

        //onPostExecute()接收doInBackground()执行的结果，并在ui显示
        @Override
        protected void onPostExecute(Long l) {
            super.onPostExecute(l);
            text.setText("加载成功");
        }
        //onProgressUpdate（）在主线程中当后台任务进程发生改变时调用
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            bar.setProgress(values[0]);
            text.setText("加载了"+values[0]+"%");
        }
        //取消时调用onCancelled()
        @Override
        protected void onCancelled(Long l) {
            super.onCancelled(l);
            text.setText("取消");
            bar.setProgress(0);
        }


        //doInBackground()在线程池中执行，用于执行异步任务
        //params表示输入参数，会调用publishProgress方法来更新任务
        //publishProgress会调用onProgressUpdate方法，返回结果给onPostExecute
        @Override
        protected Long doInBackground(String... strings) {
            try {
                int count=0;
                int length=1;
                while(count<99){
                    count+=length;
                    publishProgress(count);
                    Thread.sleep(50);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task=new MyTask();
        download=(Button) findViewById(R.id.download);
        text=(TextView) findViewById(R.id.text);
        cancel=(Button) findViewById(R.id.cancel);
        bar=(ProgressBar) findViewById(R.id.progressbar);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.execute();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.cancel(true);
            }
        });

    }
}
