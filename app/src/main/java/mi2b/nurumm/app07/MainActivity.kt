package mi2b.nurumm.app07

import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.CursorAdapter
import android.widget.ListAdapter
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnScanQR -> {
                intentIntegrator.setBeepEnabled(true).initiateScan()
            }
            R.id.btnGenerateQR -> {
                val barCodeEncoder = BarcodeEncoder()
                val bitmap = barCodeEncoder.encodeBitmap(edQRCode.text.toString(),
                    BarcodeFormat.QR_CODE,400,400)
                imV.setImageBitmap(bitmap)
            }
            R.id.btnMasukkan -> {
                val strToken = StringTokenizer(edQRCode.text.toString(),";",false)
                edNIM.setText(strToken.nextToken())
                edNama.setText(strToken.nextToken())
                edProdi.setText(strToken.nextToken())
            }
            R.id.btnSimpan -> {
                dialog.setTitle("Konfirmasi").setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("Apakah data yang akan dimasukkan sudah benar?")
                    .setPositiveButton("Ya",btnInsertDialog)
                    .setNegativeButton("Tidak",null)
                dialog.show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if (intentResult!=null){
            if(intentResult.contents != null){
                edQRCode.setText(intentResult.contents)
            }else{
                Toast.makeText(this,"Dibatalkan",Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    lateinit var  intentIntegrator: IntentIntegrator
    lateinit var db : SQLiteDatabase
    lateinit var dialog: AlertDialog.Builder
    lateinit var lsAdapter: ListAdapter
    lateinit var adapter : ListAdapter
    lateinit var builder : AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intentIntegrator = IntentIntegrator(this)
        btnGenerateQR.setOnClickListener(this)
        btnScanQR.setOnClickListener(this)

        btnMasukkan.setOnClickListener(this)
        db = DBOpenHelper(this).writableDatabase
        btnSimpan.setOnClickListener(this)
        dialog = AlertDialog.Builder(this)
        db = DBOpenHelper(this).writableDatabase
    }


    fun showDataMhs(){
        val cursor : Cursor = db.query("mhs",arrayOf("nim as _id", "nama","prodi"),
            null,null,null,null,"_id")
        adapter = SimpleCursorAdapter(this, R.layout.item_data,cursor,
            arrayOf("_id","nama","prodi"), intArrayOf(R.id.tvNIM, R.id.tvNama,R.id.tvProdi),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        lsData.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        showDataMhs()
    }

    fun insertDataMhs(nim : String, nama : String, prodi : String){
        var sql = "insert into mhs(nim, nama, prodi) values(?,?,?)"
        db.execSQL(sql, arrayOf(nim,nama,prodi))
        showDataMhs()
    }

    val btnInsertDialog = DialogInterface.OnClickListener{ dialog, which ->
        insertDataMhs(edNIM.text.toString(), edNama.text.toString(),edProdi.text.toString())
        edNIM.setText("")
        edNama.setText("")
        edProdi.setText("")
    }

    fun getDbObject() : SQLiteDatabase{
        return db
    }

}