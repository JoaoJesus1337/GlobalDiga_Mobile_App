package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.projeto.R;

import java.util.ArrayList;

import Models.Carrinho;
import Utils.Public;

public class CartListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Carrinho> Carrinhos;

    public CartListAdapter(Context context, ArrayList<Carrinho> carrinhos) {
        this.context = context;
        Carrinhos = carrinhos;
    }

    @Override
    public int getCount() {
        return Carrinhos.size();
    }

    @Override
    public Object getItem(int i) {
        return Carrinhos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Carrinhos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(inflater == null)
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = inflater.inflate(R.layout.item_cart, null);

        CartListAdapter.ViewHolderLista viewHolderLista = (CartListAdapter.ViewHolderLista) view.getTag();

        if(viewHolderLista == null){
            viewHolderLista = new CartListAdapter.ViewHolderLista(view);
            view.setTag(viewHolderLista);
        }

        viewHolderLista.update(Carrinhos.get(i));

        return view;
    }

    private class ViewHolderLista{

        private ImageView imgCapa;
        private TextView tvNome,  tvStock, tvPrecoTotal, tvPreco, tvQuantidade;

        public ViewHolderLista(View view) {
            imgCapa = view.findViewById(R.id.imageCapa);
            tvNome = view.findViewById(R.id.tvNomeDisplay);
            tvStock = view.findViewById(R.id.tvStock);
            tvPrecoTotal = view.findViewById(R.id.tvPrecoTotal);
            tvQuantidade = view.findViewById(R.id.tvQuantidade);
            tvPreco = view.findViewById(R.id.tvPreco);


        }

        public void update(Carrinho carrinho){
            tvNome.setText(carrinho.getProduto().getNome());
            double preco = carrinho.getProduto().getPreco() * carrinho.getQuantidade();
            tvPrecoTotal.setText(String.format("%s€", String.format("%.2f", preco)));
            tvQuantidade.setText(String.format("Qtd: %s", String.valueOf(carrinho.getQuantidade())));
            tvPreco.setText(String.format("%s€", String.format("%.2f", carrinho.getProduto().getPreco())));
            if(carrinho.getProduto().isEmStock()){
                tvStock.setText(R.string.inStock);
                tvStock.setTextColor(Color.parseColor("#048000"));
            }else{
                tvStock.setText(R.string.outStock);
                tvStock.setTextColor(Color.parseColor("#b00200"));
            }
            Glide.with(context)
                    .load(Public.imgURL+ carrinho.getProduto().getCapa())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);
        }
    }

}

