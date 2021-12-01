package com.example.composesimplelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.composesimplelist.ui.theme.ComposeSimpleListTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSimpleListTheme {
                MyApp(viewModel)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun MyApp(viewModel: MainActivityViewModel) {

    var shouldShowOnBoarding by rememberSaveable { mutableStateOf(true) }

    if (shouldShowOnBoarding) {
        OnBoardingScreen (onContinueClicked = { shouldShowOnBoarding = false })
    } else {
        List(viewModel.listOfItems)
    }
}

@Composable
fun OnBoardingScreen(onContinueClicked: () -> Unit) {

    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Text("Welcome to my App!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text(text = "Continue")
            }
        }    
    }
}

@ExperimentalAnimationApi
@Composable
fun List(names: MutableList<Person>) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp), state = listState) {
        items(names) { name ->
            ListItem(person = name)
        }
    }

    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    
    AnimatedVisibility(visible = showButton) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp)
        ) {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Filled.ArrowUpward, contentDescription = "Up") },
                text = { Text(text = "Up") },
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .height(36.dp)
            )
        }
    }
}

@Composable
fun ListItem(person: Person) {

    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
        ) {

        Column(modifier = Modifier
            .animateContentSize(
                animationSpec = spring (
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )) {
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface (
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = "https://mlove.3vozrast.ru/media/images/polls/748/tb_poll.jpg"
                        ),
                        contentDescription = "Person image",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                    )
                }

                Column(modifier = Modifier
                    .weight(1f)
                ) {
                    Text(
                        text = person.name,
                        style = MaterialTheme.typography.subtitle2
                    )
                    Text(
                        text = "I am ${person.age} years old",
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded}
                ) {
                    Icon(
                        if (!isExpanded) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
                        contentDescription = "Expand Button")
                }


            }

            if (isExpanded) {
                Text(
                    text = "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Maecenas rutrum finibus sem, in gravida tellus sollicitudin vitae. Etiam leo metus, gravida eget nisl ac, volutpat suscipit urna.",
                    modifier = Modifier.padding(16.dp),

                    )
            }
        }
        

        
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true, widthDp = 320, heightDp = 620)
@Composable
fun DefaultPreview() {
    ComposeSimpleListTheme {
        List(names = MutableList<Person>(10) {Person("Andrew", 18, "")} )
    }
}