package com.example.whatsapp.presentation.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
@Preview(showSystemUi = true)
fun HomeScreen() {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {/*TODO*/}) {

            }
        }
    ){
        Column (modifier = Modifier.padding(it)){
            Text(text="123")
        }
    }
}