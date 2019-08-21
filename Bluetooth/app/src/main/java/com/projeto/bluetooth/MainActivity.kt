package com.projeto.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mBluetoothAdapter : BluetoothAdapter? = null
    lateinit var mPairedDevices : Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH = 1

    companion object // TODO change to classe App
    {
        val EXTRA_ADDRESS: String = "Device_Address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(mBluetoothAdapter == null)
        {
            Toast.makeText( this, "This device does not support Dente Azul", Toast.LENGTH_SHORT)
            return
        }
        if(!mBluetoothAdapter!!.isEnabled) // !! not going to be null
        {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        select_drone_refresh.setOnClickListener{ pairedDeviceList() }

    }

    private fun pairedDeviceList()
    {
        mPairedDevices = mBluetoothAdapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()

        if(!mPairedDevices.isEmpty())
        {
            for(device in mPairedDevices)
            {
                list.add(device)
                Log.i("device", ""+device)
            }
        }
        else
            Snackbar.make(
                findViewById(R.id.root_layout),
                "No paired bluetooth devices found",
                Snackbar.LENGTH_LONG
            ).show()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        select_drone_list.adapter = adapter
        select_drone_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            val device : BluetoothDevice = list[pos]
            val address : String = device.address

            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            Log.i("Debug", "vai chamar intent numero" + pos)
            startActivity(intent)
            Log.i("Debug", "voltou do start activity")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if(mBluetoothAdapter!!.isEnabled)
//                    Toast.makeText(this,"Bluetooth enabled", Toast.LENGTH_SHORT)
                    Snackbar.make(
                        findViewById(R.id.root_layout),
                        "Bluetooth enabled",
                        Snackbar.LENGTH_LONG
                    ).show()
                else
                    Snackbar.make(
                        findViewById(R.id.root_layout),
                        "Bluetooth not enabled",
                        Snackbar.LENGTH_LONG
                    ).show()
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                Snackbar.make(
                    findViewById(R.id.root_layout),
                    "Bluetooth request has been canceled",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}
