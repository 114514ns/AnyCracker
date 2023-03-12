package cn.coderstory.xposedtemplate

import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.coderstory.xposedtemplate.ui.theme.XposedTemplateTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val state = wifiManager.wifiState
        val wifiInfo = wifiManager.connectionInfo
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        if(ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
            //进行权限申请,200是标识码
            ActivityCompat.requestPermissions(this, arrayOf("android.permission.ACCESS_FINE_LOCATION"), 200);
        }
        setContent {
            XposedTemplateTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting(
                        wifiInfo.toString()
                    )
                }
            }
        }


    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    XposedTemplateTheme {
        Greeting("Android")
    }
}