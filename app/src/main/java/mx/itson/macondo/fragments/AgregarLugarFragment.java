package mx.itson.macondo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import mx.itson.macondo.R;
import mx.itson.macondo.entidades.LugarEntidad;
import mx.itson.macondo.persistencia.MacondoDbManger;
import mx.itson.macondo.util.MacondoLocationListener;
import mx.itson.macondo.util.MacondoUtils;
import mx.itson.macondo.vistas.MainActivity;

//import com.google.android.gms.maps.model

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AgregarLugarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarLugarFragment extends Fragment implements GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener, MacondoLocationListener.MacondoLcationListenerCallbacks, View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_LUGAR_ID="lugar_id";
    MapView mapView;
    EditText et_direccion;
    Marker ubicacion;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private View view;
    private EditText txt_latitud;
    private EditText txt_longitud;
    private LocationManager myLocationManager;
    private MacondoLocationListener mLocationListener;
    private boolean gpsActivo = false;
    private File photoFile;
    private ImageView imgLugar;
    private LugarEntidad lugar;
    private MacondoDbManger macondoDbManager;
    private EditText et_nombre;
    private EditText et_referencias;
    private EditText et_caracteristicas;
    private boolean toEdit=false;

    public AgregarLugarFragment() {
        // Required empty public constructor
    }

    public static AgregarLugarFragment newInstance(int sectionNumber) {
        AgregarLugarFragment fragment = new AgregarLugarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public static AgregarLugarFragment newInstance(int sectionNumber, int id) {
        AgregarLugarFragment fragment = new AgregarLugarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_LUGAR_ID, id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_agregar_lugar, container, false);
        // Inflate the layout for this fragment
        macondoDbManager = new MacondoDbManger(this.getActivity());
        lugar = new LugarEntidad();
        if (savedInstanceState != null) {
            int id = savedInstanceState.getInt(ARG_LUGAR_ID, 0);
            lugar = macondoDbManager.cargarLugar(id);
        }
        if (getArguments() != null) {
            int id = getArguments().getInt(ARG_LUGAR_ID, 0);
            lugar = macondoDbManager.cargarLugar(id);
        }
        et_nombre = (EditText) view.findViewById(R.id.et_nombre);
        et_direccion = (EditText) view.findViewById(R.id.et_direccion);
        et_referencias = (EditText) view.findViewById(R.id.et_referencias);
        et_caracteristicas = (EditText) view.findViewById(R.id.et_caracteristicas);
        txt_latitud = (EditText) view.findViewById(R.id.txt_latitud);
        txt_longitud = (EditText) view.findViewById(R.id.txt_longitud);
        imgLugar = (ImageView) view.findViewById(R.id.imgLugar);
        imgLugar.setOnClickListener(this);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        setUpMapIfNeeded();
        myLocationManager = (LocationManager) getActivity().getSystemService(
                Context.LOCATION_SERVICE);

        mLocationListener = new MacondoLocationListener(this);
        try {
            gpsActivo = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (gpsActivo) {
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60 * 2, 10, mLocationListener);
        } else {
            myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60 * 2, 10, mLocationListener);
        }

        onLocationChanged(getLastKnowLocation());
        view.findViewById(R.id.imgBtn_tomar_foto).setOnClickListener(this);
        view.findViewById(R.id.imgBtn_save_lugar).setOnClickListener(this);
        if (savedInstanceState != null) {
            lugar.setUri_foto(savedInstanceState.getString(MacondoDbManger.LugarTable.Columns.PATH_FOTO));
            lugar.setUri_thumb_foto(savedInstanceState.getString(MacondoDbManger.LugarTable.Columns.THUMB_FOTO));
            imgLugar.setImageURI(Uri.parse("file:" + lugar.getUri_foto()));
        }
        if(lugar.getId()!=0){
            toEdit=true;
            et_nombre.setText(lugar.getNombre());
            et_direccion.setText(lugar.getDireccion());
            et_caracteristicas.setText(lugar.getCaracteristicas());
            et_referencias.setText(lugar.getReferencias());
            imgLugar.setImageURI(Uri.parse("file:" + lugar.getUri_foto()));
            Location location=new Location("");
            location.setLatitude(lugar.getLatitud());
            location.setLongitude(lugar.getLongitud());
            onLocationChanged(location);
        }
        return view;
    }

    /**
     * @param imageView imagen que se pulsara para abrir de manera mas grande
     */
    private void loadPhoto(ImageView imageView) {
        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_image_show,
                (ViewGroup) this.getActivity().findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        TextView tv = (TextView) layout.findViewById(R.id.custom_fullimage_placename);
        tv.setText("Prueba...");
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((MainActivity) activity).setTitle(getString(R.string.title_section_nuevo_lugar));
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        try {
            gpsActivo = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (gpsActivo) {
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 10, mLocationListener);
        } else {
            myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60, 10, mLocationListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        myLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        myLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myLocationManager.removeUpdates(mLocationListener);
        /*if (mMap != null) {
            mMap.stopAnimation();
        }*/
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
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.setMyLocationEnabled(true);
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMapLongClickListener(this);
            MapsInitializer.initialize(getActivity());
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        ubicacion = marker;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (ubicacion == null) {
            ubicacion = mMap.addMarker(new MarkerOptions().position(latLng));
        } else {
            ubicacion.setPosition(latLng);
        }
        lugar.setLatitud(latLng.latitude);
        lugar.setLongitud(latLng.longitude);
        txt_latitud.setText(latLng.latitude + "");
        txt_longitud.setText(latLng.longitude + "");
        myLocationManager.removeUpdates(mLocationListener);
    }

    /**
     * @return ultima ubicacion que se registro del dispositivo
     */
    private Location getLastKnowLocation() {
        Location ubicacion = null;
        if (gpsActivo) {
            ubicacion = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (!gpsActivo || ubicacion == null) {
            ubicacion = myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        return ubicacion != null ? ubicacion : new Location("");
    }

    @Override
    public void onLocationChanged(Location argLocation) {
        LatLng latLng = new LatLng(argLocation.getLatitude(), argLocation.getLongitude());
        if (ubicacion == null) {
            ubicacion = mMap.addMarker(new MarkerOptions().position(latLng));
            ubicacion.setDraggable(true);
        } else {
            ubicacion.setPosition(latLng);
        }
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        mMap.animateCamera(update);
        lugar.setLatitud(latLng.latitude);
        lugar.setLongitud(latLng.longitude);
        txt_latitud.setText(latLng.latitude + "");
        txt_longitud.setText(latLng.longitude + "");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MacondoDbManger.LugarTable.Columns.PATH_FOTO, lugar.getUri_foto());
        outState.putString(MacondoDbManger.LugarTable.Columns.THUMB_FOTO, lugar.getUri_thumb_foto());
        outState.putInt(ARG_LUGAR_ID, lugar.getId());
    }

    /**
     * @return is valid all fields of lugar
     */
    private boolean validateFields() {
        boolean isCorrect = true;
        for (EditText editText : new EditText[]{et_nombre, et_direccion, et_referencias, et_caracteristicas}) {
            if (editText.getText() == null || editText.getText().toString().isEmpty() || editText.getText().toString().trim().length() == 0) {
                editText.setError("No puede quedar vacio...");
                isCorrect = false;
            } else {
                editText.setError(null);
            }
        }
        return isCorrect;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtn_tomar_foto:
                Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (tomarFotoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    photoFile = MacondoUtils.generateNewFilePath();
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(tomarFotoIntent, MacondoUtils.REQUEST_TOMAR_FOTO);
                    }
                }
                break;
            case R.id.imgBtn_save_lugar:
                if (validateFields()) {
                    lugar.setNombre(et_nombre.getText().toString().trim());
                    lugar.setDireccion(et_direccion.getText().toString().trim());
                    lugar.setReferencias(et_referencias.getText().toString().trim());
                    lugar.setCaracteristicas(et_caracteristicas.getText().toString().trim());
                    if(toEdit){
                        macondoDbManager.modificar(lugar);
                    }else {
                        macondoDbManager.insertar(lugar);
                    }
                    myLocationManager.removeUpdates(mLocationListener);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                break;
            case R.id.imageView:
                loadPhoto(this.imgLugar);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case MacondoUtils.REQUEST_TOMAR_FOTO:
                    String photopath = photoFile.getPath();
                    String thumbFile = photoFile.getName();
                    //set urls de las fotos, chica y grande

                    lugar.setUri_foto(photopath);
                    lugar.setUri_thumb_foto(thumbFile);
                    Bitmap bmp = BitmapFactory.decodeFile(photopath);
                    bmp = Bitmap.createScaledBitmap(bmp, 1500, 1500, false);
                    FileOutputStream fOut;
                    try {
                        fOut = new FileOutputStream(photoFile);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                        fOut.flush();
                        fOut.close();
                        fOut = new FileOutputStream(MacondoUtils.getThumbPath(thumbFile));
                        bmp = Bitmap.createScaledBitmap(bmp, 300, 300, false);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    imgLugar.setImageURI(Uri.parse("file:" + photoFile.getAbsolutePath()));
                    break;
            }
        }
    }


}
