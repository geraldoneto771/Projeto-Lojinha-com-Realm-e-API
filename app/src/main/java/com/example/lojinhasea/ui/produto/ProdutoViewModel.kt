package com.example.lojinhasea.ui.produto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lojinhasea.models.CategoryModel
import com.example.lojinhasea.models.ProdutoModel
import com.example.lojinhasea.repository.bd.produto.Repository as ProdutoDB
import com.example.lojinhasea.repository.bd.categoria.Repository as CategoriaDB
import com.example.lojinhasea.repository.api.Product.Repository as ProdutoAPI
import com.example.lojinhasea.repository.api.Category.Repository as CategoriaAPI

class ProdutoViewModel : ViewModel() {

    //variavel para configurar o produto API
    private val _produtoCheck = MutableLiveData<Boolean>().apply { value = true }

    //variavel para configurar a categoria API
    private val _categoriaCheck = MutableLiveData<Boolean>().apply { value = true }

    //variável para configurar o UPDATE
    private val _produto = MutableLiveData<ProdutoModel>().apply { value = ProdutoModel() }

    //váriavel para configura o produto do repositorio local
    private val _produtos = MutableLiveData<ArrayList<ProdutoModel>>().apply { value = ArrayList() }

    //váriavel para configurar a categoria do repositório local
    private val _categoria = MutableLiveData<ArrayList<CategoryModel>>().apply { CategoriaDB().getAll(this) }

    //
    var produtoTeste = ProdutoModel()
    var positionTeste = 0

    //variaveis liveData
    val categoryCheck: LiveData<Boolean> = _categoriaCheck
    val produtoCheck: LiveData<Boolean> = _produtoCheck
    val produtos: LiveData<ArrayList<ProdutoModel>> = _produtos
    val produto: LiveData<ProdutoModel> = _produto
    val categorias: LiveData<ArrayList<CategoryModel>> = _categoria

    //função para criar o produto no repositorio local
    fun createProduct(produto: ProdutoModel) { ProdutoDB().create(produto) }

    //função para criar a categoria no repositorio local
    fun createCategory(category: CategoryModel) { CategoriaDB().create(category) }

    //função para deletar um produto
    fun deleteProduct(id: String): Boolean { return ProdutoDB().delete(id, _produtos) }



    //função para pegar as informações do produto da API
    fun getAllProductApi() { return ProdutoAPI().get(_produtoCheck) }

    //função para pegar as informações da o produto do repositorio local
    fun getAllProduct() { ProdutoDB().getAll(_produtos) }


    fun getByIdCategory(id: Int): CategoryModel { return CategoriaDB().getById(id) }
    fun getByNameCategory(category: String): CategoryModel { return CategoriaDB().getByName(category) }

    //função para pegar as informações da categoria da API
    fun getAllCategoryApi() { return CategoriaAPI().get(_categoriaCheck) }

    //função para pegar as informações da categoria do repositório local
    fun getCategory(){ CategoriaDB().getAll(_categoria) }

    //função para vender o produto e alterar os dados no repositorio
    fun saleProduct(qttStock: Int, qttSale: Int, item: Int) {
        var obj = _produtos.value?.get(item)!!
        ProdutoDB().saleProduct(qttStock, qttSale, _produtos, obj)
    }

    //função para editar o produto
    fun update(produto: ProdutoModel, item: Int ) {
        var obj = _produtos.value?.get(item)!!
        ProdutoDB().update(produto, _produtos,obj) }


}