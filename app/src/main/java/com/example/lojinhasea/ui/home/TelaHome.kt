package com.example.lojinhasea.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lojinhasea.databinding.TelaHomeUI


class TelaHome : Fragment() {

    //variável de inicialização tardia do tipo do xml tela_home
    lateinit var binding: TelaHomeUI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //inflando a tela home no binding
        binding = TelaHomeUI.inflate(inflater)

        //botão de abrir a tela de lista de produtos
        binding.buttonListProduct.setOnClickListener {
            var action = TelaHomeDirections.actionTelaHomeToListaProduto()
            findNavController().navigate(action)
        }

        //botão de abrir a tela de cadastro de produtos
        binding.buttonCreateCategory.setOnClickListener {
            var action = TelaHomeDirections.actionTelaHomeToCadastroCategoria()
            findNavController().navigate(action)
        }
        return binding.root
    }

}