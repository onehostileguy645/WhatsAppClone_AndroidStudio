package com.example.whatsapp.presentation.updatescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsapp.R
import com.example.whatsapp.presentation.bottomnavigation.BottomNavigation

@Composable
@Preview(showSystemUi = true)
fun UpdateScreen(){

    val sampleitem = listOf(
        StatusData(R.drawable.bhuvan_bam, "Ktoto", "Just Now"),
        StatusData(R.drawable.bhuvan_bam, "Ktoto", "Just Now"),
        StatusData(R.drawable.bhuvan_bam, "Ktoto", "Just Now"),
        StatusData(R.drawable.bhuvan_bam, "Ktoto", "Just Now"),
        StatusData(R.drawable.bhuvan_bam, "Ktoto", "Just Now"),
    )
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {/*TODO*/},
                containerColor = colorResource(id = R.color.light_green),
                modifier = Modifier.size(65.dp),
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                    contentDescription = null,
                )
            }
        },
        bottomBar = {
            BottomNavigation()
        },
        topBar = {
            TopBar()
        }
    ){
        Column (
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            Text(
                text = "Status",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            MyStatus()


            sampleitem.forEach {data ->
                StatusItem(data)
            }
        }
    }
}