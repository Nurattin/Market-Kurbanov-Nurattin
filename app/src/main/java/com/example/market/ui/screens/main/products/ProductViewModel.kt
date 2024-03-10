package com.example.market.ui.screens.main.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.market.data.collectAsResult
import com.example.market.domain.models.Category
import com.example.market.domain.models.Product
import com.example.market.domain.repository.ProductRepository
import com.example.market.utils.EffectHandler
import com.example.market.utils.EventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_TIME_DELAY = 500L

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel(), EventHandler<ProductEvent>, EffectHandler<ProductEffect> {

    private val _productUiState = MutableStateFlow(FilmsUiState())
    val productUiState = _productUiState.asStateFlow()

    val productList = MutableStateFlow<PagingData<Product>>(PagingData.empty())


    override val effectChannel: Channel<ProductEffect> = Channel()

    private var searchJob: Job? = null

    init {
        getProducts()
        getCategories()
    }

    override fun obtainEvent(event: ProductEvent) {
        when (event) {
            ProductEvent.RefreshData -> {
                getProducts(
                    query = productUiState.value.searchBarText,
                    category = productUiState.value.selectedCategory,
                )
                getCategories()
            }

            is ProductEvent.ChangeSearchBarText -> {
                changeSearchBarText(event.value)
            }

            is ProductEvent.SelectedProduct -> {
                getProductDetail(event.id)
            }

            is ProductEvent.RefreshProductDetail -> {
                getProductDetail(event.id)
            }

            ProductEvent.ClearSelectedProduct -> {
                clearSelectedFilm()
            }

            is ProductEvent.SelectedCategory -> {
                changeSelectedCategory(event.name)
            }
        }
    }

    private fun changeSelectedCategory(category: Category) {
        _productUiState.update { currentState ->
            currentState.copy(
                selectedCategory = if (category == currentState.selectedCategory) null else category
            )
        }
        getProducts(
            query = productUiState.value.searchBarText,
            category = productUiState.value.selectedCategory,
        )
    }

    private fun changeSearchBarText(value: String) {
        _productUiState.update { currentState ->
            currentState.copy(
                searchBarText = value,
            )
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_TIME_DELAY)
            getProducts(
                query = productUiState.value.searchBarText,
                category = productUiState.value.selectedCategory,
            )
        }
    }

    private fun clearSelectedFilm() {
        _productUiState.update { currentState ->
            currentState.copy(
                productDetailLoading = false,
                errorFilmDetail = null,
                selectedProduct = null,
            )
        }
    }

    private fun getProductDetail(id: String) {
        viewModelScope.launch {
            productRepository.getProductDetail(
                id = id,
            ).collectAsResult(
                onSuccess = { productDetail ->
                    _productUiState.update { currentState ->
                        currentState.copy(
                            productDetailLoading = false,
                            errorFilmDetail = null,
                            selectedProduct = productDetail,
                        )
                    }
                },
                onError = { ex, message ->
                    _productUiState.update { currentState ->
                        currentState.copy(
                            productDetailLoading = false,
                            errorFilmDetail = message,
                        )
                    }
                    sendEffect(ProductEffect.ShowToast(message.toString()))
                },
                onLoading = {
                    _productUiState.update { currentState ->
                        currentState.copy(
                            productDetailLoading = true,
                            errorFilmDetail = null,
                        )
                    }
                }
            )
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            productRepository.getCategories()
                .collectAsResult(
                    onSuccess = { categories ->
                        _productUiState.update { currentState ->
                            currentState.copy(
                                categories = categories,
                            )
                        }
                    }
                )
        }
    }

    private fun getProducts(
        query: String = "",
        category: Category? = null,
    ) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            productRepository.getProduct(
                query = query,
                category = category?.name,
            ).cachedIn(viewModelScope)
                .collectLatest { products ->
                    productList.update {
                        products
                    }
                }
        }
    }
}

data class FilmsUiState(
    val filmsLoading: Boolean = false,
    val favoriteFilmsLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val productDetailLoading: Boolean = false,
    val error: String? = null,
    val errorFilmDetail: String? = null,
    val searchBarText: String = "",
    val selectedProduct: Product? = null,
)

sealed interface ProductEffect {
    data class ShowToast(
        val message: String,
    ) : ProductEffect
}

sealed interface ProductEvent {
    data object RefreshData : ProductEvent

    data class ChangeSearchBarText(
        val value: String,
    ) : ProductEvent

    data class SelectedProduct(
        val id: String,
    ) : ProductEvent

    data class SelectedCategory(
        val name: Category,
    ) : ProductEvent

    data class RefreshProductDetail(val id: String) : ProductEvent

    data object ClearSelectedProduct : ProductEvent
}