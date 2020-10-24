package sleepchild.savepage;

import android.app.Activity;
import android.Manifest;
import android.content.pm.PackageManager;

public class AppPermissions
{
	public static void getPermissions(Activity activity){
		if(!hasPermissions(activity)){
			String[] perm = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
			activity.requestPermissions(perm, 234);
		}		
	}
	
	public static boolean hasPermissions(Activity activity){
		return activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == 
			PackageManager.PERMISSION_GRANTED;
	}
	
}
