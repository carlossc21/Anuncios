package es.carlossc212.anuncios.view.activityfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.carlossc212.anuncios.R;
import es.carlossc212.anuncios.objects.Historial;
import es.carlossc212.anuncios.view.adapter.AnuncioAdapter;
import es.carlossc212.anuncios.viewmodel.AnuncioViewModel;


public class PrincipalFragment extends Fragment {

    private View root;
    private RecyclerView rvAnuncios;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Historial.setCurrentFragment(this);
    }

    private void initialize() {
        rvAnuncios = root.findViewById(R.id.rvAnuncios);
        rvAnuncios.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        AnuncioViewModel avm = new AnuncioViewModel(getActivity().getApplication());
        AnuncioAdapter adapter = new AnuncioAdapter(getActivity());
        //adaptador
        rvAnuncios.setAdapter(adapter);
        //livedata
        //observar -> adaptador
        avm.getLivedataAnunios().observe(getActivity(), anuncios -> {
            adapter.setAnunciosList(anuncios);
        });
        //requestAnuncios
        avm.requestAllAnuncios();

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(v->{
            NavHostFragment.findNavController(PrincipalFragment.this).navigate(R.id.to_add);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_principal, container, false);
        initialize();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Historial.setCurrentFragment(this);
    }
}