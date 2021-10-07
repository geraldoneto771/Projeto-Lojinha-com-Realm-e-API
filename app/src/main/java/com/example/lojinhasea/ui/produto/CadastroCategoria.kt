package com.example.lojinhasea.ui.produto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lojinhasea.R
import com.example.lojinhasea.databinding.CadastroCategoriaUI
import com.example.lojinhasea.databinding.CadastroProdutoUI
import com.example.lojinhasea.models.CategoryModel
import com.example.lojinhasea.models.ProdutoModel
import com.google.android.material.snackbar.Snackbar

class CadastroCategoria : Fragment() {
    //variável de inicialização tardia do tipo do xml
    private lateinit var mBinding: CadastroCategoriaUI

    //variável de inicialização tardia do tipo do viewmodel
    private lateinit var viewModel: ProdutoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //inflando a tela de cadasatro de categoria no binding
        mBinding = CadastroCategoriaUI.inflate(inflater)

        //vinculando a viewmodel do produto
        viewModel = ViewModelProvider(requireActivity()).get(ProdutoViewModel::class.java)

        //botão de salvar a categoria
        mBinding.buttonSave.setOnClickListener {
            //chamando a função de salvar a categoria
            saveCategory()

        }
        return mBinding.root
    }

    //função para salvar a cateogria
    private fun saveCategory() {
        try {

            //Salvando as entradas de dados dos usuários nas variáveis
            val name = mBinding.categoryName.text.toString()
            val qttStock = mBinding.qttStock.text.toString().toInt()

            //passando a classe CategoryModel como valor na variavel category
            val category = CategoryModel()

            //passando as variaveis que possui as informações da categoria para os atributos da CategoryModel
            category.name = name
            category.qttStock = qttStock

            //criando a categoria com base nas informações do usuário
            viewModel.createCategory(category)

            //após salvar retorne para página antetior
            findNavController().popBackStack()

            //msg de adicionado com sucesso
            Snackbar.make(
                mBinding.root,
                "Categoria adicionado com sucesso!", Snackbar.LENGTH_SHORT
            ).show()

        } // msg de erro
        catch (nfe: NumberFormatException) {
            Snackbar.make(
                mBinding.root,
                "Informe valores validos", Snackbar.LENGTH_SHORT
            ).show()
        }
    }


}