package mrtubbs.savepage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout3);
		//
    }
	
	
	public void onAction(View v){
		//
		int id = v.getId();
		if(id == R.id.ll_enter_url){
			//
			Intent browserint = new Intent(MainActivity.this, BrowserAct.class);
			startActivity(browserint);
		}
		else if(id == R.id.ll_saved_sites){
			//
			Intent sitelist = new Intent(MainActivity.this, SiteListAct.class);
			startActivity(sitelist);
		}
	}
	
	public void toast(CharSequence msg){
		//
		Toast.makeText(getApplicationContext(),msg, 500).show();
	}

	
	
}
