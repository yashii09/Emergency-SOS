package com.example.myfamily

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myfamily.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_CONTACTS
    )

    val permissionsCode = 78

    lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       askForPermission()



       binding.bottomBar.setOnItemSelectedListener {menuItem ->

           when (menuItem.itemId) {
               R.id.nav_guard -> {
                   inflateFragment(GuardFragment.newInstance())
               }
               R.id.nav_home -> {
                   inflateFragment(HomeFragment.newInstance())
               }
               R.id.nav_dashboard -> {
                   inflateFragment(MapsFragment())
               }
               R.id.nav_profile -> {
                   inflateFragment((ProfileFragment.newInstance()))
               }
           }

           true
       }
        binding.bottomBar.selectedItemId = R.id.nav_home
    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(this, permissions, permissionsCode)
    }

    private fun inflateFragment(newInstance: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, newInstance)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == permissionsCode){
            if(allPermissionGranted()){
                //openCamera()
            }else{
                
            }
        }
    }

    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    private fun allPermissionGranted(): Boolean {
        for(item in permissions){
            if(ContextCompat.checkSelfPermission(this, item) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}