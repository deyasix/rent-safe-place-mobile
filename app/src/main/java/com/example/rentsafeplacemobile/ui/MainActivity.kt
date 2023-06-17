package com.example.rentsafeplacemobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rentsafeplacemobile.R
import com.example.rentsafeplacemobile.RetrofitClient.basicAuthInterceptor
import com.example.rentsafeplacemobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LoginFragment.OnLoginSuccessListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.buildings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, BuildingFragment())
                        .commit()
                    true
                }
                R.id.account -> {
                    if (basicAuthInterceptor.isUserLoggedIn) {
                        val accountFragment = AccountFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, accountFragment)
                            .commit()
                    } else {
                        val loginFragment = LoginFragment()
                        loginFragment.onLoginSuccessListener = this@MainActivity
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, loginFragment)
                            .commit()
                    }
                    true
                }
                else -> false
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, BuildingFragment())
            .commit()
    }

    override fun onLoginSuccess() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, BuildingFragment())
            .commit()
    }

}