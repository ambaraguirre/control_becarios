package com.example.ordinario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ordinario.databinding.ActivityVerinfo2Binding
import com.example.ordinario.databinding.ActivityVerinfoBinding

class verinfo2 : AppCompatActivity() {
    private lateinit var binding: ActivityVerinfo2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerinfo2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        getActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)


        val nombre = binding.txtNombre2
        val correo = binding.txtCorreo
        val password = binding.txtPassword

        nombre.setText("Nombre: ${intent.getStringExtra("nombre").toString()}")
        correo.setText("Correo: ${intent.getStringExtra("correo").toString()}")
        password.setText("Password: ${intent.getStringExtra("password").toString()}")
    }
}