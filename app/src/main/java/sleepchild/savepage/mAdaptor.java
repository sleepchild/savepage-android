package sleepchild.savepage;
import android.widget.BaseAdapter;
import java.util.List;
import android.view.View;
import java.util.ArrayList;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;

public class mAdaptor extends BaseAdapter
{
	LayoutInflater mInflator;
	
	private List<Page> pagelist = new ArrayList<>();;

	mAdaptor(Context ctx){
		mInflator = LayoutInflater.from(ctx);
	}
	
	mAdaptor(Context ctx, List<Page> pagelist){
		this.pagelist = pagelist;
		mInflator = LayoutInflater.from(ctx);
	}

	public void update(List<Page> newList){
		this.pagelist = newList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		//
		return pagelist.size();
	}

	@Override
	public Object getItem(int p1)
	{
		return pagelist.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}

	@Override
	public View getView(int pos, View view, ViewGroup p3)
	{
		//
		Page pg = pagelist.get(pos);
		view = mInflator.inflate(R.layout.list_item,null);
		TextView li_Title = (TextView) view.findViewById(R.id.listitem_tv_title);
		
		li_Title.setText(pg.getTitle());
		view.setTag(pg);
		//
		return view;
	}
	
}
