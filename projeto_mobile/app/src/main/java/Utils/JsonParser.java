package Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Models.Carrinho;
import Models.Categoria;
import Models.Dados;
import Models.DBHelper;
import Models.Fatura;
import Models.LinhaFatura;
import Models.Marca;
import Models.Produto;
import Models.Signup;

public class JsonParser {

    //region Produto
    public static ArrayList<Produto> parserJsonProdutos(JSONArray response){
        ArrayList<Produto> produtos = new ArrayList<>();
        try {

            for (int i=0; i<response.length(); i++){
                JSONObject jsonObject = (JSONObject) response.getJSONObject(i);
                JSONObject produto = jsonObject.getJSONObject("produto");
                boolean inStock = jsonObject.getBoolean("stock");
                int id= produto.getInt("id");
                String nome = produto.getString("nome");
                String imagem = produto.getString("imagem");
                String precoString = produto.getString("preco");
                double preco= Double.parseDouble(precoString);
                Produto produtoAux = new Produto(id,nome,null,imagem,preco, null ,null,inStock);
                produtos.add(produtoAux);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return produtos;
    }


    public static Produto parserJsonProduto(JSONObject response){
        Produto produtoAux = null;
        try {
            JSONObject produto = response.getJSONObject("produto");
            boolean inStock = response.getBoolean("stock");
            int id= produto.getInt("id");
            String nome = produto.getString("nome");
            String imagem = produto.getString("imagem");
            String precoString = produto.getString("preco");
            String descricao = produto.getString("descricao");
            String marca = produto.getString("id_Marca");
            String referencia = produto.getString("referencia");
            double preco= Double.parseDouble(precoString);
            produtoAux = new Produto(id,nome,descricao,imagem,preco, marca ,referencia,inStock);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return produtoAux;
    }

    //endregion

    //region Carrinho

    public static ArrayList<Carrinho> parserJsonCarrinho (JSONArray response){
        ArrayList<Carrinho> carrinhos = new ArrayList<>();
        try {
            for (int i=0; i<response.length(); i++){
                JSONObject jsonObject = (JSONObject) response.getJSONObject(i);
                JSONObject produto = jsonObject.getJSONObject("produto");
                int id= produto.getInt("id");
                String nome = produto.getString("nome");
                String imagem = produto.getString("imagem");
                String precoString = produto.getString("preco");
                double preco= Double.parseDouble(precoString);
                int quantidade = jsonObject.getInt("quantidade");
                boolean inStock = jsonObject.getBoolean("stock");
                Produto produtoAux = new Produto(id,nome,null,imagem,preco, null ,null,inStock);
                Carrinho carrinho = new Carrinho(produtoAux,quantidade);
                carrinhos.add(carrinho);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return carrinhos;
    }

    public static Dados parserJsonDados(JSONObject response){
        Dados dadosAux = null;
        try {
            JSONObject dados = response;
            String nome = dados.getString("nome");
            String telefone = dados.getString("telefone");
            String nif = dados.getString("nif");
            String morada = dados.getString("morada");
            String codPostal = dados.getString("codPostal");
            dadosAux = new Dados(nome,telefone,nif,morada, codPostal);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return dadosAux;
    }
    //endregion

    //region Fatura

    public static Fatura parserJsonFatura(JSONObject response){
        Fatura auxFatura = null;
        try {
            int id = response.getInt("id");
            String data = response.getString("dataFatura");
            double valorTotal = Double.parseDouble(response.getString("valorTotal"));
            double ivaTotal = Double.parseDouble(response.getString("valorIva"));
            double subtotal = Double.parseDouble(response.getString("subtotal"));
            double valorDesconto = Double.parseDouble(response.getString("valorDesconto"));
            String morada = response.getString("morada");
            String nif = response.getString("nif");
            auxFatura = new Fatura(id,nif,morada,data,valorTotal,ivaTotal, valorDesconto, subtotal);
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }
        return auxFatura;
    }

    public static ArrayList<LinhaFatura> parserJsonLinhas(JSONArray response){
        ArrayList<LinhaFatura> linhasFatura = new ArrayList<>();
        try {
            for (int i=0; i<response.length(); i++){
                JSONObject linha = (JSONObject) response.getJSONObject(i);
                int id = linha.getInt("id");
                int idFatura = linha.getInt("id_Fatura");
                String nome = linha.getString("produto_nome");
                String referencia = linha.getString("produto_referencia");
                double iva = Double.parseDouble(linha.getString("valorIva"));
                double preco = Double.parseDouble(linha.getString("valor"));
                int quantidade = linha.getInt("quantidade");
                LinhaFatura linhaFatura = new LinhaFatura(id, idFatura, nome, referencia, quantidade, preco, iva);
                linhasFatura.add(linhaFatura);
            }
    }catch (JSONException e){
        System.out.println(e.getMessage());
    }
        return linhasFatura;
    }
    //endregion

    //region Filter

    public static ArrayList<Categoria> parserJsonCategorias(JSONArray response){
        ArrayList<Categoria> categorias = new ArrayList<>();
        try {
            for (int i=0; i<response.length(); i++){
                JSONObject jsonObject = (JSONObject) response.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String nome = jsonObject.getString("nome");
                Categoria categoria = new Categoria(id,nome);
                categorias.add(categoria);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return categorias;
    }

    public static ArrayList<Marca> parserJsonMarcas(JSONArray response){
        ArrayList<Marca> marcas = new ArrayList<>();
        try {
            for (int i=0; i<response.length(); i++){
                JSONObject jsonObject = (JSONObject) response.getJSONObject(i);
                String nome = jsonObject.getString("nome");
                Marca marca = new Marca(i+1,nome);
                marcas.add(marca);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return marcas;
    }

    //endregion

    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }
}
