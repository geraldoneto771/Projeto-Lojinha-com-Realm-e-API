package com.example.lojinhasea.ui.produto

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lojinhasea.databinding.CadastroProdutoUI
import com.example.lojinhasea.databinding.ItemProductUI
import com.example.lojinhasea.models.CategoryModel
import com.example.lojinhasea.models.ProdutoModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cadastro_produto.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroProduto : Fragment(){


    //variável de inicialização tardia do tipo do xml
    private lateinit var mBinding: CadastroProdutoUI

    //variável de inicialização tardia do tipo do viewmodel
    private lateinit var viewModel: ProdutoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflando a tela de cadastro de produto no binding
        mBinding = CadastroProdutoUI.inflate(inflater)

        //vinculando a viewmodel do produto
        viewModel = ViewModelProvider(requireActivity()).get(ProdutoViewModel::class.java)

        //botão para salvar o produto
        mBinding.buttonSave.setOnClickListener {
            //chamando a função de salvar o produto

            saveProduct()
        }
        //botão para retornar para pagina anterior
        mBinding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        //observando a lista de produtos com o recyclerView
        viewModel.categorias.observe(viewLifecycleOwner, { mList ->
            mBinding.spinnerDynamic.adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, mList)
        })

        //loadSpinner()

        return mBinding.root
    }



/*
    private fun loadSpinner() {
        //lista de strings, essa lista poderia vim de um banco de dados de cadastro de categoria tambem
//        val category = viewModel.getAllCategory()
//        Log.e("Cat-name", ""+category[0].name)
        val categorias = viewModel.getAllCategoryApi()
        val mList = listOf(1,3,4)

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, mList)

        mBinding.spinnerDynamic.adapter = adapter
    }
*/


    //função para salvar o produto
    private fun saveProduct() {

        try {
            //Salvando as entradas de dados dos usuários nas variáveis



            val name = mBinding.productName.text.toString()
            val description = mBinding.productDescription.text.toString()
            val category = mBinding.spinnerDynamic.selectedItem.toString()
            val price = mBinding.productPrice.text.toString().toFloat()
            val qttStock = mBinding.productQttStock.text.toString().toInt()

            val cat = viewModel.getByNameCategory(category)

            //passando a classe ProdutoModel como valor na variavel product
            val product = ProdutoModel()

            //passando as variaveis que possui as informações do produto para os atributos do ProdutoModel
            product.name = name
            product.description = description
            product.category = cat.id
            product.price = price
            product.qttStock = qttStock

            //criando o produto com base nas informações do usuário
            viewModel.createProduct(product)

            //após salvar retorne para página antetior
            findNavController().popBackStack()
            //msg de adicionado com sucesso
            Snackbar.make(
                mBinding.root,
                "Produto adicionado com sucesso!", Snackbar.LENGTH_SHORT
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