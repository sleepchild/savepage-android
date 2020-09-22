package mrtubbs.savepage;
//
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;
import java.util.List;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;
import android.graphics.Color;

public class SiteListAct extends Activity
{
	//
	LinearLayout itemOptsChoser, itemOptsCategChoice, categListHolder;
	LinearLayout optsNewCategContainer;
	String movingPagePath;
	String currentCategInView="All";
	EditText etNewCateg;
	RelativeLayout itemOptsContainer;
	TextView itemsOptionTitle, tabItemAll;
	ListView list5;
	FileManager fm;
	mAdaptor adapter;
	View lastTab;
	//boolean isrenaming = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pagelist);
		//
		initVoid();
	}
	
	private void initVoid(){
		//
		itemOptsContainer = (RelativeLayout) findViewById(R.id.item_options_container);
		itemsOptionTitle = (TextView) findViewById(R.id.item_opt_title);
		itemOptsChoser =(LinearLayout) findViewById(R.id.item_opts_choser);
		itemOptsCategChoice = (LinearLayout) findViewById(R.id.item_opt_categ_choice);
		categListHolder = (LinearLayout) findViewById(R.id.categ_list_holder);
		optsNewCategContainer = (LinearLayout) findViewById(R.id.opts_categ_new_container);
		etNewCateg = (EditText) findViewById(R.id.opts_categ_new_et);
		tabItemAll = (TextView) findViewById(R.id.tab_item_all);
		//
		fm = new FileManager(getApplicationContext());
		//
		showCategTabs();
		adapter = new mAdaptor(getApplicationContext(),fm.getPages(currentCategInView));
		list5 = (ListView) findViewById(R.id.list5);
		list5.setAdapter(adapter);
		list5.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> p1, View view, int p3, long p4)
			{
				String path = view.getTag().toString();
				startBrowserAct(path);
			}
		});
		list5.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> p1, View view, int pos, long p4)
			{
				showItemOptions(view.getTag().toString());
				return true;
			}
		});
		//
	}// e.o initVoid

	public void startBrowserAct(CharSequence path){
		Intent webInt = new Intent(SiteListAct.this, BrowserAct.class );
		webInt.putExtra(Intent.EXTRA_TEXT, path);
		startActivityForResult(webInt,425);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//
		super.onActivityResult(requestCode, resultCode, data);
		adapter.update(fm.getPages(currentCategInView));
		showCategTabs();
	}
	
	public void switchTab(View v){
		String categName = v.getTag().toString();
		if(lastTab!=null){
			TextView ltv = (TextView) lastTab;
			ltv.setTextColor(Color.parseColor(getString(R.color.tab_inactive)));
		}else{
			//
			tabItemAll.setTextColor(Color.parseColor(getString(R.color.tab_inactive)));
		}
		TextView tac = (TextView) v;
		tac.setTextColor(Color.parseColor(getString(R.color.tab_active)));
		//
		adapter.update(fm.getPages(categName));
		currentCategInView = categName;
		lastTab = v;
	}
	
	public void onItemOptionSelected(View v){
		switch(v.getId()){
			case R.id.item_opt_rename:
				//
				// showRenameDialog();
				toast("oops, I forgot to implement that!");
				break;
			case R.id.item_opt_move:// move to category/
				//
				showCategChoice();
				break;
			case R.id.item_opt_delete:
				//
				toast("oops, I forgot to implement that!");
				break;
		}
		//hideItemOptions();
	}
	
	
	boolean isOptionsShown = false;
	public void showItemOptions(String itemPath){
		//
		movingPagePath = itemPath;
		itemsOptionTitle.setText( fm.getFile(itemPath).getName());
		showView(itemOptsContainer);
		showView(itemOptsChoser);
		hideView(itemOptsCategChoice);
		isOptionsShown=true;
		//itemOptsContainer.setVisibility(View.VISIBLE);
		//itemOptsContainer.animate().translationX(0).setDuration(500).start();
	}
	
	public void hideItemOptions(View v){
		hideItemOptions();
	}
	
	public void hideItemOptions(){
		//
		//itemOptsContainer.setVisibility(View.GONE);
		hideView(itemOptsContainer);
		hideView(itemOptsCategChoice);
		hideView(itemOptsChoser);
		hideView(optsNewCategContainer);
		//
		etNewCateg.setText("");
		//showView(itemOptsChoser);
		//itemsOptionTitle.setText("");
		try{
			categListHolder.removeAllViews();
		}catch(Exception e){}
		isOptionsShown=false;
	}
	
	public void showCategChoice(){
		//
		//itemOptsCategChoice.setVisibility(View.VISIBLE);
		List<String> categs = fm.getCategories();
		if(categs!=null){
			for(String cat : categs){
				//*
				LinearLayout root = (LinearLayout)getLayoutInflater().inflate(R.layout.categ_item,null,false);
				TextView tvt = (TextView) root.findViewById(R.id.tv_categ_item);
				//root.removeAllViews();
				tvt.setText(cat);
				root.setTag(cat);
				categListHolder.addView(root);
				//*/
			}
		}
		showView(itemOptsCategChoice);
		hideView(itemOptsChoser);
	}
	
	public void onChoseCategory(View v){
		if(fm.moveToCateg(v.getTag().toString(), movingPagePath)){
			adapter.update(fm.getPages(currentCategInView));
			showCategTabs();
			//
			toast("moved");
		}else{ toast("unspecified error occured"); }
		//
		hideItemOptions();
	}
	
	public void categCreateNewDialog(View v){
		//
		showView(optsNewCategContainer);
		hideView(itemOptsCategChoice);
		etNewCateg.requestFocus();
	}
	
	public void createCateg(View v){
		//
		String catN = etNewCateg.getText().toString();
		if(!catN.isEmpty()){
			if(fm.moveToCateg(catN, movingPagePath)){
				adapter.update(fm.getPages(currentCategInView));
				toast("moved");
			}else{ toast("unspecified error occured"); }
			hideItemOptions();
		}
	}
	
	public void hideRenamer(View v){
		showView(itemOptsCategChoice);
		hideView(optsNewCategContainer);
	}
	
	//////////
	LinearLayout tabholder;
	public void showCategTabs(){
		if(tabholder==null){
			tabholder = (LinearLayout) findViewById(R.id.tab_item_container);
		}
		tabholder.removeAllViews();
		for(String tab : fm.getCategories()){
			LinearLayout root = (LinearLayout) getLayoutInflater().inflate(R.layout.tab_item_categ,null,false);
			TextView tvTab = (TextView) root.findViewById(R.id.tab_item_categTextView);
			tvTab.setText(tab);
			tvTab.setTag(tab);
			tabholder.addView(root);
		}
	}
	
	//////////////
	private void showView(View v){
		v.setVisibility(View.VISIBLE);
	}
	private void hideView(View v){
		v.setVisibility(View.GONE);
	}
	
	@Override
	public void onBackPressed()
	{
		if(isOptionsShown){
			hideItemOptions();
			return;
		}
		super.onBackPressed();
	}
	
	public void toast(CharSequence msg){
		Toast.makeText(getApplicationContext(),msg,500).show();
	}
	
}
