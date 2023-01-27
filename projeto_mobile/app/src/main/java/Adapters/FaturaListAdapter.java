package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projeto.R;

import java.util.ArrayList;

import Models.Fatura;

public class FaturaListAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Fatura> Faturas;

    public FaturaListAdapter(Context context, ArrayList<Fatura> faturas) {
        this.context = context;
        Faturas = faturas;
    }

    @Override
    public int getCount() {
        return Faturas.size();
    }

    @Override
    public Object getItem(int i) {
        return Faturas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Faturas.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(inflater == null)
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = inflater.inflate(R.layout.item_fatura, null);

        FaturaListAdapter.ViewHolderLista viewHolderLista = (FaturaListAdapter.ViewHolderLista) view.getTag();

        if(viewHolderLista == null){
            viewHolderLista = new ViewHolderLista(view);
            view.setTag(viewHolderLista);
        }

        viewHolderLista.update(Faturas.get(i));
        return view;
    }

    private class ViewHolderLista{

        private TextView tvFatura, tvTotal, tvData, tvDesconto ;

        public ViewHolderLista(View view){
            tvDesconto = view.findViewById(R.id.tvTotalDesconto);
            tvFatura = view.findViewById(R.id.tvFaturaId);
            tvTotal = view.findViewById(R.id.tvTotal);
            tvData = view.findViewById(R.id.tvDataFatura);
        }

        public void update(Fatura fatura){
            tvDesconto.setText(String.format("Desconto Total: %.2f€", fatura.getValorDesconto()));
            tvFatura.setText(String.format("Fatura Nº%s", String.valueOf(fatura.getId())));
            tvTotal.setText(String.format("Total: %.2f€", fatura.getValorTotal()));
            tvData.setText(String.format("Compra efetuada a %s", fatura.getData()));
        }
    }
}
