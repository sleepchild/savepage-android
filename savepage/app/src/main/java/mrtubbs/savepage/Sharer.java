package mrtubbs.savepage;
//
import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.widget.Toast;
//

//
public class Sharer extends Activity
{
	Context ctx_;
	final static String SAVEURL = "mrtubbs.savepage.SAVEURL";

	@Override
	protected void onStart()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onStart();
		//
		ctx_= getApplicationContext();
		String url = getIntent().getStringExtra(Intent.EXTRA_TEXT);
		if(url==null || url.isEmpty()){
			url = getIntent().getData().getPath();
		}
		/*
		// bg service
		Intent saver = new Intent(getApplicationContext(), SaveService.class);
		saver.putExtra(SAVEURL,url);
		startService(saver);
		//*/
		// or 2) open BrowserAct
		//*
		Intent saveInt = new Intent(this,BrowserAct.class);
		saveInt.putExtra(Intent.EXTRA_TEXT,url);
		startActivity(saveInt);
		//*/
		finish();
	}
	
	public void toast(CharSequence msg){
		Toast.makeText(getApplicationContext(),msg,1000).show();
	}
	
}
