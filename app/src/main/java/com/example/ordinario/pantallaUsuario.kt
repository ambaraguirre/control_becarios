package com.example.ordinario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ordinario.databinding.ActivityPantallaUsuarioBinding

class pantallaUsuario : AppCompatActivity() {
    private lateinit var binding: ActivityPantallaUsuarioBinding


    private var data =  ArrayList<Becarios>()
    private lateinit var rvAdapter: becariosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvBecarios.layoutManager = LinearLayoutManager(this)

        val btnAgregarBecario = binding.btnAgregar

        //conexion base de datos
        val dbBecarios = DBHelperBecarios(this)
        var db = dbBecarios.readableDatabase

        //Declaramos un cursor para recorrer los registros en la tabla
        val cursor = db.rawQuery("SELECT * FROM becarios", null)

        //evaluar if si el cursor de la base de datos se  puede mover
        if(cursor.moveToFirst())
        {
            do {
                //pasar los valores de la tabla a variables locales
                val idBecarios = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val itemNom = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val itemTipoDeBeca = cursor.getString(cursor.getColumnIndexOrThrow("tipoDeBeca"))
                val itemCuenta = cursor.getString(cursor.getColumnIndexOrThrow("nocuenta"))
                val itemImagen = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))

                //agregarlos a la lista data
                data.add(Becarios(
                    "${idBecarios}",
                    "${itemNom}",
                    "${itemCuenta}",
                    "${itemTipoDeBeca}",
                    "${itemImagen}"
                )
                )
            }  while(cursor.moveToNext())
            //cerramos base de datos y cursor
            db.close()
            cursor.close()
            //pasar la informacion del Array por el adaptador
            rvAdapter = becariosAdapter(this,data, object : becariosAdapter.OptionsMenuClickListener{
                override fun onOptionsMenuClicked(position: Int) {
                    //funcion para llamar menu opciones
                    itemOptionsMenu(position)
                }
            })

            binding.rvBecarios.adapter = rvAdapter
        }// fin del if movetofirst



        btnAgregarBecario.setOnClickListener({
            val intent = Intent(this, agregarBecario::class.java)
            startActivity(intent)
        })
    }

    private fun itemOptionsMenu( position: Int) {
        val popupMenu = PopupMenu(this, binding.rvBecarios[position].findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)
        //Para cambiarnos de activity
        val intento2 = Intent(this,agregarBecario::class.java)
        //Implementar el click en el item
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.borrar -> {
                        val dialog = AlertDialog.Builder(this@pantallaUsuario)
                            .setTitle("Borrar resgistro")
                            .setMessage("¿Estas seguro que deseas eliminar el registro?")
                            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                                Toast.makeText(this@pantallaUsuario, "Presionaste cancelar", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                                val dbBecarios = DBHelperBecarios(this@pantallaUsuario)
                                val tmpBecario = data[position]
                                val db = dbBecarios.writableDatabase
                                val id = arrayOf(tmpBecario.id)
                                val res = db.delete("becarios", "id = ?", id)
                                db.close()

                                if(res > 0){
                                    data.remove(tmpBecario)
                                    rvAdapter.notifyItemRemoved(position)
                                    Toast.makeText(this@pantallaUsuario, "El elemento se elimino con éxito", Toast.LENGTH_LONG).show()
                                }
                                else{
                                    Toast.makeText(this@pantallaUsuario, "Error al eliminar el elemento" + "${id}", Toast.LENGTH_LONG).show()
                                }
                                dialog.dismiss()
                                recreate()
                            }
                            .setCancelable(false)
                            .create()

                        dialog.show()
                        return true
                    }
                    R.id.editar ->{
                        //Tomamos los datos del becario, en la posición de la lista donde hicieron click
                        val id = data[position].id
                        val nombre = data[position].nombre
                        val nocuenta = data[position].nocuenta
                        val tipoDeBeca = data[position].tipoDeBeca
                        val imagen = data[position].imagen
                        //En position tengo el indice del elemento en la lista
                        val idBecario: Int = position
                        intento2.putExtra("id", "${(data.indexOf(Becarios("${id}","$nombre","$nocuenta","$tipoDeBeca", "$imagen"))) + 1}")
                        intento2.putExtra("mensaje","edit")
                        intento2.putExtra("nombre","${nombre}")
                        intento2.putExtra("nocuenta","${nocuenta}")
                        intento2.putExtra("tipoDeBeca","${tipoDeBeca}")
                        intento2.putExtra("imagen", "${imagen}")
                        //Pasamos por extras el idAlum para poder saber cual editar de la lista (ArrayList)
                        intento2.putExtra("idB",idBecario)
                        startActivity(intento2)
                        return true
                    }
                    R.id.ver ->{
                        val intento3 = Intent(this@pantallaUsuario, verinfo::class.java)

                        val nombre = data[position].nombre.toString()
                        val nocuenta = data[position].nocuenta.toString()
                        val tipoDeBeca = data[position].tipoDeBeca.toString()
                        val imagen = data[position].imagen

                        intento3.putExtra("nombre","${nombre}")
                        intento3.putExtra("nocuenta","${nocuenta}")
                        intento3.putExtra("tipoDeBeca","${tipoDeBeca}")
                        intento3.putExtra("imagen", "$imagen")
                        startActivity(intento3)
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

}