package mx.itson.macondo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import mx.itson.macondo.R;
import mx.itson.macondo.entidades.LugarEntidad;
import mx.itson.macondo.persistencia.MacondoDbManger;
import mx.itson.macondo.vistas.MainActivity;

public class MapaFragment extends Fragment implements GoogleMap.OnMarkerDragListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    View rootView;
    MapView mapView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MacondoDbManger macondoDbManager;
    private List<LugarEntidad> lugares;

    public MapaFragment() {
    }

    public static MapaFragment newInstance(int sectionNumber) {
        MapaFragment fragment = new MapaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        macondoDbManager = new MacondoDbManger(this.getActivity());
        lugares = macondoDbManager.cargarLugares();
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        setUpMapIfNeeded();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
    public void onDestroyView() {
        super.onDestroyView();

        /*if (mMap != null) {
            getFragmentManager()
                    .beginTransaction()
                    .remove(getFragmentManager().findFragmentById(R.id.map))
                    .commit();
        }*/
    }

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
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.setMyLocationEnabled(true);
            MapsInitializer.initialize(getActivity());
            /*Fragment f=getActivity().getSupportFragmentManager().findFragmentById(2131230787);
            mMap = ((SupportMapFragment) f.getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            */
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng lugarLoc = null;
        for (LugarEntidad lugar : lugares) {
            lugarLoc = new LatLng(lugar.getLatitud(), lugar.getLongitud());
            MarkerOptions mark=new MarkerOptions().position(lugarLoc).title(lugar.getNombre()).snippet(lugar.getDireccion());
            mMap.addMarker(mark);
            builder.include(lugarLoc);
        }
        LatLngBounds bounds = builder.build();
        int padding = 20; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        //mMap.animateCamera(cu);
        try {
            mMap.animateCamera(cu);
        } catch (Exception ex) {
            cu = CameraUpdateFactory.newLatLngZoom(lugarLoc,12);
            mMap.animateCamera(cu);
        }

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Toast.makeText(getActivity(), "Marker " + marker.getId() + " Drag@" + marker.getPosition(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(getActivity(), "Marker " + marker.getId() + " DragEnd", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(getActivity(), "Marker " + marker.getId() + " DragStart", Toast.LENGTH_SHORT).show();

    }
}
