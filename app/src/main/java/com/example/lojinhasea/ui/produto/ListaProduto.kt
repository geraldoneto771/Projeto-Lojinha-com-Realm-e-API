package com.example.lojinhasea.ui.produto

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.lojinhasea.R
import com.example.lojinhasea.databinding.ItemProductUI
import com.example.lojinhasea.databinding.ListaProdutoUI
import com.example.lojinhasea.models.ProdutoModel
import com.example.lojinhasea.services.DataSyncDownload
import com.example.lojinhasea.services.DataSyncUpload
import java.time.Duration
import java.util.concurrent.TimeUnit


class ListaProduto : Fragment() {

    //variável de inicialização tardia do tipo do xml
    private lateinit var binding: ListaProdutoUI

    //variável de inicialização tardia do tipo do viewmodel
    private lateinit var viewModel: ProdutoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflando a tela de listar produto no binding
        binding = ListaProdutoUI.inflate(inflater)

        //vinculando a viewmodel do produto
        viewModel = ViewModelProvider(requireActivity()).get(ProdutoViewModel::class.java)

        //chamando a função apiRequest
        apiRequest()

        //pegando a lista de produtos do BD e da API
        viewModel.getAllProduct()
        viewModel.getAllProductApi()

        //observando a lista de produtos com o recyclerView
        viewModel.produtos.observe(viewLifecycleOwner, Observer {
            binding.recyclerview.adapter = ProductAdapter(it)
            binding.recyclerview.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        })

        //botão para redirecionar para página de cadastro de produto
        binding.addProduct.setOnClickListener {
            findNavController().navigate(ListaProdutoDirections.actionListaProdutoToCadastroProduto())
        }
        return binding.root
    }

    //Adapter e viewHolder do recyclerView da Lista de produtos
    inner class ProductAdapter(val list: ArrayList<ProdutoModel>) :
        RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

        //vinculando o layout do item com um binding para o viewHolder utilizar
        inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var bind: ItemProductUI

            init {
                bind = ItemProductUI.bind(itemView)
            }
        }

        //configurando o layout principal com o layout do item
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            return ProductViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_aluno, parent, false)
            )
        }

        @SuppressLint("ResourceAsColor")
        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val product = list[position]

            val cat = viewModel.getByIdCategory(product.category)


            //alterando os valores dos items da lista com os valores salvos no produtoModel com base na pa posição
            holder.bind.fieldName.text = product.name
            holder.bind.category.text = cat.name

            //botão para exibir o menu de cada item na lista
            holder.bind.buttonAction.setOnClickListener() {
                //variavel do popUp
                val popup = PopupMenu(requireContext(), holder.bind.buttonAction)
                //inflando o layout de menu no popUp
                popup.inflate(R.menu.menu_file)
                //ação de escolha dos items do menu
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        //botão de alterar produto -> redireciona para tela de editar produto
                        R.id.alterProduct -> {
                            viewModel.produtoTeste = product
                            viewModel.positionTeste = position
                            findNavController().navigate(
                                ListaProdutoDirections.actionListaProdutoToEditarProduto()
                            )
                            true
                        }
                        //botão para deletar o produto da lista
                        R.id.deleteProduct -> {
                            viewModel.deleteProduct(product.id)
                            true
                        }
                        else -> false
                    }
                }
                //exibindo o popUp do menu
                popup.show()
            }

            //variavel para representar da exibição das informações do produto na lista
            val hiddenInfo = holder.bind.moreInfo

            //ação ao clica em cima de um item da lista
            holder.bind.relative1.setOnClickListener {

                //se a lista estiver visivel, torne-a invisivel
                if (hiddenInfo.isVisible) {
                    hiddenInfo.visibility = View.GONE
                }
                //se a lista estiver invisivel, torne-a visivel
                else {
                    hiddenInfo.visibility = View.VISIBLE

                    //alterando os valores dos produtos na lista conforme os valores salvos no produtoModel
                    holder.bind.description.text = product.description

                    //????
                    var stock = holder.bind.stockValue.text.toString().toInt()

                    //passando as variaveis que possui as informações do produto para os atributos do ProdutoModel
                    holder.bind.price.text = product.price.toString()
                    holder.bind.saleValue.text = product.qttSale.toString()
                    holder.bind.stockValue.text = product.qttStock.toString()

                    //função do botão para chamar o metodo de compra da viewmodel
                    holder.bind.buttonBuy.setOnClickListener {
                        ////Salvando a quantidade de produtos informada pelo usuários na variável
                        var qttSale = holder.bind.qtdSale.text.toString().toInt()
                        //chamando a função para vender o produto e alterar os dados no repositorio
                        viewModel.saleProduct(stock, qttSale, position)
                    }

                }

            }

        }

        //pegando a quantidade de items na lista
        override fun getItemCount(): Int {
            return list.size
        }

    }

    //função de apiRequest para controle do DataSync Download e Upload
    fun apiRequest() {
        try {

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val upload = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                PeriodicWorkRequestBuilder<DataSyncUpload>(
                    900000,
                    TimeUnit.MILLISECONDS
                )
                    .setConstraints(constraints)
                    .setInitialDelay(Duration.ofMillis(100L))
                    .addTag("UPLOAD")
                    .build()

            } else {
                PeriodicWorkRequestBuilder<DataSyncUpload>(
                    900000,
                    TimeUnit.MILLISECONDS
                )
                    .setConstraints(constraints)
                    .addTag("UPLOAD")
                    .setInitialDelay(100, TimeUnit.MILLISECONDS)
                    .build()
            }
            val download = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PeriodicWorkRequestBuilder<DataSyncDownload>(
                    900000,
                    TimeUnit.MILLISECONDS
                )
                    .addTag("DOWNLOAD")
                    .setInitialDelay(Duration.ofMillis(100L))
                    .build()
            } else {
                PeriodicWorkRequestBuilder<DataSyncDownload>(
                    900000,
                    TimeUnit.MILLISECONDS
                )
                    .addTag("DOWNLOAD")
                    .setInitialDelay(100, TimeUnit.MILLISECONDS)
                    .build()
            }


            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                "UPLOAD",
                ExistingPeriodicWorkPolicy.REPLACE, upload
            )

            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                "DOWNLOAD",
                ExistingPeriodicWorkPolicy.REPLACE, download
            )
        } catch (e: NullPointerException) {
            WorkManager.getInstance(requireContext()).cancelAllWork()

        }
    }

}

//????
inline fun <reified W : ListenableWorker> PeriodicWorkRequestBuilder(
    repeatInterval: Long,
    repeatIntervalTimeUnit: TimeUnit
): PeriodicWorkRequest.Builder {
    return PeriodicWorkRequest.Builder(W::class.java, repeatInterval, repeatIntervalTimeUnit)
}

