package sleepchild.savepage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.*;
import android.view.*;
import android.widget.*;

public class actionDialog extends Dialog
{
	String title="";
	Page mPage;
	Context ctx;
	EditText etRename;
	SiteListAct slact;
	Util util;
	LinearLayout categChoosermain, categChooserNewCateg;
	
	actionDialog(SiteListAct act, Page page){
		super(act);
		slact=act;
		mPage = page;
		ctx = act.getApplicationContext();
		util = act.util;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adg_actions);
		//
		initV();
	}
	
	private void initV(){
		//
		TextView adgTitle = (TextView) findViewById(R.id.adg_title);
		adgTitle.setText(mPage.getTitle());
	}
	
	public void otc(View v){
		//*
		switch(v.getTag().toString()){
			case "rename":
				initR();
				break;
			case "renameokay":
				String newname = etRename.getText().toString();
				// util.rename(mPage.getPath(), newname);
				if(newname.equals(mPage.getTitle())){
					slact.toast( "nothing changed.");
				}else{
					if(Util.renameFile(mPage,newname)){
						slact.OnActionComplete("rename", mPage,"");
					}else{
						slact.toast("failed to rename..");
					}
					dismiss();
				}
				break;
			case "move":
				initM();
				break;
			case "newcateg":
				createNewCateg();
				break;
			case "delete":
				setContentView(R.layout.adg_action_delete);
				getTV(R.id.delete_target_name).setText(mPage.getTitle());
				break;
			case "delete_confirmed":
				Util.deleteFile(mPage.getPath());
				slact.OnActionComplete("delete", mPage,"");
				dismiss();
				break;
			case "cancel":
				slact.hideSoftInput();
				dismiss();
				break;
			default:
			    util.toast("?");dismiss();
				break;
		}
		//*/
	}
	
	
	private void initR(){
		setContentView(R.layout.adg_action_rename);
		getTV(R.id.adg_rename_title).setText(mPage.getTitle());
		etRename = getET(R.id.adg_rename_et_newname);
		etRename.setSelectAllOnFocus(true);
		etRename.setText(mPage.getTitle());
		etRename.requestFocus();
		slact.showSoftInput(etRename);
	}
	
	private void initM(){
		setContentView(R.layout.adg_action_move);
		categChoosermain = getLL(R.id.adg_action_movecategs);
		categChooserNewCateg = getLL(R.id.adg_action_move_newcategcontainer);
		LinearLayout container = (LinearLayout) findViewById(R.id.adg_action_move_categ_container);
		container.removeAllViews();
		LayoutInflater linf = LayoutInflater.from(ctx);
		for(String categ: util.getCategories()){
			View v = linf.inflate(R.layout.categ_item, null, false);
			v.setTag(categ);
			TextView ctitle = (TextView) v.findViewById(R.id.categ_item_title);
			ctitle.setText(categ);
			v.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View vi)
				{
					util.moveToCateg(vi.getTag().toString(), mPage.getPath());
					slact.OnActionComplete("move",mPage,"");
					dismiss();
				}
			});
			container.addView(v);
		}
	}
	
	private void createNewCateg(){
		categChoosermain.setVisibility(View.GONE);
		categChooserNewCateg.setVisibility(View.VISIBLE);
		final EditText newcategname = (EditText) findViewById(R.id.adg_action_newcategEditText);
		Button btn = (Button) findViewById(R.id.adg_action_newcategButton);
		btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View p1)
			{
				String nn =newcategname.getText().toString();
				if(!nn.isEmpty()){
					util.moveToCateg(nn, mPage.getPath());
					slact.OnActionComplete("move",mPage,"");
					dismiss();
				}
			}
		});
		newcategname.requestFocus();
		slact.showSoftInput(newcategname);
		
		//
	}
	
	private LinearLayout getLL(int resId){
		LinearLayout llt = (LinearLayout) findViewById(resId);
		return llt;
	}
	private TextView getTV(int resId){
		TextView ttv = (TextView) findViewById(resId);
		return ttv;
	}
	
	private EditText getET(int resid){
		EditText ett = (EditText) findViewById(resid);
		return ett;
	}
	
	
}
