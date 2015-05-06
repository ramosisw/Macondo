package mx.itson.macondo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mx.itson.macondo.R;
import mx.itson.macondo.adapters.LugarAdapter;
import mx.itson.macondo.entidades.LugarEntidad;
import mx.itson.macondo.persistencia.MacondoDbManger;
import mx.itson.macondo.util.Section;
import mx.itson.macondo.vistas.MainActivity;

public class LugaresFragment extends Fragment implements AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {


    private static final String ARG_SECTION_NUMBER = "section_number";


    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private LugarAdapter mAdapter;
    private List<LugarEntidad> items;
    private MacondoDbManger macondoDbManager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LugaresFragment() {
    }

    // TODO: Rename and change types of parameters
    public static LugaresFragment newInstance(int sectionNumber) {
        LugaresFragment fragment = new LugaresFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lugares, container, false);
        macondoDbManager = new MacondoDbManger(this.getActivity());
        //Load lugares
        items = macondoDbManager.cargarLugares();
        mAdapter = new LugarAdapter(this.getActivity(), items);
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        // Set OnItemClickListener so we can be notified on item clicks
        if(items.size()>0) {
            view.findViewById(R.id.txt_no_lugares).setVisibility(View.GONE);
        }
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        view.findViewById(R.id.imgBtn_add_lugar).setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((MainActivity) activity).setTitle(getString(R.string.title_section_lugares));
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container, VerLugarFragment.byId(Section.LUGAR_VER, items.get(position).getId()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
        final int id_lugar=items.get(i).getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.str_message_to_delete));
        builder.setMessage("No se podra recuperar...");
        builder.setCancelable(false);
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        macondoDbManager.eliminarLugar(id_lugar);
                        items.remove(i);
                        mAdapter.notifyDataSetChanged();
                        //notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtn_add_lugar:

                try {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, AgregarLugarFragment.newInstance(Section.LUGAR_NUEVO))
                            .addToBackStack(null)
                            .commit();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
