package com.example.ordinario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.ordinario.databinding.ActivityVerinfoBinding

class verinfo : AppCompatActivity() {
    private lateinit var binding:  ActivityVerinfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)


        val nombre = binding.txtNombre2
        val tipoDeBeca = binding.txtTipoBeca
        val cuenta = binding.txtCuenta2
        val imagen = binding.image

        nombre.setText("Nombre: ${intent.getStringExtra("nombre").toString()}")
        tipoDeBeca.setText("Tipo de Beca: ${intent.getStringExtra("tipoDeBeca").toString()}")
        cuenta.setText("Número de Cuenta: ${intent.getStringExtra("nocuenta").toString()}")
        Glide.with(this).load("${intent.getStringExtra("imagen").toString()}").into(imagen)
    }
}