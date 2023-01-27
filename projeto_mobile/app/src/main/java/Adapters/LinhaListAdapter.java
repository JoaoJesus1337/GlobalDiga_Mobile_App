package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projeto.R;

import java.util.ArrayList;

import Models.LinhaFatura;

public class LinhaListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<LinhaFatura> Linhas;

    public LinhaListAdapter(Context context, ArrayList<LinhaFatura> linhas) {
        this.context = context;
        Linhas = linhas;
    }

    @Override
    public int getCount() {
        return Linhas.size();
    }

    @Override
    public Object getItem(int i) {
        return Linhas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Linhas.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.item_linha, null);

        LinhaListAdapter.ViewHolderLista viewHolderLista = (LinhaListAdapter.ViewHolderLista) view.getTag();

        if (viewHolderLista == null) {
            viewHolderLista = new ViewHolderLista(view);
            view.setTag(viewHolderLista);
        }

        viewHolderLista.update(Linhas.get(i));
        return view;
    }

    private class ViewHolderLista{

        private TextView tvNome, tvReferencia, tvQuantidade, tvPrecoTotal, tvIvaTotal;

        public ViewHolderLista(View view){
            tvNome = view.findViewById(R.id.tvNomeDisplay);
            tvReferencia = view.findViewById(R.id.tvReferencia);
            tvQuantidade = view.findViewById(R.id.tvQuantidade);
            tvPrecoTotal = view.findViewById(R.id.tvPrecoTotal);
            tvIvaTotal = view.findViewById(R.id.tvIvaTotal);
        }

        public void update(LinhaFatura Linha){
            tvNome.setText(Linha.getProduto_nome());
            tvReferencia.setText(Linha.getProduto_referencia());
            tvQuantidade.setText(String.format("Qtd %s", String.valueOf(Linha.getQuantidade())));
            tvPrecoTotal.setText(String.format("%.2f€", Linha.getValor()));
            tvIvaTotal.setText(String.format("%.2f€", Linha.getValorIva()));
        }

        }

}
