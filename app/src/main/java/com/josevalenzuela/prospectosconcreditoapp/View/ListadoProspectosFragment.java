package com.josevalenzuela.prospectosconcreditoapp.View;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josevalenzuela.prospectosconcreditoapp.Presenter.ListadoProspectosPresenter;
import com.josevalenzuela.prospectosconcreditoapp.R;
import com.josevalenzuela.prospectosconcreditoapp.adapters.ProspectoAdapter;
import com.josevalenzuela.prospectosconcreditoapp.contracts.ListadoProspectosContract;
import com.josevalenzuela.prospectosconcreditoapp.models.Prospecto;

import java.util.List;

public class ListadoProspectosFragment extends Fragment implements ListadoProspectosContract.View, ProspectoAdapter.OnItemClickListener {

    private ListadoProspectosContract.Presenter presenter;
    private RecyclerView recyclerViewProspecto;
    private Context mContext;
    private Fragment infoProspectoFragment;
    private List<Prospecto> prospectos;
    private ProspectoAdapter prospectoAdapter;
    private FloatingActionButton addProspectoBtn;

    public ListadoProspectosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_listadoprospectos, container, false);
        this.recyclerViewProspecto = fragmentView.findViewById(R.id.recyclerProspectoViewId);
        this.infoProspectoFragment = new InfoProspectoFragment();
        this.addProspectoBtn = fragmentView.findViewById(R.id.nuevoProspectoBtn);

        presenter = new ListadoProspectosPresenter(this);
        presenter.obtenerProspectos();




        // Inflate the layout for this fragment
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void mostrarListaProspectos(List<Prospecto> prospectos) {
        this.prospectos = prospectos;
        prospectoAdapter = new ProspectoAdapter(prospectos, mContext, this::onItemClick);

        recyclerViewProspecto.setAdapter(prospectoAdapter);
        recyclerViewProspecto.setLayoutManager(new LinearLayoutManager(mContext));

    }

    @Override
    public void showError(String error) {
        Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        Prospecto prospecto = prospectos.get(position);
        bundle.putSerializable("prospectoSeleccionado", prospecto);
        infoProspectoFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.contenedorFragmentos, infoProspectoFragment).commit();
    }
}