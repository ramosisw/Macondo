package mx.itson.macondo.vistas;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import mx.itson.macondo.R;
import mx.itson.macondo.fragments.AcercaDeFragment;
import mx.itson.macondo.fragments.LugaresFragment;
import mx.itson.macondo.fragments.MapaFragment;
import mx.itson.macondo.persistencia.MacondoDbManger;
import mx.itson.macondo.util.NavigationDrawerFragment;
import mx.itson.macondo.util.Section;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private int position = 0;
    private CharSequence mTitle;
    private MacondoDbManger macondoDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        macondoDbManager = new MacondoDbManger(this);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (position != Section.LUGAR_NUEVO && position != Section.LUGAR_VER && position!= Section.LUGAR_EDITAR) {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
        } else {
            return;
        }
        /*if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }*/
        switch (position) {
            case Section.LUGAR:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, LugaresFragment.newInstance(position))
                        .commit();
                break;

            case Section.MAPA:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MapaFragment.newInstance(position))
                        .commit();
                break;

            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, AcercaDeFragment.newInstance(position))
                        .commit();
        }

    }

    public void onSectionAttached(int number) {
        position = number;
        if (mNavigationDrawerFragment != null)
            mNavigationDrawerFragment.setSection(number);
        switch (number) {
            case Section.ACERCA_DE:
                mTitle = getString(R.string.title_section_acerca_de);
                break;
            case Section.LUGAR:
                mTitle = getString(R.string.title_section_lugares);
                break;
            case Section.MAPA:
                mTitle = getString(R.string.title_section_mapa);
                break;
            case Section.LUGAR_NUEVO:
                mTitle = getString(R.string.title_section_nuevo_lugar);
                break;
            case Section.LUGAR_EDITAR:
                mTitle = getString(R.string.title_section_editar_lugar);
                break;
            case Section.LUGAR_VER:
                mTitle = getString(R.string.title_section_ver_lugar);
                break;
        }
        //restoreActionBar();

    }

    public void setTitle(String title) {
        mTitle = title;
        //restoreActionBarHomeAsUp();
    }

    public void restoreActionBarHomeAsUp() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        FragmentManager fragmentManager = getSupportFragmentManager();
        actionBar.setHomeAsUpIndicator(fragmentManager.getBackStackEntryCount() > 0 ? R.drawable.ic_arrow_back_white_36dp : R.drawable.ic_menu_white_36dp);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            switch (this.position) {
                case 2:
                    //getMenuInflater().inflate(R.menu.lugares, menu);
                    break;
            }
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                }
                return false;
            case android.R.id.background:
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                }
                return false;
            case R.id.action_edit:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            restoreActionBar();
        }
    }
}
