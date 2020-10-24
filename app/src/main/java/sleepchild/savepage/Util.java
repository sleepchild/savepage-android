
// package mrtubbs.fimi;
package sleepchild.savepage;

import android.content.*;
import java.util.*;
import android.os.*;
import java.io.*;
import android.graphics.*;
import android.widget.*;

public class Util
{
	private Context ctx;
	private String dir = "/.sleepchild/savepage/pages/"; //
	private String folder, newPath;
	private int inc = 1;
	private String repl="73u6rt";
	
	
	Util(Context context){
		this.ctx = context;
		folder = Environment.getExternalStorageDirectory().getAbsolutePath() + dir;
		mkdirs(folder);
		rmEmptyDirs();
	}
	
	public String getSavePath(String filename, String categ){
		mkdirs(folder+categ);
		return fixPath( folder+categ+"/"+filename+".mht" );
	}
	
	private String fixPath(String path){
		newPath = path.trim();
		File f = new File(newPath);
		if(f.exists()){
			//
			if(newPath.endsWith(repl)){
				newPath = newPath.replace(repl, ".mht");
			}
			repl = "-"+inc+".mht";
			newPath = newPath.replace(".mht",repl);
			inc++;
			//
			newPath = fixPath(newPath);
		}
		//
		inc = 1;
		return newPath;
	}
	
	public List<Page> getPages(String category){
		List<Page> clist;
		if(category.equalsIgnoreCase("all")){
			clist = getFiles(folder);
		}else{
			clist = getFiles(folder+category);
		}
		return clist;
	}
	
	public List<String> getCategories(){
		List<String> categs = new ArrayList<>();
		File[] fls = new File(folder).listFiles();
		if(fls!=null){
			for(File fl: fls){
				if(fl.isDirectory()){
					categs.add(fl.getName());
				}
			}
		}
		Collections.sort(categs, new Comparator<String>(){
				@Override
				public int compare(String lhs, String rhs)
				{
					return lhs.toLowerCase().compareTo(rhs.toLowerCase());
				}
		});
		return categs != null ? categs : null;
	}
	
	private boolean newCategory(String categoryName){
		//
		categoryName = categoryName.replace("/","_");
		if(!fileExists(folder+categoryName)){
			mkdirs(folder+categoryName);
		}
		return fileExists(folder+categoryName);
	}
	
	public boolean moveToCateg(String category, String filePath){
		if(newCategory(category)){
			File page = getFile(filePath);
			String toPath = folder+category+"/"+page.getName();
			
			if(fileExists(toPath)){
				return false;
			}
			try
			{
				FileInputStream in = new FileInputStream(filePath);
				FileOutputStream out = new FileOutputStream(toPath);
				//
				byte[] buff = new byte[in.available()];
				//
				in.read(buff);
				out.write(buff);
				//
				in.close();
				out.close();
				if(fileExists(toPath)){
					getFile(filePath).delete();
					return true;
				}
			}
			catch (FileNotFoundException e)
			{}
			catch(Exception e){}
		}
		return false;

	}
	
	private void rmEmptyDirs(){
		//
		File fld =new File(folder);
		for(File flx : fld.listFiles()){
			if(flx.isDirectory()){
				if(flx.listFiles().length <1){
					flx.delete();
				}
			}
		}
	}
	
	
	private List<Page>  getFiles(String fpath){
		File pDir = new File(fpath);
		List<Page> plist = new ArrayList<>();
		if(!pDir.exists()){return null;}
		for(File fl : pDir.listFiles()){
			if(fl.isDirectory()){
				for(File xfl: fl.listFiles()){
					// at this point we dont expect subfolders, but we'l check anyways
					if(xfl.isFile()){
						plist.add(new Page(xfl.getName().replace(".mht",""), xfl.getAbsolutePath()));
					}// we'll ignore any folders here
				}
				//
			}else if(fl.isFile()){
				plist.add(new Page(fl.getName().replace(".mht",""), fl.getAbsolutePath()));
			}
		}
		return plist;
	}
	
	public boolean mkdirs(String path){
		return new File(path).mkdirs();
	}
	
	public File getFile(String path){
		return new File(path);
	}
	
	public boolean fileExists(String path){
		return getFile(path).exists();
	}
	
	public boolean fileExists(File file){
		return file.exists();
	}
	
	
	public static boolean renameFile(Page page, String newname){
		File fl = new File(page.getPath());
		if(fl.exists()){
			String np = fl.getAbsolutePath().replace(page.getTitle(), newname);
			File nfl = new File(np);
			fl.renameTo(nfl);
			if(fl.exists()){return false ;}
		}
		return true;
	}
	
	public static boolean deleteFile(String path){
		return new File(path).delete();
	}
	
	public String getUrlFromLocal(String path){
		//
		String cData = readTextFile(path);
		String c = cData;
		String d = c.split("Snapshot-Content-Location:")[1];
		String e = d.split("Subject:")[0];
		if(!e.isEmpty()){
			return e;
		}
		
		return "http://null";
	}
	
	
	
	public String readTextFile(String path){
		File tf = new File(path);
		if(!tf.exists()){return"";}
		
		//
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];

		try{
			FileInputStream is = new FileInputStream(tf);
			Reader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		}
		catch(IOException ioe){}
		//catch(FileNotFoundException fnf){}
		finally{}

		return writer.toString();
		//
		//return "";
	}	
	
	public static int parseCol(String col){
		return Color.parseColor(col);
	}
	
	public void toast(CharSequence msg){
		Toast.makeText(ctx, msg, 500).show();
	}
	
	public static void toast(Context ctx, CharSequence msg){
		Toast.makeText(ctx, msg, 500).show();
	}
	
}
