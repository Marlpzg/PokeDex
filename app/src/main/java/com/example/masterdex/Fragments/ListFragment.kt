package com.example.masterdex.Fragments

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.masterdex.Adapters.PokemonAdapter
import com.example.masterdex.Entities.Pokemon
import com.example.masterdex.Interfaces.FragmentCommunication
import com.example.masterdex.R
import org.json.JSONObject
import java.io.IOException
import com.example.masterdex.Utilities.NetworkUtils
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.item_list.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var viewAdapter:PokemonAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var pokemonRes: ArrayList<Pokemon> = ArrayList<Pokemon>()
    private lateinit var linearRecycle: LinearLayout
    private val puntitos = listOf("",".","..","...")
    private var value = 0
    private lateinit var searchButton: Button
    private lateinit var txt_type: EditText


    private lateinit var loadAll: FetchPokemonTask

    private lateinit var activity: Activity
    private lateinit var comunicacion: FragmentCommunication

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("Listado", pokemonRes)
        super.onSaveInstanceState(outState)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
            pokemonRes = savedInstanceState.getParcelableArrayList("Listado")
        }
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista: View = inflater.inflate(R.layout.fragment_list, container, false)

        viewManager = LinearLayoutManager(context)


        return vista
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Activity){
            this.activity = context
            comunicacion = this.activity as FragmentCommunication
        }

        if (context is OnFragmentInteractionListener) {
            listener = context

        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onStart() {
        super.onStart()

        if(pokemonRes.size > 0){
            initRecycler()
        }

        searchButton = btn_search
        txt_type = txt_pokemon_type

        searchButton.setOnClickListener{v->
            if (recyclerPokemon != null){
                try {
                    loadAll.stopping = true
                }catch (e: UninitializedPropertyAccessException){
                    println("Nel")
                }
                loadAll = FetchPokemonTask()
                loadAll.execute(txt_type.text.toString().toLowerCase(), "type")
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        private lateinit var task: String
        private var index: Int = 0
        private var limit: Int = 0
        var stopping = false

        override fun doInBackground(vararg pokemonNumbers: String): String? {

            if (pokemonNumbers.isEmpty()) {
                return null
            }

            val ID = pokemonNumbers[0]
            task = pokemonNumbers[1]
            val search = task

            if (task == "pokemon"){
                index = pokemonNumbers[2].toInt()
                limit = pokemonNumbers[3].toInt()
            }

            val pokeAPI = NetworkUtils.buildUrl(ID, search)

            if (recyclerPokemon == null || stopping == true){
                println("Final")
                this.cancel(true)
                return ""
            }
            try {
                return NetworkUtils.getResponseFromHttpUrl(pokeAPI!!)
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        }

        override fun onPostExecute(pokemonInfo: String?) {
            if (pokemonInfo != null && pokemonInfo != "") {

                val resultados = JSONObject(pokemonInfo)

                if(task == "type") {

                    val pokemones = resultados.getJSONArray("pokemon")

                    for(i in 0..(pokemones.length()-1)){
                        FetchPokemonTask().execute(pokemones.getJSONObject(i).getJSONObject("pokemon").getString("name"), "pokemon", i.toString(), pokemones.length().toString())
                        if(recyclerPokemon == null || stopping == true){
                            return
                        }
                    }

                }else{

                    pokemonRes.add(index, Pokemon(
                        resultados.getInt("id").toString(),
                        resultados.getString("name"),
                        pokemonInfo
                    ))

                    if(index%2 == 0 && lbl_loading_text != null){
                        lbl_loading_text.text = "Loading Data"+puntitos[value];
                        value = value+1
                        if(value > 3){
                            value = 0
                        }
                    }

                    if(recyclerPokemon != null && index == limit-1) {
                        initRecycler()

                        if(lbl_loading_text != null) {

                            lbl_loading_text.text = ""

                        }
                    }

                }


            } else {
                pokemonRes = ArrayList()
                pokemonRes.add(0,Pokemon("Empty","Check the information provided", "{}"))


                if(lbl_loading_text != null) {

                    lbl_loading_text.text = ""

                }
                initRecycler()
            }
        }
    }

    fun initRecycler() {

        viewAdapter = PokemonAdapter(pokemonRes)

        viewAdapter.setOnClickListener(View.OnClickListener {
            if(pokemonRes.get(recyclerPokemon.getChildAdapterPosition(it)).id != "Empty") {
                comunicacion.sendData(pokemonRes.get(recyclerPokemon.getChildAdapterPosition(it)))
                loadAll.stopping = true
            }
        })

        recyclerPokemon.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

    }
}
