package controlador.Calculadora;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp.R;

public class CalculadoraFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla la vista correspondiente a la calculadora
        return inflater.inflate(R.layout.calculadora_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Aca están los botones principales de numero
        Button boton1 = view.findViewById(R.id.button_1);
        Button boton2 = view.findViewById(R.id.button_2);
        Button boton3 = view.findViewById(R.id.button_3);
        Button boton4 = view.findViewById(R.id.button_4);
        Button boton5 = view.findViewById(R.id.button_5);
        Button boton6 = view.findViewById(R.id.button_6);
        Button boton7 = view.findViewById(R.id.button_7);
        Button boton8 = view.findViewById(R.id.button_8);
        Button boton9 = view.findViewById(R.id.button_9);
        Button boton0 = view.findViewById(R.id.button_0);

        //aca van los botones de los demas botones
        Button botonpunto = view.findViewById(R.id.button_punto);



    }
}