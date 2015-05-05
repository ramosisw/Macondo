package mx.itson.macondo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mx.itson.macondo.R;
import mx.itson.macondo.entidades.LugarEntidad;
import mx.itson.macondo.persistencia.MacondoDbManger;
import mx.itson.macondo.vistas.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class VerLugarFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_LUGAR_ID = "lugarid";
    private View view;
    private MacondoDbManger macondoDbManager;
    private LugarEntidad lugar;
    private TextView txt_nombre;
    private TextView txt_direccion;
    private TextView txt_referencias;
    private TextView txt_caracteristicas;
    private MapView mapView;
    private GoogleMap mMap;
    private ImageView imageView;

    public VerLugarFragment() {
        // Required empty public constructor
    }

    public static VerLugarFragment byId(int sectionNumber, int id) {
        VerLugarFragment fragment = new VerLugarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_LUGAR_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(MacondoDbManger.LugarTable.Columns.NOMBRE, et_nombre.getText().toString().trim());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ver_lugar, container, false);
        int id = getArguments().getInt(ARG_LUGAR_ID);
        macondoDbManager = new MacondoDbManger(this.getActivity());
        lugar = macondoDbManager.cargarLugar(id);
        txt_nombre = (TextView) view.findViewById(R.id.txt_nombre);
        txt_direccion = (TextView) view.findViewById(R.id.txt_direccion);
        txt_referencias = (TextView) view.findViewById(R.id.txt_referencias);
        txt_caracteristicas = (TextView) view.findViewById(R.id.txt_caracteristicas);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse("file:" + lugar.getUri_foto()));
        imageView.setOnClickListener(this);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        setUpMapIfNeeded();

        txt_nombre.setText(lugar.getNombre());
        txt_direccion.setText(lugar.getDireccion());
        txt_referencias.setText(lugar.getReferencias());
        txt_caracteristicas.setText(lugar.getCaracteristicas());
        LatLng latLng = new LatLng(lugar.getLatitud(), lugar.getLongitud());
        mMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(lugar.getNombre())

        );
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        mMap.animateCamera(update);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setAlpha(0.5f);
        return view;
    }

    /**
     * Inicializador del mapa
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = mapView.getMap();
            mapView.setClickable(true);
            mapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return motionEvent.getAction() == MotionEvent.ACTION_MOVE || view.onTouchEvent(motionEvent);
                }
            });
            //mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //mMap.setMyLocationEnabled(true);
            MapsInitializer.initialize(getActivity());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((MainActivity) activity).setTitle(getString(R.string.title_section_ver_lugar));
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if (mMap != null) {
            getFragmentManager()
                    .beginTransaction()
                    .remove(getFragmentManager().findFragmentById(R.id.map))
                    .commit();
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri imgUri = Uri.parse("file:" + lugar.getUri_foto());
                intent.setDataAndType(imgUri, "image/*");
                startActivity(intent);
                break;
        }
    }
}
