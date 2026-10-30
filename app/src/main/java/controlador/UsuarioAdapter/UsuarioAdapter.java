package controlador.UsuarioAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.franco.asistenciaiquique.R;

import java.util.ArrayList;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private ArrayList<String> listaCorreos;
    private OnEliminarClickListener listener;

    // Interfaz para avisarle a la Activity cuando se toque el basurero
    public interface OnEliminarClickListener {
        void onEliminarClick(String correo);
    }

    public UsuarioAdapter(ArrayList<String> listaCorreos, OnEliminarClickListener listener) {
        this.listaCorreos = listaCorreos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuarios, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        String correo = listaCorreos.get(position);
        holder.tvCorreo.setText(correo);

        holder.btnEliminar.setOnClickListener(v -> {
            listener.onEliminarClick(correo);
        });
    }

    @Override
    public int getItemCount() {
        return listaCorreos.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvCorreo;
        ImageButton btnEliminar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCorreo = itemView.findViewById(R.id.tv_correo_usuario);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar_usuario);
        }
    }
}