package Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projeto.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Listeners.CartListener;
import Listeners.DadosListener;
import Listeners.DetailsListener;
import Listeners.FaturasListener;
import Listeners.FilterListener;
import Listeners.LinhasListener;
import Listeners.ProdutoListener;
import Utils.JsonParser;
import Utils.Public;

public class Singleton {
    private static Singleton single_instance = null;
    private ArrayList<Produto> produtos;
    private static RequestQueue volleyQueue = null;
    private ProdutoListener produtoListener;
    private CartListener cartListener;
    private DetailsListener detailsListener;
    private DadosListener dadosListener;
    private FaturasListener faturasListener;
    private LinhasListener linhasListener;
    private FilterListener filterListener;
    private static DBHelper dbHelper;

    public static synchronized Singleton getInstance(Context context) {
        if (single_instance == null) {
            single_instance = new Singleton();
            volleyQueue = Volley.newRequestQueue(context);
            dbHelper = new DBHelper(context);
        }
        return single_instance;
    }

    private Singleton() {
        produtos = new ArrayList<>();
    }

    //region Listeners

    public void setProdutoListener(ProdutoListener produtoListener) {
        this.produtoListener = produtoListener;
    }

    public void setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    public void setDetailsListener(DetailsListener detailsListener) {
        this.detailsListener = detailsListener;
    }

    public void setDadosListener(DadosListener dadosListener) {
        this.dadosListener = dadosListener;
    }
    public void setFaturasListener(FaturasListener faturasListener) {
        this.faturasListener = faturasListener;
    }

    public void setLinhasListener(LinhasListener linhasListener) {
        this.linhasListener = linhasListener;
    }

    public void setCartListener(CartListener cartListener) {
        this.cartListener = cartListener;
    }

    //endregion Listeners

