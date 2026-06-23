package com.example.myapplication

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MenuLateral : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.toolbar)


        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.bringToFront()

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CadastroFragment())
                .commit()
            navView.setCheckedItem(R.id.nav_chamado)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_chamado -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CadastroFragment()).commit()
            }
            R.id.nav_listagem -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ListarFragment()).commit()
            }
            R.id.nav_pesquisa -> {

                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PesquisaFragment()).commit()
            }
            R.id.nav_estatistica -> {

                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, EstatisticFragment()).commit()
            }
            R.id.nav_sobre -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SobreFragment()).commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}