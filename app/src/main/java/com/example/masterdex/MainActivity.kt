package com.example.masterdex

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.masterdex.Entities.Pokemon
import com.example.masterdex.Fragments.DetailFragment
import com.example.masterdex.Fragments.ListFragment
import com.example.masterdex.Interfaces.FragmentCommunication
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DetailFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener, FragmentCommunication {
    lateinit var lista: ListFragment
    var detalle: DetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(contenedorFragment != null){
            if(savedInstanceState != null){
                return
            }

            lista = ListFragment()

            supportFragmentManager.beginTransaction().replace(R.id.contenedorFragment, lista).commit()

        }

    }

    override fun sendData(data: Pokemon) {

        if(contenedorFragment == null) {
            detalle = this.supportFragmentManager.findFragmentById(R.id.fragDetail) as DetailFragment
        }

        if(detalle != null){
            detalle?.asignarDatos(data, this.findViewById(R.id.fragDetail), this.findViewById(R.id.img_detail))

        }else{
            detalle = DetailFragment()
            var datosEnviados: Bundle = Bundle()
            datosEnviados.putSerializable("Datos", data)
            detalle?.arguments = datosEnviados

            //Fragment en activity
            supportFragmentManager.beginTransaction().replace(R.id.contenedorFragment, detalle!!).addToBackStack(null).commit()

        }

    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
