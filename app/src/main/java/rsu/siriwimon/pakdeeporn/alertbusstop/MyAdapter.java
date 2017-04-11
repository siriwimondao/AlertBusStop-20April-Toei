package rsu.siriwimon.pakdeeporn.alertbusstop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by masterUNG on 4/11/2017 AD.
 */

public class MyAdapter extends BaseAdapter{

    private Context context;
    private String[] busStopStrings, statusStrings;
    private TextView textView;
    private ImageView imageView;
    private int[] ints = new int[]{R.mipmap.ic_bus, R.mipmap.ic_destination};

    public MyAdapter(Context context,
                     String[] busStopStrings,
                     String[] statusStrings) {
        this.context = context;
        this.busStopStrings = busStopStrings;
        this.statusStrings = statusStrings;
    }

    @Override
    public int getCount() {
        return busStopStrings.length;
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

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.my_listview, viewGroup, false);

        //Initial View
        textView = (TextView) view1.findViewById(R.id.txtNameBus);
        imageView = (ImageView) view1.findViewById(R.id.imvIcon);

        //Show View
        textView.setText(busStopStrings[i]);
        imageView.setImageResource(ints[Integer.parseInt(statusStrings[i])]);

        return view1;
    }
}   // Main Class
