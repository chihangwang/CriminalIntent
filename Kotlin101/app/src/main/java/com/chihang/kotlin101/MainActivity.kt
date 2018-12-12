package com.chihang.kotlin101

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun toastMe(view: View) {
    val toast = Toast.makeText(this, "Hello Toast!", Toast.LENGTH_SHORT)
    toast.show()
  }

  fun count(view: View) {
    val showCountTextView = findViewById<TextView>(R.id.textView)
    var count = Integer.valueOf(showCountTextView.text.toString()) + 1
    showCountTextView.setText(count.toString())
  }

  fun random(view: View) {
    val intent = Intent(this, Main2Activity::class.java)
    val count = Integer.valueOf(findViewById<TextView>(R.id.textView).text.toString())
    intent.putExtra(Main2Activity.TOTAL_COUNT, count)
    startActivity(intent)
  }
}
