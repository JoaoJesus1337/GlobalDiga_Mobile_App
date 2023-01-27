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

import Models.Produto;
import Utils.Public;

public class ProductGridAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Produto> Produtos;

    public ProductGridAdapter(Context context, ArrayList<Produto> produtos) {
        this.context = context;
        Produtos = produtos;
    }

    @Override
    public int getCount() {
        return Produtos.size();
    }

    @Override
    public Object getItem(int i) {
        return Produtos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Produtos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(inflater == null)
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = inflater.inflate(R.layout.item_product_grid, null);

        ViewHolderLista viewHolderLista = (ViewHolderLista) view.getTag();

        if(viewHolderLista == null){
            viewHolderLista = new ViewHolderLista(view);
            view.setTag(viewHolderLista);
        }

        viewHolderLista.update(Produtos.get(i));

        return view;
    }

    private class ViewHolderLista{

        private ImageView imgCapa;
        private TextView tvNome, tvDetalhes, tvStock, tvPreco;

        public ViewHolderLista(View view) {
            imgCapa = view.findViewById(R.id.imageCapa);
            tvNome = view.findViewById(R.id.tvNomeDisplay);
            tvStock = view.findViewById(R.id.tvStock);
            tvPreco = view.findViewById(R.id.tvPrecoTotal);


        }

        public void update(Produto produto){
            tvNome.setText(produto.getNome());
            if(produto.isEmStock()) {
                tvStock.setText(R.string.inStock);
                tvStock.setTextColor(Color.parseColor("#048000"));
            } else {
                tvStock.setText(R.string.outStock);
                tvStock.setTextColor(Color.parseColor("#b00200"));
            }
            tvPreco.setText(String.format("%.2f€", produto.getPreco()));
            Glide.with(context)
                    .load(Public.imgURL +produto.getCapa())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);
        }
    }
}
