package com.example.connexiopostgresql



import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.DriverManager


class MainActivity : AppCompatActivity() {
    internal var cont: String = ""


    private var sqlThread: Thread = object : Thread() {
        override fun run() {

            val conn = DriverManager.getConnection(
                "jdbc:postgresql://89.36.214.106:5432/geo_ad",
                "geo_ad",
                "geo_ad"
            )

            val sentencia = "SELECT * FROM COMARCA ORDER BY 1"
            val st = conn.createStatement()
            val rs = st.executeQuery(sentencia)
            while (rs.next()) {
                cont+=(rs.getString(1) + " - " + rs.getString(2) + "\n")
            }
            rs.close()
            conn.close()


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Des de la versió 3 d'Android, no es permet obrir una connexió des del thread principal.
        // Per tant s'ha de crear un nou.
        sqlThread.start()

        // i ara esperem a que finalitze el thread fill unint-lo (join)
        try {
            sqlThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        text.setText(cont)

    }
}

