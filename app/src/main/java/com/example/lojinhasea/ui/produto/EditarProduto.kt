package com.example.lojinhasea.ui.produto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lojinhasea.databinding.CadastroProdutoUI
import com.example.lojinhasea.models.ProdutoModel
import kotlinx.android.synthetic.main.fragment_lista_produto.*

class EditarProduto: Fragment()  {
    private lateinit var mBinding: CadastroProdutoUI
    private lateinit var viewModel: ProdutoViewModel
    lateinit var Product: ProdutoModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = CadastroProdutoUI.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity()).get(ProdutoViewModel::class.java)

        var product = ProdutoModel().apply {
            viewModel.produtoTeste
        }
        mBinding.textTitle.text = "Alterar Produto"
        mBinding.productName.setText(viewModel.produtoTeste.name)
        mBinding.productDescription.setText(viewModel.produtoTeste.description)
        mBinding.productPrice.setText(viewModel.produtoTeste.price.toString())
        mBinding.productQttStock.setText(viewModel.produtoTeste.qttStock.toString())

        //Editar produto
        mBinding.buttonSave.setOnClickListener {

            product.name = mBinding.productName.text.toString()
            product.description = mBinding.productDescription.text.toString()
            product.price = mBinding.productPrice.text.toString().toFloat()
            product.qttStock = mBinding.productQttStock.text.toString().toInt()
            product.category = viewModel.produtoTeste.category

            viewModel.update(product, viewModel.positionTeste)

            findNavController().popBackStack()
        }

        //return to previous page
        mBinding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        return mBinding.root
    }
}