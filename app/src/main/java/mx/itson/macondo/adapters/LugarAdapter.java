package mx.itson.macondo.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.itson.macondo.R;
import mx.itson.macondo.entidades.LugarEntidad;
import mx.itson.macondo.util.MacondoUtils;

/**
 * @author  ramos.isw@gmail.com
 * Created by Julio on 15/04/2015.
 */
public class LugarAdapter extends BaseAdapter {
    private final Activity activity;
    private final List<LugarEntidad> items;

    public LugarAdapter(Activity activity, List<LugarEntidad> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public LugarEntidad getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.fragment_lugares_item, null);
        }
        final LugarEntidad lugar = items.get(i);
        ImageView imagen = ((ImageView) view.findViewById(R.id.img_lugar_small));
        imagen.setImageURI(Uri.parse("file:" + MacondoUtils.getThumbPath(lugar.getUri_thumb_foto())));
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = new ImageView(activity);
                img.setImageURI(Uri.parse("file:" + lugar.getUri_foto()));
                loadPhoto(img);
            }
        });
        ((TextView) view.findViewById(R.id.tv_nombre)).setText(lugar.getNombre());
        ((TextView) view.findViewById(R.id.tv_direcion)).setText(lugar.getDireccion());
        ((TextView) view.findViewById(R.id.tv_referencias)).setText(lugar.getReferencias());
        return view;
    }

    /**
     * @param imageView imagen que se pulsara para abrir de manera mas grande
     */
    private void loadPhoto(ImageView imageView) {
        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this.activity);
        LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_image_show,
                (ViewGroup) this.activity.findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        TextView tv = (TextView) layout.findViewById(R.id.custom_fullimage_placename);
        tv.setText("Prueba...");
        tv.setVisibility(View.GONE);
        image.setImageDrawable(imageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        imageDialog.create();
        imageDialog.show();
    }
}
