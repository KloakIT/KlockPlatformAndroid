package com.kloak.platform

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.VideoView
import androidx.annotation.RequiresApi
import java.io.*

class MainActivity : AppCompatActivity() {

    var _startedNodeAlready = false
    val nodeDir = "nodejs_project"
    lateinit var infoTextView: TextView
    var nodeJsStartPath = ""
    var dataRootPath = ""
    val totalFiles = 4655 + 290 + 2
    var copyFileCount = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate (savedInstanceState: Bundle? ) {
        super.onCreate ( savedInstanceState )
        setContentView ( R.layout.activity_main )
        infoTextView = findViewById<TextView>( R.id.textView )
        // Example of a call to a native method
        infoTextView.text = "Kloak platform start up"
        val vid = findViewById<VideoView>( R.id.videoView )
        val path = "android.resource://com.kloak.platform/" + R.raw.startup_background
        val u = Uri.parse( path )
        vid.setVideoURI( u )

        vid.setOnPreparedListener { mp -> mp.isLooping = true }
        vid.start()
        checkNodeJsProject( infoTextView )
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    external fun startNodeWithArguments ( arguments: Array < String > ): Integer

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("node")
        }
    }


    fun checkNodeJS (textView : TextView) {
        textView.text = "_startedNodeAlready = " + _startedNodeAlready
        if ( !_startedNodeAlready ) {
            _startedNodeAlready = true


            val startupPath = "$nodeJsStartPath/$nodeDir/main.js"
            println ("nodeJS start at $startupPath")
            Thread ( Runnable {

                startNodeWithArguments ( arrayOf ("node",
                        startupPath, "$nodeJsStartPath/$nodeDir/"
                    //startupPath
                    //"console.log ('startup nodejs')"
                /*
                    "-e",
                    "var Fs = require('fs');" +
                            //"var lookFIle = '/data/data/com.kloak.platform/files/nodejs_project/app/views/Shared/coreLayout.pug';" +
                            "var lookFIle = '/data/data/com.kloak.platform/files/nodejs_project/app/views/Shared/';" +
                            "var ss= Fs.readdirSync(lookFIle);" +
                            "var sp = Fs.statSync (lookFIle);" +
                    "var http = require('http'); " +
                            "var versions_server = http.createServer( (request, response) => {" +
                                "response.end('Versions: ' + JSON.stringify(sp) + ss );" +
                            "});" +
                            //"http.on('error', err => { console.log (err);});" +
                            "versions_server.listen(3000);"

                    //"const localWebServer = require ('/app/localWebServer');" + "new localWebServer ( 3000, '');"

                    //"console.log (__dirname);"
                */
                    ))

            }).start ()


        }
    }

    fun readAssetsFile ( path: String ) {
        val str = assets.open ( path ).bufferedReader().use {
            it.readText()
        }
        println ( str )
    }

    fun copyAssetFile ( formPath: String, toPath: String ) {

        val buffer = ByteArray(1024)
            assets.open ( formPath ).use { input ->
                val out = File ( dataRootPath, toPath )
                    out.createNewFile()
                println ("create file ${out.absoluteFile}")
                    out.outputStream().use { fileOut ->
                        while (true) {
                            val length = input.read(buffer)
                            if (length <= 0) {
                                break
                            }
                            fileOut.write(buffer, 0, length)
                        }
                        fileOut.flush()
                        fileOut.close()
                        println ("${out.absoluteFile} length = [${out.length()}]")
                    }

            }
        //println ("copyAssetFile formPath[$formPath] toPath [${ff.absoluteFile}] ")


            //val to =
            //var toStream = FileOutputStream ( to.absoluteFile )
            //to.createNewFile()
            /*
            val info = "Coping file [$formPath] to [${to.absolutePath}] total [$copyFileCount]"


                var buffer = ByteArray(ass.available())
                var offset = 0
                var read = ass.read ( buffer )

                while ( read != -1 ) {
                    println("buffer.size=[$buffer.size] read = [$read] offset = [$offset]")
                    offset += read
                    toStream.write( buffer, 0, read )
                    read = ass.read ( buffer )

                }
        ass.close()
        toStream.flush()
        toStream.close()
        */
        copyFileCount ++
        println ("[$formPath] total = [$copyFileCount]")

            //

            //println ( info )

        return
    }



    fun copyNodeJSProject (formPath: String, toPath: String ) {
        val asset11 = assets.list ( formPath ) ?: return

        if ( asset11.size == 0 ) {
            return copyAssetFile ( formPath, toPath )
        }

        File ( dataRootPath, toPath ).mkdir()

        asset11.forEach {
            println ("==================================> start copy path [$toPath/$it]")
            copyNodeJSProject("$formPath/$it", "$toPath/$it")
        }

    }


    fun checkNodeJsProject (textView: TextView ) {

        val nodePath = File ( applicationContext.filesDir, "" )
        dataRootPath = nodePath.absolutePath

        dataRootPath = dataRootPath.replace("/data/user/0/", "/data/data/")
        nodeJsStartPath = dataRootPath

        //if ( !nodePath.exists() ) {
            //nodePath.mkdir()
        //readAssetsFile ( "$nodeDir/package.json")
        //val back = File(dataRootPath,"$nodeDir/backup1")
        //back.mkdir()
        //println ("${back.absoluteFile} exists is ${back.exists()}")
        //copyAssetFile ("$nodeDir/package.json", "$nodeDir/backup1/package.json")

            copyNodeJSProject ( nodeDir, nodeDir )
        //}
        checkNodeJS ( textView )
    }

}