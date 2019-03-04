package dk.cphbusiness.bluetooth.experiment

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val REQUEST_ENABLE_BT = 1
    val deviceNames = mutableListOf<String>("No devices found, press update")
    lateinit var adapter: ArrayAdapter<String>

    val reciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent)  {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    deviceNames.add("${device.name}%${device.address}")
                    adapter.notifyDataSetChanged()
                    }
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enable()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNames)
        device_list.adapter = adapter
        update_button.setOnClickListener {
            val devices = BluetoothAdapter.getDefaultAdapter()?.bondedDevices
            if (devices != null) {
                deviceNames.clear()
                devices.forEach { deviceNames.add("${it.name}/${it.address}")}
                }
            adapter.notifyDataSetChanged()
            }
        }

    fun enable() {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            Log.d("BTE", "No bluetooth adapter")
            return
            }
        if (!adapter.isEnabled) {
            Log.d("BTE", "Bluetooth not enabled yet")
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
            }
        Log.d("BTE", "Adapter already enabled")
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ENABLE_BT -> {
                when (resultCode) {
                    Activity.RESULT_OK -> Log.d("BTE", "Bluetooth enabled")
                    else -> Log.d("BTE", "Sorry no bluetooth")
                    }
                }
            else -> {
                Log.d("BTE", "unknown command")
                }
            }
        }

    }
