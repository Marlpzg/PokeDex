package com.example.masterdex

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.masterdex.Entities.Pokemon
import com.example.masterdex.Fragments.DetailFragment
import com.example.masterdex.Fragments.ListFragment
import com.example.masterdex.Interfaces.FragmentCommunication
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail.*

class MainActivity : AppCompatActivity(), DetailFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener, FragmentCommunication {
    var listaGlobal: ArrayList<Pokemon> = ArrayList()


    lateinit var lista: ListFragment
    var detalle: DetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(savedInstanceState == null) {
            lista = ListFragment();

            supportFragmentManager.beginTransaction().replace(R.id.contenedorFragment, lista).commit()
        }

    }

    override fun sendData(data: Pokemon) {



        detalle = DetailFragment()
        var datosEnviados: Bundle = Bundle()
        datosEnviados.putSerializable("Datos", data)
        detalle?.arguments = datosEnviados

        if(fragDetail != null) {
            supportFragmentManager.beginTransaction().add(R.id.fragDetail, detalle!!).commit()
        }else{

            //Fragment en activity
            supportFragmentManager.beginTransaction().replace(R.id.contenedorFragment, detalle!!).addToBackStack(null).commit()

        }

    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
