//
//

package mrtubbs.savepage;
//
import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.widget.*;
import android.view.*;
import android.graphics.*;
import android.inputmethodservice.*;
import android.view.inputmethod.*;
import android.content.*;
import android.net.*;
import java.net.*;
import android.os.*;

public class BrowserAct extends Activity
{
	final String PREFIX_FILE = "file://";
	String currentUrl = "";
	String pagename, saveTitle;
	WebView web5;
	EditText etUrl, etSaveTitle;
	ProgressBar progress87;
	ImageButton btngo, btnClear, btnsave, btnExit;
	RelativeLayout root;
	RelativeLayout saveRContainer;
	int originHeight; //to keep track if keboard visible
	boolean localPage = false;
	boolean showKeyBoard = true;
	//
	InputMethodManager imm;
	Intent localIntent;
	//
	FileManager fm;
	//
	Context ctx_;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browseract);
		//
		initVoid();
		
		//
		try{
			Bundle datas = getIntent().getExtras();
			String path;
			if(datas!=null){
				path = datas.getString(Intent.EXTRA_TEXT);
				if(path!=null || !path.isEmpty()){
					path = fixedPath(path);
					//toast(path);
					showKeyBoard = false;
					if(isLocalFile(path)){
						localPage = true;
						//String pData = fm.readTextFile(path);
						String baseurl = path;
						try{
							baseurl = fm.getUrlFromLocal(path.replace(PREFIX_FILE,""));
						}catch(Exception e){
							baseurl = path;
						}
						loadPage(path);
						//loadLocalPage(baseurl,pData);
						etUrl.setText(baseurl);
					}else{
						loadPage(path);
					}
				}
			}
			
		}catch(Exception e){
			toast("err "+e);
		}
		
		if(showKeyBoard){
			etUrl.postDelayed(new Runnable(){
					public void run(){showSoftInput(etUrl);}
			},10);
		}

	}
	
	private boolean isLocalFile(String path){
		return fixedPath(path).startsWith(PREFIX_FILE);
	}
	
	private String fixedPath(String path){
		String env = Environment.getExternalStorageDirectory().getAbsolutePath();
		if(path.startsWith("/sdcard/")
		   || path.startsWith(env)){
			   return PREFIX_FILE+path;
		}
		return path;
	}
	
	private void initVoid(){
		//
		ctx_ = getApplicationContext();
		//
		root = (RelativeLayout) findViewById(R.id.browseractRoot);
		root.postDelayed(new Runnable(){
			public void run(){
				originHeight = root.getHeight();
				//toast(""+originHeight);
			}
		},100);
		
		saveRContainer = (RelativeLayout) findViewById(R.id.saveRenameContainer);
		//
		fm = new FileManager(ctx_);
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		//
		//
		web5 = (WebView) findViewById(R.id.web5);
		WebSettings wst = web5.getSettings();
		web5.getSettings().setJavaScriptEnabled(true);
		
		wst.setAppCacheEnabled(true);
		wst.setUserAgentString("android");
		wst.setDomStorageEnabled(true);
		web5.setWebChromeClient(new mWCC());
		web5.setWebViewClient(new mWVC());
		//
		web5.setInitialScale(1);
		web5.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		wst.setLoadWithOverviewMode(true);
		wst.setUseWideViewPort(true);
		wst.setBuiltInZoomControls(true);
		wst.setAppCachePath("/sdcard/fm/");
		//
		//
		progress87 = (ProgressBar)findViewById(R.id.progress_bar_1);
		progress87.setMax(100);
		//
		etUrl = (EditText)findViewById(R.id.et_url);
		etUrl.setSelectAllOnFocus(true);
		etUrl.setOnEditorActionListener(new TextView.OnEditorActionListener(){
			@Override
			public boolean onEditorAction(TextView v, int p2, KeyEvent p3){
				hideSoftInput();
				if(queryIsValid()){
					loadPage(currentUrl);
					return true;
				}
				return false;
			}
		});
		
		//etUrl.requestFocus();
		//
		btnClear = (ImageButton) findViewById(R.id.btn_url_clear);
		btnClear.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				//
				etUrl.setText("");
			}
		});
		//
		btngo = (ImageButton) findViewById(R.id.btn_search_go);
		btngo.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View p1){
				if (isKeyBoardShown()){
					if(queryIsValid()){
						hideSoftInput();
						loadPage(currentUrl);
					}
				}else{
					showSoftInput(etUrl);
				}
				
			}
		});
		
		etSaveTitle = (EditText) findViewById(R.id.et_save_offline_title);
		etSaveTitle.setSelectAllOnFocus(true);
		etSaveTitle.setOnEditorActionListener(new TextView.OnEditorActionListener(){
			@Override
			public boolean onEditorAction(TextView tv, int p1, KeyEvent event){
				//
				savePageConfirm(tv);
				return false;
			}
		} );
		
		btnsave = (ImageButton) findViewById(R.id.btn_save_prompt);
		btnsave.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				//
			    String saveTitle1 = web5.getTitle();
				//
				etSaveTitle.setText(saveTitle1);
				etSaveTitle.setSelected(true);
				saveRContainer.setVisibility(View.VISIBLE);
				//
				showSoftInput(etSaveTitle);
			}
		});
		//
		
	}
	
	public void savePageConfirm(View v){
		saveTitle = etSaveTitle.getText().toString().replace("/","_");
		if(saveTitle.isEmpty()){
			saveTitle = web5.getTitle();
		}
		final String path = fm.getSavePath(saveTitle);
		
		web5.saveWebArchive(path, false, new ValueCallback<String>(){

				@Override
				public void onReceiveValue(String p1)
				{
					// TODO: Implement this method
					if(fm.fileExists(path)){
						toast("saved: "+path); 
					}else{
						toast("failed to save: "+path);
					}
				}
			});
		//if(fm.fileExists(path)){
			//toast(path);
		//}
		//rewrite(path);
		saveRContainer.setVisibility(View.GONE);
		//
		hideSoftInput();
	}
	
	
	
	public void savePageCancel(View v){
		//
		saveRContainer.setVisibility(View.GONE);
		etSaveTitle.setText("");
		hideSoftInput();
	}
	
	public void loadPage(String url){
		web5.loadUrl(url);
	}
	
	public void loadLocalPage(String base, String wdata){
		web5.loadDataWithBaseURL(base,wdata,"multipart/related","UTF-8","");
	}
	
	private boolean queryIsValid(){
		//
		getUrl();// dont want too much clutter inneeer ?
		String q = currentUrl;
		//invalid if empty
		if(q.isEmpty() || q.equals("") || q.equals(" ")){
			return false;
		}
		//invalid if has bad characters ?
		    // return false
			
		return true;
	}
	
	public void getUrl(){
		//
		String url0 = etUrl.getText().toString();
		if(!url0.isEmpty()){
		    //
			String prep = "";
			
			// okay! I know there is a better, more efficient way to do this
			//
			if( ! (url0.startsWith("http://") || (url0.startsWith("https://")) || (url0.startsWith("file://")) )){
				prep = "http://";
			}
			//if(! (url0.endsWith(".com") || url0.endsWith(".net") || url0.endsWith(".org"))){}
			//
			currentUrl = prep+url0;
			if(!localPage){
				etUrl.setText(currentUrl);
			}

		}
		else{currentUrl="";}
	}
	
	public boolean isKeyBoardShown(){
		return root.getHeight() < originHeight ; 
	}
	
	private void showSoftInput(View v){
		v.requestFocus();
		if(!isKeyBoardShown()){
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
		}
	}
	
	private void hideSoftInput(){
		etUrl.clearFocus();
		etSaveTitle.clearFocus();
		imm.hideSoftInputFromWindow(etUrl.getWindowToken(),0);
	}
	
	//
	boolean opcr = false;
	//
	class mWCC extends WebChromeClient{
		//
		@Override
		public void onProgressChanged(WebView view, int newProgress){
			progress87.setProgress(newProgress);
			if(newProgress == 100 && !opcr){
				if(localPage){
					localPage = false;
				}else{
					etUrl.setText(web5.getUrl());
				}
				progress87.setVisibility(View.GONE);
				opcr = true;
			}
			//super.onProgressChanged(view, newProgress);
		}
		
	}// mwcc

	//
	String ht = "";
	class mWVC extends WebViewClient{
		@Override
		public void onPageFinished(WebView view, String url){
			pagename = view.getTitle();	
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon){
			opcr = false;
			etUrl.setText(url);
			progress87.setProgress(0);
			progress87.setVisibility(View.VISIBLE);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg)
		{
			// TODO: Implement this method
			super.onTooManyRedirects(view, cancelMsg, continueMsg);
		}
		
		
		
	} //mwvc
	
	public void onReturnButton(View v){
		if(web5.canGoBack()){
			if(isKeyBoardShown()){
				hideSoftInput();
			}
			web5.goBack();
		}else{
			super.onBackPressed();
		}
	}

	@Override
	public void onBackPressed(){ 
		super.onBackPressed();
	}
	
	
	public void toast(CharSequence msg){
		Toast.makeText(getApplicationContext(),msg,500).show();
	}
	
	public void toast(int msg){
		Toast.makeText(getApplicationContext(),""+msg,500).show();
	}
	
	
}
