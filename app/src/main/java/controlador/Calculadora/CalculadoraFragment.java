package controlador.Calculadora;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.util.TypedValue;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.franco.asistenciaiquique.R;

public class CalculadoraFragment extends Fragment {

    private EditText tvResultado;
    private String inputActual = "";
    private String operador = "";
    private double primerOperando = 0;
    private String textoPrimerOperando = ""; // Recuerda el primer número para mostrarlo

    // VARIABLES PARA EL HISTORIAL
    private ScrollView panelHistorial;
    private TextView tvHistorial;
    private String memoriaHistorial = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calculadora_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Enlazamos la pantalla (EditText) y bloqueamos el teclado del celular
        tvResultado = view.findViewById(R.id.tv_resultado);
        tvResultado.setShowSoftInputOnFocus(false);

        // --- INICIO CONFIGURACIÓN DEL HISTORIAL ---
        panelHistorial = view.findViewById(R.id.panel_historial);
        tvHistorial = view.findViewById(R.id.tv_historial);
        Button btnHistorial = view.findViewById(R.id.button_Historial);

        // Hacer que el botón muestre u oculte el panel del historial
        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(v -> {
                if (panelHistorial.getVisibility() == View.GONE) {
                    panelHistorial.setVisibility(View.VISIBLE);
                } else {
                    panelHistorial.setVisibility(View.GONE);
                }
            });
        }
        // --- FIN CONFIGURACIÓN DEL HISTORIAL ---

        // 1. Configurar botones de Números (0-9)
        int[] idBotonesNumeros = {
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
                R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
                R.id.button_8, R.id.button_9
        };

        View.OnClickListener listenerNumeros = v -> {
            Button b = (Button) v;
            if (inputActual.equals("Error")) inputActual = "";

            inputActual += b.getText().toString();
            actualizarPantalla(inputActual);
        };

        for (int id : idBotonesNumeros) {
            Button btn = view.findViewById(id);
            if (btn != null) btn.setOnClickListener(listenerNumeros);
        }

        // 2. Configurar botón Punto Decimal
        Button btnPunto = view.findViewById(R.id.button_punto);
        if (btnPunto != null) {
            btnPunto.setOnClickListener(v -> {
                if (!inputActual.contains(".")) {
                    if (inputActual.isEmpty()) inputActual = "0.";
                    else inputActual += ".";
                    actualizarPantalla(inputActual);
                }
            });
        }

        // 3. Configurar Operadores (+, -, *, /)
        int[] idBotonesOperadores = {
                R.id.button_suma, R.id.button_resta, R.id.button_mult, R.id.button_div
        };

        View.OnClickListener listenerOperadores = v -> {
            Button b = (Button) v;
            if (!inputActual.isEmpty() && !inputActual.equals("Error") && !inputActual.equals("-")) {
                // Guardamos el número actual en la memoria visual y matemática
                textoPrimerOperando = inputActual;
                primerOperando = Double.parseDouble(inputActual);
                operador = b.getText().toString();
                inputActual = ""; // Limpiamos para esperar el segundo número
                actualizarPantalla(inputActual);

            } else if (inputActual.isEmpty() && !textoPrimerOperando.isEmpty()) {
                // Si ya había un operador, pero cambiaron de opinión (ej: tocó + pero quería *)
                operador = b.getText().toString();
                actualizarPantalla(inputActual);
            }
        };

        for (int id : idBotonesOperadores) {
            Button btn = view.findViewById(id);
            if (btn != null) btn.setOnClickListener(listenerOperadores);
        }

        // 4. Configurar botón Igual (=)
        Button btnIgual = view.findViewById(R.id.button_igual);
        if (btnIgual != null) {
            btnIgual.setOnClickListener(v -> {
                if (!inputActual.isEmpty() && !operador.isEmpty() && !inputActual.equals("Error") && !inputActual.equals("-")) {
                    double segundoOperando = Double.parseDouble(inputActual);
                    double resultado = 0;

                    // Realizar la matemática (Arreglé los signos - y * que faltaban)
                    switch (operador) {
                        case "+": resultado = primerOperando + segundoOperando; break;
                        case "-": resultado = primerOperando - segundoOperando; break;
                        case "*": resultado = primerOperando * segundoOperando; break;
                        case "/":
                            if (segundoOperando != 0) {
                                resultado = primerOperando / segundoOperando;
                            } else {
                                inputActual = "Error";
                                operador = "";
                                textoPrimerOperando = "";
                                actualizarPantalla(inputActual);
                                return;
                            }
                            break;
                    }

                    // Guardar el número original en texto (por si tenía decimales o no)
                    String textoSegundoOperando = (segundoOperando == (long) segundoOperando) ?
                            String.format("%d", (long) segundoOperando) : String.valueOf(segundoOperando);

                    // Formatear el resultado final
                    if (resultado == (long) resultado) {
                        inputActual = String.format("%d", (long) resultado);
                    } else {
                        inputActual = String.valueOf(resultado);
                    }

                    // --- GUARDAR EN EL HISTORIAL ---
                    // Se arma la operación: Ej. "5 + 5 = 10"
                    String operacionCompleta = textoPrimerOperando + " " + operador + " " + textoSegundoOperando + " = " + inputActual;
                    memoriaHistorial += operacionCompleta + "\n\n"; // Se suma a lo que ya había con un salto de línea
                    tvHistorial.setText(memoriaHistorial);          // Se envía al panel oscuro
                    // -------------------------------

                    // Limpiar memorias temporales para mostrar solo el resultado final
                    operador = "";
                    textoPrimerOperando = "";
                    actualizarPantalla(inputActual);
                }
            });
        }

        // 5. Configurar botón AC (Borrar Todo)
        Button btnAc = view.findViewById(R.id.button_ac);
        if (btnAc != null) {
            btnAc.setOnClickListener(v -> {
                inputActual = "";
                operador = "";
                textoPrimerOperando = "";
                primerOperando = 0;
                actualizarPantalla("");
            });
        }

        // 6. Configurar botón C (Borrar un dígito)
        Button btnC = view.findViewById(R.id.button_del_num_act);
        if (btnC != null) {
            btnC.setOnClickListener(v -> {
                if (!inputActual.isEmpty() && !inputActual.equals("Error")) {
                    // Borramos el último número que estábamos tecleando
                    inputActual = inputActual.substring(0, inputActual.length() - 1);
                    actualizarPantalla(inputActual);
                } else if (inputActual.isEmpty() && !operador.isEmpty()) {
                    // Si presionan borrar y solo estaba el operador (+), borramos el operador
                    // y devolvemos el primer número para seguir editándolo
                    inputActual = textoPrimerOperando;
                    operador = "";
                    textoPrimerOperando = "";
                    actualizarPantalla(inputActual);
                }
            });
        }

        // 7. Configurar botón Porcentaje (%)
        Button btnPorcen = view.findViewById(R.id.button_porcen);
        if (btnPorcen != null) {
            btnPorcen.setOnClickListener(v -> {
                if (!inputActual.isEmpty() && !inputActual.equals("Error") && !inputActual.equals("-")) {
                    double valor = Double.parseDouble(inputActual) / 100;
                    if (valor == (long) valor) {
                        inputActual = String.format("%d", (long) valor);
                    } else {
                        inputActual = String.valueOf(valor);
                    }
                    actualizarPantalla(inputActual);
                }
            });
        }

        // 8. Configurar botón +/- (Usa el ID del paréntesis)
        Button btnParen = view.findViewById(R.id.button_parentesis);
        if (btnParen != null) {
            btnParen.setOnClickListener(v -> {
                if (inputActual.equals("Error")) inputActual = "";

                if (inputActual.isEmpty()) {
                    inputActual = "-";
                } else if (inputActual.equals("-")) {
                    inputActual = "";
                } else {
                    if (inputActual.startsWith("-")) {
                        inputActual = inputActual.substring(1);
                    } else {
                        inputActual = "-" + inputActual;
                    }
                }
                actualizarPantalla(inputActual);
            });
        }
    }

    /**
     * Construye la vista de la pantalla mostrando la operación completa.
     */
    private void actualizarPantalla(String texto) {
        String textoAMostrar = "";

        // Aquí es donde se juntan el número, el signo (+, -) y el segundo número
        if (!textoPrimerOperando.isEmpty() && !operador.isEmpty()) {
            textoAMostrar = textoPrimerOperando + " " + operador + " " + texto;
        } else {
            textoAMostrar = texto;
        }

        int cantidadCaracteres = textoAMostrar.length();

        if (cantidadCaracteres <= 8) {
            tvResultado.setTextSize(TypedValue.COMPLEX_UNIT_SP, 54); // Tamaño gigante normal
        } else if (cantidadCaracteres <= 12) {
            tvResultado.setTextSize(TypedValue.COMPLEX_UNIT_SP, 42); // Se achica un poco
        } else if (cantidadCaracteres <= 16) {
            tvResultado.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30); // Se achica más
        } else {
            tvResultado.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26); // Tamaño mínimo
        }

        // Mostrar "0" si está vacío, o el texto final
        if (textoAMostrar.isEmpty() || textoAMostrar.trim().isEmpty()) {
            tvResultado.setText("0");
            tvResultado.setSelection(1);
        } else {
            tvResultado.setText(textoAMostrar);
            tvResultado.setSelection(textoAMostrar.length());
        }
    }
}