package com.example.masterdex.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.masterdex.Entities.Pokemon
import com.example.masterdex.R
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_list.view.*
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var txtId: TextView
    private lateinit var txtName: TextView
    private lateinit var typesView: TextView
    private lateinit var numberView: TextView
    private lateinit var heightView: TextView
    private lateinit var weightView: TextView
    private lateinit var abilitiesView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        var vista = inflater.inflate(R.layout.fragment_detail, container, false)

        txtId = vista.findViewById(R.id.detail_pokemonId)
        txtName = vista.findViewById(R.id.detail_pokemonName)
        typesView = vista.findViewById(R.id.tv_types)
        heightView = vista.findViewById(R.id.tv_height)
        weightView = vista.findViewById(R.id.tv_weight)
        abilitiesView = vista.findViewById(R.id.tv_abilities)

        var datosRecibidos: Bundle? = arguments
        var pokemon: Pokemon? = null

        datosRecibidos?.let{
            pokemon = it.getSerializable("Datos") as Pokemon
            asignarDatos(pokemon!!, container!!, vista!!)

        }

        return vista
    }

    public fun asignarDatos(pokemon: Pokemon, container: ViewGroup, vista: View) {
        val resultado = JSONObject(pokemon.data)

        txtId.text = "Pokédex #"+pokemon?.id
        txtName.text = pokemon?.name?.capitalize()

        heightView.text = "Height: "+(resultado.getInt("height").toFloat()/10.0).toString() + " m"
        weightView.text = "Weight: "+(resultado.getInt("weight").toFloat()/10.0).toString() + " kg"

        val abilitiesList = resultado.getJSONArray("abilities")
        var abilitiesText : String = ""

        for (i in 0..(abilitiesList.length()-1)){
            abilitiesText = abilitiesText+abilitiesList.getJSONObject(i).getJSONObject("ability").getString("name").capitalize()
            if (i != abilitiesList.length()-1){
                abilitiesText = abilitiesText+"\n"
            }
        }

        abilitiesView.text = abilitiesText

        val types = resultado.getJSONArray("types")

        if (types.length() == 2){
            typesView.text = "Types: "+types.getJSONObject(0).getJSONObject("type").getString("name").capitalize()+", "+types.getJSONObject(1).getJSONObject("type").getString("name").capitalize()
        }else{
            typesView.text = "Type: "+types.getJSONObject(0).getJSONObject("type").getString("name").capitalize()
        }

        Glide.with(container).load(JSONObject(pokemon?.data).getJSONObject("sprites").getString("front_default")).into(vista.findViewById(R.id.img_detail))
    }

    public fun asigarDatos(pokemon: Pokemon) {


    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
