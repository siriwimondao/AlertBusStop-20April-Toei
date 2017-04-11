package rsu.siriwimon.pakdeeporn.alertbusstop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by masterUNG on 4/11/2017 AD.
 */

public class MyAdapter extends BaseAdapter{

    private Context context;
    private String[] busStopStrings, statusStrings;

    public MyAdapter(Context context,
                     String[] busStopStrings,
                     String[] statusStrings) {
        this.context = context;
        this.busStopStrings = busStopStrings;
        this.statusStrings = statusStrings;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}   // Main Class
