package com.example.mall_navigation
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mall_navigation.adapters.ShopAdapter
import com.example.mall_navigation.models.Point
import com.example.mall_navigation.models.Shop
import com.example.mall_navigation.viewmodel.NavigationViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NavigationViewModel
    private lateinit var shopAdapter: ShopAdapter
    private lateinit var searchInput: TextInputEditText
    private lateinit var searchButton: MaterialButton
    private lateinit var floorSpinner: Spinner
    private lateinit var shopsRecyclerView: RecyclerView
    private lateinit var routeInfoCard: MaterialCardView
    private lateinit var routeDistanceText: TextView
    private lateinit var instructionsText: TextView
    private lateinit var startNavigationButton: MaterialButton

    private var selectedShop: Shop? = null
    private val currentPosition = Point(0.0, 0.0, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        viewModel = NavigationViewModel()
        setupRecyclerView()
        setupSpinner()
        setupObservers()
        setupListeners()

        viewModel.loadShops()
    }

    private fun initViews() {
        searchInput = findViewById(R.id.searchInput)
        searchButton = findViewById(R.id.searchButton)
        floorSpinner = findViewById(R.id.floorSpinner)
        shopsRecyclerView = findViewById(R.id.shopsRecyclerView)
        routeInfoCard = findViewById(R.id.routeInfoCard)
        routeDistanceText = findViewById(R.id.routeDistanceText)
        instructionsText = findViewById(R.id.instructionsText)
        startNavigationButton = findViewById(R.id.startNavigationButton)
    }

    private fun setupRecyclerView() {
        shopAdapter = ShopAdapter { shop ->
            selectedShop = shop
            AlertDialog.Builder(this)
                .setTitle("Маршрут")
                .setMessage("Построить маршрут до ${shop.name}?")
                .setPositiveButton("Да") { _, _ ->
                    val end = Point(shop.x, shop.y, shop.floor)
                    viewModel.getRoute(currentPosition, end)
                }
                .setNegativeButton("Нет", null)
                .show()
        }
        shopsRecyclerView.layoutManager = LinearLayoutManager(this)
        shopsRecyclerView.adapter = shopAdapter
    }

    private fun setupSpinner() {
        val floors = arrayOf("Все этажи", "1 этаж", "2 этаж", "3 этаж")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, floors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        floorSpinner.adapter = adapter
        floorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>, v: View?, pos: Int, id: Long) {
                when (pos) {
                    0 -> viewModel.loadShops()
                    1 -> viewModel.loadShopsByFloor(1)
                    2 -> viewModel.loadShopsByFloor(2)
                    3 -> viewModel.loadShopsByFloor(3)
                }
            }
            override fun onNothingSelected(p: AdapterView<*>) {}
        }
    }

    private fun setupObservers() {
        viewModel.shops.observe(this) { shops ->
            shopAdapter.submitList(shops)
        }
        viewModel.route.observe(this) { route ->
            if (route != null) {
                routeInfoCard.visibility = android.view.View.VISIBLE
                routeDistanceText.text = "Расстояние: ${String.format("%.1f", route.distance)} м"
                instructionsText.text = route.instructions.joinToString("\n\n")
            }
        }
        viewModel.error.observe(this) { error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    private fun setupListeners() {
        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            viewModel.searchShops(query)
        }
        startNavigationButton.setOnClickListener {
            selectedShop?.let { shop ->
                val end = Point(shop.x, shop.y, shop.floor)
                viewModel.getRoute(currentPosition, end)
            }
        }
    }
}