    //region Produto
    public void getAllProdutosAPI(final Context context) {
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Public.apiURL + "/produtos", null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    produtos = JsonParser.parserJsonProdutos(response);
                    if (produtoListener != null)
                        produtoListener.onRefreshProdutos(produtos);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage()+"", Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(jsonArrayRequest);
        }
    }

    public void getProdutoAPI(final Context context, int id) {
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Public.apiURL + "/produtos/" + id, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Produto produto = JsonParser.parserJsonProduto(response);
                    if (detailsListener != null)
                        detailsListener.onRefreshDetails(produto);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(jsonObjectRequest);
        }
    }

    //endregion produtos

    //region User
    public void loginUserAPI(final Context context, final String credentials){
        if (!JsonParser.isConnected(context)){
            BetterToast(context, context.getString(R.string.sem_internet));
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Public.apiURL + "/user/login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        editor.putString(Public.TOKEN, jsonObject.getString("response"));
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    BetterToast(context, "Login efetuado com sucesso");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    BetterToast(context, "Username ou Password errados");
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + credentials);
                    return headers;
                }};
            volleyQueue.add(stringRequest);
        }
    }

    public void createUserAPI(final Context context, final Signup user){
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Public.apiURL + "/user/register", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    BetterToast(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected java.util.Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", user.getUsername());
                    params.put("email", user.getEmail());
                    params.put("password", user.getPassword());
                    params.put("nome", user.getNome());
                    params.put("codPostal", user.getCodPostal());
                    params.put("telefone", user.getTelefone());
                    params.put("nif", user.getNif());
                    params.put("morada", user.getMorada());
                    return params;
                }
            };
            volleyQueue.add(stringRequest);
        }

    }

    public void getDadosAPI(final Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
        if(!JsonParser.isConnected(context))
        {
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }
        else
        {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Public.apiURL + "/dados?access-token=" + sharedPreferences.getString(Public.TOKEN,null), null, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    Dados dado = JsonParser.parserJsonDados(response);

                    if (dadosListener != null)
                    {
                        dadosListener.onRefreshDados(dado);
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(jsonObjectRequest);
        }
    }

    public void updateDadosAPI(final Context context, final Dados dados){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, Public.apiURL + "/dados/update?access-token="+ sharedPreferences.getString(Public.TOKEN,null), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    BetterToast(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected java.util.Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nome", dados.getNome());
                    params.put("codPostal", dados.getCodPostal());
                    params.put("telefone", dados.getTelefone());
                    params.put("nif", dados.getNif());
                    params.put("morada", dados.getMorada());
                    return params;
                }
            };
            volleyQueue.add(stringRequest);
        }

    }

    public void getLojaMaisPerto(final Context context, final double longitude, final double latitude,final int idProduto){

        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Public.apiURL + "/produtos/location" ,new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    BetterToast(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage()+"", Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected java.util.Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("latitude", String.valueOf(latitude));
                    params.put("longitude", String.valueOf(longitude));
                    params.put("idProduto", String.valueOf(idProduto));
                    return params;
                }
            };
            volleyQueue.add(stringRequest);
        }

    }
    //endregion User

    //region Cart

    public void addCartAPI(final Context context, final int idProduto, final int quantidade){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, Public.apiURL + "/carrinho/create?access-token="+ sharedPreferences.getString(Public.TOKEN, null),
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    BetterToast(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //System.out.println(error.getMessage());
                }
            }){
                @Override
                protected java.util.Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_Produto", String.valueOf(idProduto));
                    params.put("quantidade", String.valueOf(quantidade));
                    return params;
                }
            };
            volleyQueue.add(stringRequest);
        }
    }

   public void viewCartAPI(final Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(Public.COUPON)){
            sharedPreferences.edit().remove(Public.COUPON).apply();
        }
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Public.apiURL + "/carrinho?access-token="+ sharedPreferences.getString(Public.TOKEN, null), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Carrinho> carrinhos = JsonParser.parserJsonCarrinho(response);
                    if (cartListener != null)
                        cartListener.onRefreshCart(carrinhos);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(jsonArrayRequest);
        }
    }

    public void clearCartAPI(final Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, Public.apiURL + "/carrinho/delete?access-token="+ sharedPreferences.getString(Public.TOKEN, null), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    BetterToast(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(stringRequest);
        }
    }

    public void removeCartAPI(final Context context, final int idProduto){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Public.apiURL + "/carrinho/remove?access-token="+ sharedPreferences.getString(Public.TOKEN, null), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    BetterToast(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected java.util.Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_Produto", String.valueOf(idProduto));
                    return params;
                }
            };
            volleyQueue.add(stringRequest);
        }
    }

    public void checkCouponApi(final Context context, final String codigo){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Public.apiURL + "/carrinho/coupon?access-token="+ sharedPreferences.getString(Public.TOKEN, null), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String valid = jsonObject.getString("response");
                        //Doesn't seem to be able to validade words in portuguese so I'm using this workaround
                        if(valid.length() == 13){
                            sharedPreferences.edit().putString(Public.COUPON, codigo).apply();
                        }
                        else{
                            BetterToast(context, response);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    BetterToast(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "asdasd",Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected java.util.Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("promoCode", codigo);
                    return params;
                }
            };
            volleyQueue.add(stringRequest);
        }
    }

    public void buyCartAPI(final Context context, final String deliveryAddress){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Public.apiURL + "/carrinho/buy?access-token="+ sharedPreferences.getString(Public.TOKEN, null), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    BetterToast(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected java.util.Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("adress", deliveryAddress);
                    if (sharedPreferences.contains(Public.COUPON))
                        params.put("promoCode", sharedPreferences.getString(Public.COUPON, null));
                    return params;
                }
            };
            volleyQueue.add(stringRequest);
        }
    }

    public void updateCartAPI(final Context context, final int idProduto, final int quantidade){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
        if(!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, Public.apiURL + "/carrinho/update?access-token="+ sharedPreferences.getString(Public.TOKEN, null), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    BetterToast(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected java.util.Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_Produto", String.valueOf(idProduto));
                    params.put("quantidade", String.valueOf(quantidade));
                    return params;
                }
            };
            volleyQueue.add(stringRequest);
        }
    }
    //endregion Cart

    //region Faturas

    public void getFaturasAPI(final Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Public.SHARED_FILE, Context.MODE_PRIVATE);
        if(!JsonParser.isConnected(context) && sharedPreferences.contains(Public.TOKEN)){
            faturasListener.onRefreshFaturas(dbHelper.getAllFaturasDB());
        }else {
            dbHelper.removeAllFaturas();
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Public.apiURL + "/faturas?access-token="+ sharedPreferences.getString(Public.TOKEN, null), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            JSONObject fatura = jsonObject.getJSONObject("fatura");
                            JSONArray linhas = jsonObject.getJSONArray("linhasFatura");
                            Fatura auxFatura = JsonParser.parserJsonFatura(fatura);
                            ArrayList<LinhaFatura> auxLinhas = JsonParser.parserJsonLinhas(linhas);
                            dbHelper.addFaturaDB(auxFatura);
                            dbHelper.addLinhasDB(auxLinhas);
                        }
                        if (faturasListener != null) {
                            faturasListener.onRefreshFaturas(dbHelper.getAllFaturasDB());
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(jsonArrayRequest);
        }
    }

    public void getFaturaDB(final Context context, final int id){
        if (linhasListener != null){
            linhasListener.onRefreshLinhasFatura(dbHelper.getAllLinhasFaturaDB(id), dbHelper.getFaturaDB(id));
        }
    }
    //endregion Faturas

    //region Filters


    public void getAllFiltersAPI(final Context context){
        if (!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            dbHelper.removeAllFilters();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Public.apiURL + "/filters", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ArrayList<Categoria> categorias = null;
                    ArrayList<Marca> marcas = null;
                    try {
                        JSONArray categoriasJson = response.getJSONArray("categorias");
                        JSONArray marcasJson = response.getJSONArray("marcas");
                        categorias = JsonParser.parserJsonCategorias(categoriasJson);
                        marcas = JsonParser.parserJsonMarcas(marcasJson);
                        if (filterListener != null)
                            filterListener.onRefreshFilters(categorias, marcas);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(jsonObjectRequest);
        }
    }

    public void getALlFiltersDB(final Context context){
        ArrayList<Categoria> categorias = dbHelper.getAllCategoriasDB();
        ArrayList<Marca> marcas = dbHelper.getAllMarcasDB();
        if (categorias.size() == 0 || marcas.size() == 0){
            getAllFiltersAPI(context);
        }else {
            if (filterListener != null)
                filterListener.onRefreshFilters(categorias, marcas);
        }
    }

    public void getQueryProdutosAPI(final Context context, @Nullable final String query){
        if (!JsonParser.isConnected(context)){
            Toast.makeText(context, context.getString(R.string.sem_internet), Toast.LENGTH_LONG).show();
        }else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Public.apiURL + "/produtos/search" + query, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Produto> produtos = JsonParser.parserJsonProdutos(response);
                    if (produtoListener != null)
                        produtoListener.onRefreshProdutos(produtos);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(jsonArrayRequest);
        }
    }

    //endregion Filters

    public void BetterToast(Context context,String message) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            try {
                Toast.makeText(context, jsonObject.getString("response"), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

}