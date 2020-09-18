package com.josevalenzuela.prospectosconcreditoapp.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.josevalenzuela.prospectosconcreditoapp.Presenter.AgergarProspectoPresenter;
import com.josevalenzuela.prospectosconcreditoapp.R;
import com.josevalenzuela.prospectosconcreditoapp.adapters.NuevoDocumentoAdapter;
import com.josevalenzuela.prospectosconcreditoapp.contracts.AgregarProspectoContract;
import com.josevalenzuela.prospectosconcreditoapp.models.Prospecto;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class AgegarProspectoFragmet extends Fragment implements AgregarProspectoContract.View {

    private ImageView imageView;
    private int docIndex = 0;
    private Uri img_uri;
    private List<Bitmap> bitmaps;
    private List<String> docsEncodedList;
    private AgregarProspectoContract.Presenter presenter;
    private Fragment lsitadoFragment;
    //componentes iterfaz
    private TextInputEditText nombreEditTxt, primerAppEditTxt, segundoAppEditTxt, telefonoEditTxt, rfcEditTxt, coloniaEditTxt, calleEditTxt, numeroEditTxt, codigoPostalEditTxt;
    private RecyclerView recyclerView;
    private LinearLayout layoutAddDoc;
    private ImageView enviarBtn;
    private final int REQUEST_CODE = 1;


    public AgegarProspectoFragmet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agegar_prospecto, container, false);
        bitmaps = new ArrayList<>();
        docsEncodedList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerViewAgregarDocs);
        imageView = view.findViewById(R.id.agregarDocId);

        nombreEditTxt = view.findViewById(R.id.nuevoProspectoNombreId);
        primerAppEditTxt = view.findViewById(R.id.nuevoProspectoAppPaternoETxtId);
        segundoAppEditTxt = view.findViewById(R.id.nuevoProspectoAppMaternoId);
        rfcEditTxt = view.findViewById(R.id.nuevoProspectoRfcId);
        telefonoEditTxt = view.findViewById(R.id.nuevoProspectoTelefonoId);
        calleEditTxt = view.findViewById(R.id.nuevoProspectoCalleId);
        coloniaEditTxt = view.findViewById(R.id.nuevoProspectoColoniaId);
        codigoPostalEditTxt = view.findViewById(R.id.nuevoProspectoCodPostalId);
        numeroEditTxt = view.findViewById(R.id.nuevoProspectoNumeroCasaId);
        enviarBtn = view.findViewById(R.id.saveProspectoId);
        layoutAddDoc = view.findViewById(R.id.recyclerProspectoViewId);

        presenter = new AgergarProspectoPresenter(this);
        lsitadoFragment = new ListadoProspectosFragment();


        //Validar formulario y agregar prospecto
        enviarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.validarFormulario(
                        nombreEditTxt.getText().toString(), primerAppEditTxt.getText().toString(), segundoAppEditTxt.getText().toString(),
                        calleEditTxt.getText().toString(), numeroEditTxt.getText().toString(), coloniaEditTxt.getText().toString(),
                        codigoPostalEditTxt.getText().toString(), telefonoEditTxt.getText().toString(), rfcEditTxt.getText().toString()
                )){
                        presenter.agregarProspecto(docsEncodedList, nombreEditTxt.getText().toString(), primerAppEditTxt.getText().toString(), segundoAppEditTxt.getText().toString(),
                                calleEditTxt.getText().toString(), numeroEditTxt.getText().toString(), coloniaEditTxt.getText().toString(),
                                codigoPostalEditTxt.getText().toString(), telefonoEditTxt.getText().toString(), rfcEditTxt.getText().toString());

                }
            }
        });




        //Agregar documento
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setColorFilter(getResources().getColor(R.color.colorAccent));
                Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (camara.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(camara, REQUEST_CODE);
                }
                docIndex++;
            }
        });
        return view;
    }

    private void addDoc(){
        NuevoDocumentoAdapter adapter = new NuevoDocumentoAdapter(bitmaps);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (REQUEST_CODE == requestCode){
            assert data != null;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG,40,bos);
            byte[] bb = bos.toByteArray();
            String encode = Base64.encodeToString(bb, Base64.DEFAULT);
            docsEncodedList.add(encode);
            bitmaps.add(photo);
        }

        addDoc();
    }

    @Override
    public void prospectoAgregadoSucces(Prospecto prospecto) {
        Toast.makeText(getContext(), prospecto.getNombre() + " agregado satisfactoriamente", Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.contenedorFragmentos, lsitadoFragment).commit();
    }

    @Override
    public void mostrarErrorNombre(String error) {
        nombreEditTxt.setError(error);
    }

    @Override
    public void mostrarErrorAppPaterno(String error) {
        primerAppEditTxt.setError(error);
    }

    @Override
    public void mostrarErrorAppMaterno(String error) {

    }

    @Override
    public void mostrarErrorRFC(String error) {
        rfcEditTxt.setError(error);
    }

    @Override
    public void mostrarErrorTelefono(String error) {
        telefonoEditTxt.setError(error);
    }

    @Override
    public void mostrarErrorCodigoPostal(String error) {
        codigoPostalEditTxt.setError(error);
    }

    @Override
    public void mostrarErrorCalle(String error) {
        calleEditTxt.setError(error);
    }

    @Override
    public void mostrarErrorColonia(String error) {
        coloniaEditTxt.setError(error);
    }

    @Override
    public void mostrarErrorNumero(String error) {
        numeroEditTxt.setError(error);
    }

    @Override
    public void mostrarAgregarProspectoError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.contenedorFragmentos, lsitadoFragment).commit();
    }
}