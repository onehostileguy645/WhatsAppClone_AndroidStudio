package com.example.whatsapp.presentation.callscreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsapp.R

@Composable
@Preview(showSystemUi = true)
fun FavoritesSection(){

    val sampleFavorites = listOf(
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali"),
        FavoritesContact(R.drawable.ssa, name = "Aliyev Vali")
    )

    Column (
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    ){
        Text(
            "Favorites",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row (modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())){
            sampleFavorites.forEach {
                FavoritesItem(it)
            }
        }
    }
}


data class FavoritesContact(
    val image:Int,
    val name:String
)