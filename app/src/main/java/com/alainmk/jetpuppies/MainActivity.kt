package com.alainmk.jetpuppies

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.alainmk.jetpuppies.model.Puppy
import com.alainmk.jetpuppies.model.puppies
import com.alainmk.jetpuppies.ui.theme.JetPuppiesTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPuppiesTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "home") {
                    composable("home") {
                        MyApp { MyScreenContent(navController) }
                    }
                    composable("detail/{puppyId}") { backStackEntry ->
                        MyApp { DetailScreen(backStackEntry.arguments?.getString("puppyId")) }
                    }
                }

            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Surface {
        content()
    }
}

@Composable
fun MyScreenContent(navController: NavController) {
    val context = LocalContext.current
    Scaffold(  topBar = {
        TopAppBar(
            title = { Text("JetPuppies") },
            actions = {
                IconButton(onClick = {
                    Toast.makeText(
                        context,
                        "I love Jetpack Compose",
                        Toast.LENGTH_SHORT
                    ).show() }) {
                    Icon(Icons.Default.Favorite, "favorite")
                }
            }
        )
    }) {
        PuppiesList(puppies, navController)
    }
}

@Composable
fun PuppiesList(puppies: List<Puppy>, navController: NavController) {
    LazyColumn {
        items(items = puppies) { puppy ->
            PuppyItem(puppy, navController)
        }
    }
}

@Composable
fun PuppyItem(puppy: Puppy, navController: NavController) {
    Column(
        Modifier
            .background(color = MaterialTheme.colors.surface)
            .clickable
                (
                onClick = {
                    navController.navigate("detail/${puppy.puppyId}")
                }
            )
    ) {
        Row(modifier = Modifier
            .padding(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
            ) {
                Image(
                    painter = painterResource(id = puppy.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Row {
                    Text(puppy.name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text(puppy.birthDate, style = MaterialTheme.typography.body2)
                }
                //Spacer(modifier = Modifier.size(8.dp))
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        puppy.habits,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
        Divider()
    }
}

@Composable
fun DetailScreen(puppyId: String?) {
    val context = LocalContext.current
    Scaffold(  topBar = {
        TopAppBar(
            title = { Text("Detail") },
            actions = {
                IconButton(onClick = {
                    Toast.makeText(
                        context,
                        "I love Jetpack Compose $puppyId",
                        Toast.LENGTH_SHORT
                    ).show() }) {
                    Icon(Icons.Default.Favorite, "favorite")
                }
            }
        )
    }) {
        val puppy = puppies.first { it.puppyId == puppyId?.toInt() }
        val typography = MaterialTheme.typography
        Column(modifier = Modifier.background(color = MaterialTheme.colors.surface).padding(16.dp)) {
            Image(
                painter = painterResource(id = puppy.image),
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(16.dp))
            Row {
                Text(
                    puppy.name,
                    style = typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(puppy.birthDate, style = MaterialTheme.typography.body2)
            }
            Spacer(modifier = Modifier.size(15.dp))
            Text(puppy.habits, style = MaterialTheme.typography.subtitle1)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetPuppiesTheme {
        //DetailScreen()
    }
}