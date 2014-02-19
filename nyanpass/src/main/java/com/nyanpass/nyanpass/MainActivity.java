package com.nyanpass.nyanpass;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends ActionBarActivity {
    SoundPool sp;
    int music;
    RequestParams params = new RequestParams("ck", "1");
    AsyncHttpClient client = new AsyncHttpClient();
    AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody) {
            if (statusCode == 200){
                TextView textView = (TextView) findViewById(R.id.count);
                textView.setText(new String(responseBody));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        //init
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        music = sp.load(this, R.raw.nyanpass, 1);

        refresh();
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
        if (id == R.id.action_website) {
            Intent internet = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.nyanpass.com/"));
            startActivity(internet);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void nyanpass(View view) {
        sp.play(music, 1, 1, 0, 0, 1);
        refresh();
    }
    public void refresh(){
        client.post("http://www.nyanpass.com/count" + String.valueOf((int)(Math.random() * 1000)), params, handler);
    }
}
