package es.carlossc212.anuncios.view.activityfragments;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import es.carlossc212.anuncios.R;
import es.carlossc212.anuncios.repository.AnunciosRepository;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnunciosRepository r = new AnunciosRepository(this);
        initialize();
    }

    private void initialize(){
        NavController navC = Navigation.findNavController(this, R.id.nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(navC.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navC, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

}