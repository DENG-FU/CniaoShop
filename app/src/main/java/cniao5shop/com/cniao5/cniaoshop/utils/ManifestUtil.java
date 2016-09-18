package cniao5shop.com.cniao5.cniaoshop.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by DENGFU on 2016/7/22.
 *
 * read data from Manifest file
 */
public class ManifestUtil {

    public static String getMetaDataValue(Context context,String name,String def){
        String value = getMetaDataValue(context,name);
        return (value == null)? def : value;
    }

    public static String getMetaDataValue(Context context,String name){
        Object value = null;

        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;

        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(),128);
            if (applicationInfo != null && applicationInfo.metaData != null){
                value = applicationInfo.metaData.get(name);
            }
        }catch (PackageManager.NameNotFoundException e){
            throw new RuntimeException("Could not read the name in the manifest file.",e);
        }
        if (value == null){
            throw new RuntimeException("The name '"+name+"' is not defined in the menifest file's meta data.");
        }
        return value.toString();
    }
}
