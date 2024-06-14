package com.example.firebaseapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.firebaseapp.data.Book
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        val fs = Firebase.firestore
        val storage = Firebase.storage.reference.child("images")
        val context = LocalContext.current

        val list = remember {
            mutableStateOf(emptyList<Book>())
        }

        fs.collection("Books").addSnapshotListener { snapshot, exception ->
            list.value = snapshot?.toObjects(Book::class.java) ?: emptyList()
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                items(list.value) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = it.imageUrl,
                                contentDescription = "",
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(30.dp)
                            )
                            Text(
                                text = it.name, modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth()
                                    .padding(15.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {

//                    val task = storage.child("book.png").putBytes(bitmapToByteArray(context))
//                    task.addOnSuccessListener {
//                        it.metadata?.reference?.downloadUrl?.addOnCompleteListener {
//                            saveBook(fs, it.result.toString())
//                        }
//                    }

                }) {
                Text(text = "Add Book")
            }
        }
    }
}

private fun bitmapToByteArray(context: Context): ByteArray {
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.book)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
    return baos.toByteArray()
}

private fun saveBook(fs: FirebaseFirestore, url: String) {
    fs.collection("Books").document().set(
        Book(
            "My Book",
            "asdasd",
            "100",
            "Fantasy",
            url
        )
    )
}