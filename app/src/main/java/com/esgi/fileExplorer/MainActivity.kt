package com.esgi.fileExplorer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var recyclerView: RecyclerView
    private lateinit var path : String
    private var originPath = "/sdcard"
    private var files: Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            this.init()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }
    }

    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 0){
            if(!grantResults.contains(PackageManager.PERMISSION_DENIED)) this.init()
        }
    }

    private fun init(){
        this.path = this.originPath
        val tmpList = File(path).listFiles()
        this.files = File(path).list()!!
        for(i in tmpList!!.indices){
            this.files[i] = tmpList[i].absolutePath
        }
        viewAdapter = RecyclerAdapter(dataset = this.files as Array<String>, onClickListener = this::onClickItem)
        viewManager = LinearLayoutManager(this)

        this.recyclerView = findViewById(R.id.filesRecyclerView)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        this.reloadFiles()
    }

    override fun onBackPressed() {
        val current = File(this.path)
        if(current.parentFile?.absolutePath == "/" ){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Quitter ?")
            builder.setMessage("Êtes-vous sûr de vouloir quitter ?")
            builder.setPositiveButton(android.R.string.yes){ _,_ ->
                super.onBackPressed()
            }
            builder.setNegativeButton(android.R.string.no) { _,_ ->
                //YOU DO NOTHING JOHN SNOW
            }
            builder.show()
        } else {
            this.path = current.parentFile.absolutePath
            reloadFiles()
        }
    }

    private fun onClickItem(view: View, filename: String){
        if(this.path.indexOf(filename) == -1){
            this.originPath = this.path
            this.path = File(filename).absolutePath
        }
        reloadFiles()
    }

    private fun reloadFiles(){
        val tmp = File(this.path)
        if(tmp.canRead() && tmp.isDirectory) {
            val tmpList = File(path).listFiles()
            this.files = File(path).list()
            for(i in tmpList.indices){
                this.files[i] = tmpList[i].absolutePath
            }
            if(this.files.isNullOrEmpty()){
                println("EMPTY")
                recyclerView.adapter = EmptyAdapter()
            } else {
                Arrays.sort(this.files)
                recyclerView.adapter = RecyclerAdapter(this.files, this::onClickItem)
                println("FILES SIZE -> "+ this.files.size)
            }
            viewAdapter.notifyDataSetChanged()
        } else if(tmp.isFile){
            val openFile = Intent(Intent.ACTION_VIEW)
            var fileUri = FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".provider", tmp)
            openFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            openFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            openFile.setData(fileUri)
            val open = Intent.createChooser(openFile, "Open with : ")
            try {
                startActivity(open)
            } catch (e: ActivityNotFoundException) {
                println(e.localizedMessage)
            }
            this.path = tmp.parentFile.absolutePath
        }
        this.files = emptyArray()
    }
}