package com.example.myapplication

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream
import java.util.Calendar
import android.util.Base64
import com.parse.ParseObject
import com.parse.SaveCallback
import com.parse.ParseException


class CadastroFragment : Fragment(R.layout.cadastro) {

    private lateinit var bd: BD
    private var fotoBase64: String = ""
    private var fotoByteArray: ByteArray? = null
    private lateinit var imgFoto: ImageView

    private val tirarFotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            imgFoto.setImageBitmap(imageBitmap)

            val outputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            fotoByteArray = outputStream.toByteArray()
            fotoBase64 = Base64.encodeToString(fotoByteArray, Base64.DEFAULT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bd = BD(requireContext(), "bd", null, 3)

        val titulo = view.findViewById<TextInputEditText>(R.id.idTitulo)
        val data = view.findViewById<TextInputEditText>(R.id.idData)
        val descricao = view.findViewById<TextInputEditText>(R.id.idDescricao)
        val local = view.findViewById<TextInputEditText>(R.id.idLocal)
        val btnCadastrar = view.findViewById<Button>(R.id.idButton)
        imgFoto = view.findViewById(R.id.imgFoto)
        val btnTirarFoto = view.findViewById<Button>(R.id.btnTirarFoto)


        val rgTipo = view.findViewById<RadioGroup>(R.id.idTipo)
        val rgStatus = view.findViewById<RadioGroup>(R.id.idStatus)

        data.setOnClickListener {
            val calendar = Calendar.getInstance()
            val picker = DatePickerDialog(requireContext(), { _, year, month, day ->
                data.setText("$year/${month + 1}/$day")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            picker.show()
        }

        btnTirarFoto.setOnClickListener {
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            tirarFotoLauncher.launch(intentCamera)
        }

        btnCadastrar.setOnClickListener {
            if (titulo.text.isNullOrEmpty() || data.text.isNullOrEmpty() ||
                descricao.text.isNullOrEmpty() || local.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipoSelecionado = if (rgTipo.checkedRadioButtonId == R.id.infra) 1 else 2
            val statusSelecionado = when (rgStatus.checkedRadioButtonId) {
                R.id.aberto -> 1
                R.id.curso -> 2
                else -> 3
            }


            val pessoa = Pessoa(
                "Não solucionado", // solucao
                titulo.text.toString(), // titulo
                data.text.toString(), // data
                descricao.text.toString(), // descricao
                local.text.toString(), // local
                tipoSelecionado, // tipo
                statusSelecionado, // status
                0, // id
                fotoBase64
            )
            bd.salvarDados(pessoa)


            val chamadoCloud = ParseObject("Chamados")
            chamadoCloud.put("titulo", titulo.text.toString())
            chamadoCloud.put("data", data.text.toString())
            chamadoCloud.put("descricao", descricao.text.toString())
            chamadoCloud.put("local", local.text.toString())
            chamadoCloud.put("tipo", tipoSelecionado)
            chamadoCloud.put("status", statusSelecionado)

            if (fotoBase64.isNotEmpty()) {
                chamadoCloud.put("fotoTextual", fotoBase64)
            }
                chamadoCloud.saveInBackground(SaveCallback { erroSalvar: ParseException? ->
                    if (erroSalvar == null) {
                        Toast.makeText(requireContext(), "Salvo local e na Nuvem (sem foto)!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Erro nuvem: ${erroSalvar.message}", Toast.LENGTH_LONG).show()
                    }
                })

            titulo.text?.clear()
            data.text?.clear()
            descricao.text?.clear()
            local.text?.clear()
            imgFoto.setImageDrawable(null)
            fotoByteArray = null
            fotoBase64 = ""
        }
    }
}