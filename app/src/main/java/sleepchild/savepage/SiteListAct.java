package sleepchild.savepage;
//
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.content.*;
import android.widget.*;
import android.view.*;
import android.view.inputmethod.*;

public class SiteListAct extends Activity 
{
	Context ctx;
	ListView list54;
	Util util;
	mAdaptor adaptor;
	LinearLayout pagelistContainer;
	LayoutInflater mInflator;
	String currentCategory = "all";
	TextView currentCategview;
	actionDialog adg;
	View focusedView;
	RelativeLayout root;
	int originHeight=0;
	InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(AppPermissions.hasPermissions(this)){
			try{
				initV();
			}catch(Exception e){toast(e.getMessage());}
		}else{AppPermissions.getPermissions(this);}
	}
	
	private void initV(){
		setContentView(R.layout.pagelist);
		//
		root = (RelativeLayout) findViewById(R.id.pagelistRoot);
		root.postDelayed(new Runnable(){
			public void run(){
				originHeight = root.getHeight();
			}
		},100);
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		//
		ctx = getApplicationContext();
		util = new Util(ctx);
		adaptor = new mAdaptor(ctx);
		list54 = (ListView)findViewById(R.id.pagelist_list5);
		list54.setDivider(null);
		list54.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View view, int pos, long p4)
				{
					Page pg = (Page) view.getTag();
					Intent viewIntent = new Intent(SiteListAct.this, BrowserAct.class);
					viewIntent.putExtra(Intent.EXTRA_TEXT, pg.getPath());
					startActivity(viewIntent);
					//*/
				}
		});
		list54.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View view, int pos, long p4)
				{
					Page pg = (Page)view.getTag();
					adg = new actionDialog(SiteListAct.this, pg);
					adg.show();
					return true;
				}
			});
		//
		list54.setAdapter(adaptor);
		updateLists(currentCategory);
		//
		pagelistContainer = (LinearLayout) findViewById(R.id.pagelist_categitem_container);
		mInflator = getLayoutInflater(); 
		currentCategview = (TextView) findViewById(R.id.categitem_tab_title);
		currentCategview.setTextColor(getColor(R.color.tab_active));
		//
		loadPageCategs();
	}
	
	public void otc(View v){
		if(adg!=null){
			adg.otc(v);
		}
	}
	
	public void OnActionComplete(String action, Page page, String datas){
		//
		switch(action){
			case "rename":
				break;
			case "move":
				toast("page moved");
				loadPageCategs();
				break;
			case "delete":
				toast("file deleted : "+datas);
				break ;
		}
		updateLists(currentCategory);
	}
	
	private void updateLists(String category){
		adaptor.update(util.getPages(category));
		currentCategory = category;
	}
	
	private void loadPageCategs(){
		pagelistContainer.removeAllViews();
		View v;
		for(String categ : util.getCategories()){
			v = mInflator.inflate(R.layout.tab_item_categ, null,false);
			TextView ctitle = (TextView) v.findViewById(R.id.categitem_tab_title);
			ctitle.setText(categ);
			v.setTag(categ);
			pagelistContainer.addView(v);
		}
	}
	
	public void onChoseCategory(View v){
		try{
		updateLists(v.getTag().toString()); 
		if(currentCategview!=null){
			currentCategview.setTextColor(getColor(R.color.tab_inactive));
		}
		currentCategview = (TextView) v.findViewById(R.id.categitem_tab_title);
		currentCategview.setTextColor(getColor(R.color.tab_active));
		}catch(Exception e){toast(e.getMessage());}
	}
	
	public void toast(CharSequence msg){
		Toast.makeText(getApplicationContext(),msg,500).show();
	}
	
	public boolean isKeyBoardShown(){
		return root.getHeight() < originHeight ; 
	}
	
	public void showSoftInput(View v){
		focusedView = v;
		focusedView.requestFocus();
		if(!isKeyBoardShown()){
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
		}
	}

	public void hideSoftInput(){
		if(!isKeyBoardShown()) return;
		focusedView.clearFocus();
		imm.hideSoftInputFromWindow(focusedView.getWindowToken(),0);
	}

	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		super.onBackPressed();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		// TODO: Implement this method
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(AppPermissions.hasPermissions(this)){
			initV();
		}else{
			toast("Storage permissions required");
			finish();
		}
	}
	
	
	
	
}
