package mx.edu.ittepic.ladm_u4_tarea2_contentproviderskotlin_ricardovilla


import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val siPermisoMsj = 1
    val siPermisoLlamada = 2
    var contenido : Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS),siPermisoMsj)
        }

        btn_borradores.setOnClickListener {
            mostrarMensajes()
        }

        btn_llamadas.setOnClickListener {
            mostrarLlamadas()
        }


    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == siPermisoMsj){

            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),siPermisoLlamada)
                contenido = CallLog.Calls.CONTENT_URI
            }
        }
    }


    //REGISTRO LAMADAS
    private fun mostrarLlamadas() {
        contenido = CallLog.Calls.CONTENT_URI
        var cursor = contentResolver.query(contenido!!,null,null,null,null)

        var resultado = ""

        if(cursor!!.moveToFirst()){

            while(cursor.moveToNext()){

                resultado += "\nCONTACTO: "+cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))+
                        "\nDURACION: "+cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))+"\n"
            }
        }
        else{
            resultado = "NO HAY REGISTROS DE LLAMADAS"
        }
        txt_mostrar.setText(resultado)

    }


    //BORRADORES
    private fun mostrarMensajes() {

        var cursor = contentResolver.query(Uri.parse("content://sms/draft"),null,null,null,null)

        var resultado = ""

        if(cursor!!.moveToFirst()){

            do{

                resultado+= "\nMENSAJE: "+cursor.getString(cursor.getColumnIndex("body")) +
                        "\n//////////////////////////\n"

            }while(cursor.moveToNext())

        }
        else{
            resultado = "NO HAY BORRADORES"
        }
        txt_mostrar.setText(resultado)

    }


}
