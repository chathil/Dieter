package com.example.dieter.ui.screen.welcome

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.dieter.R
import com.example.dieter.ui.component.AppNameHeader
import com.example.dieter.ui.component.DieterDefaultButton
import com.example.dieter.ui.theme.AlphaNearTransparent
import com.example.dieter.ui.theme.DieterTheme
import com.example.dieter.utils.LocalSysUiController
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.toPaddingValues
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen(
    welcomeViewModel: WelcomeViewModel = viewModel(),
    welcomeFinished: () -> Unit,
    navigateToHome: () -> Unit
) {
    LocalSysUiController.current.setStatusBarColor(
        MaterialTheme.colors.background.copy(
            AlphaNearTransparent
        )
    )
    val pagerState = rememberPagerState(pageCount = 3)

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                Modifier
                    .padding(LocalWindowInsets.current.statusBars.toPaddingValues())
                    .padding(16.dp)
            )
            AppNameHeader()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                var currentPage by remember { mutableStateOf(0) }
                HorizontalPager(state = pagerState) { page ->
                    currentPage = this.currentPage
                    when (page) {
                        0 -> SlideOne(page = currentPage)
                        1 -> SlideTwo(page = currentPage)
                        2 -> SlideThree(page = currentPage)
                    }
                }
                /* TODO: https://google.github.io/accompanist/pager/ */
                SlideNavigation(currentPage = currentPage, navigateToHome = navigateToHome)
            }
        }
    }
}

@Composable
fun GlideGifImage(request: Int) {
    val requestManager = Glide.with(LocalContext.current).addDefaultRequestListener(object :
            RequestListener<Any> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Any>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Any?,
                model: Any?,
                target: Target<Any>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (resource is GifDrawable) {
                    resource.setLoopCount(1)
                }
                return false
            }
        })

    Surface(
        Modifier
            .fillMaxWidth()
            .height(312.dp)
    ) {
        Image(
            painter = rememberGlidePainter(
                request = request,
                requestManager = requestManager,
                fadeIn = true
            ),
            contentDescription = "cooking some meat"
        )
    }
}

@Composable
fun SlideOne(modifier: Modifier = Modifier, page: Int = 0) {

    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (page == 0)
            GlideGifImage(request = R.drawable.chef_illustration)
        else
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(312.dp)
            )

        Text(
            "Donec tempor commodo consectetur. Maecenas quis",
            style = MaterialTheme.typography.h6,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
        Text(
            "Aenean eu nibh nec velit vulputate bibendum. Vivamus condimentum, elit et tincidunt placerat, mi odio maximus mauris, at elementum",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SlideTwo(modifier: Modifier = Modifier, page: Int = 1) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (page == 1)
            GlideGifImage(request = R.drawable.workout_illustration)
        else
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(312.dp)
            )
        Text(
            "Morbi tincidunt mi sed congue blandit. Nunc euismod",
            style = MaterialTheme.typography.h6,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
        Text(
            "Aliquam fringilla sit amet ligula in laoreet. Mauris mauris magna, dignissim iaculis blandit in",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SlideThree(modifier: Modifier = Modifier, page: Int = 2) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (page == 2)
            GlideGifImage(request = R.drawable.login_illustration)
        else
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(312.dp)
            )
        Text(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
            style = MaterialTheme.typography.h6,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
        Text(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ante turpis, lacinia sit amet congue blandit, vehicula a metus.",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SlideNavigation(
    modifier: Modifier = Modifier,
    currentPage: Int = 0,
    navigateToHome: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 46.dp, start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = navigateToHome) {
            Text("Skip")
        }
        OutlinedButton(
            onClick = when (currentPage) {
                2 -> navigateToHome
                else -> {
                    {}
                }
            },
            modifier = Modifier
                .width(126.dp)
                .height(64.dp),
            border = BorderStroke(2.dp, MaterialTheme.colors.primary),
            colors = DieterDefaultButton.outlinedColors()
        ) {
            Crossfade(targetState = currentPage) { page ->
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (page) {
                        0 -> Text("Start")
                        1 -> Text("Next")
                        2 -> Text("Sign in")
                    }
                    Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = "next")
                }
            }
        }
    }
}

@Preview
@Composable
fun SlidePreview() {
    DieterTheme {
        SlideOne()
    }
}

@Preview
@Composable
fun SlideNavigationPreview() {
    DieterTheme {
        SlideNavigation()
    }
}
