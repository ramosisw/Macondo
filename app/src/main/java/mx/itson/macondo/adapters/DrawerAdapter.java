package mx.itson.macondo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mx.itson.macondo.R;

/**
 * @author  ramos.isw@gmail.com
 * Created by Julio on 15/04/2015.
 */
public class DrawerAdapter extends BaseAdapter {
    private final Context context;
    private final int[] items;

    public DrawerAdapter(Context context, int[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public String getItem(int i) {
        return items[i] + "";
    }

    @Override
    public long getItemId(int i) {
        return items[i];
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.fragment_drawer_item, null);
            int resource = 0;
            switch (items[i]) {
                case R.string.title_section_acerca_de:
                    resource = R.drawable.ic_info_outline_white_24dp;
                    break;
                case R.string.title_section_lugares:
                    resource = R.drawable.ic_view_list_white_24dp;
                    break;
                case R.string.title_section_mapa:
                    resource = R.drawable.ic_satellite_white_24dp;
                    break;
            }
            ((ImageView) view.findViewById(R.id.ic_small)).setImageResource(resource);
            ((TextView) view.findViewById(R.id.tv_nombre)).setText(context.getString(items[i]));
        }

        return view;
    }
}
