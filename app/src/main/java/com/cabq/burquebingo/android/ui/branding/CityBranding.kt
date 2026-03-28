package com.cabq.burquebingo.android.ui.branding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.cabq.burquebingo.android.R

private const val WORDMARK_ASSET = "file:///android_asset/branding/city_of_albuquerque_wordmark_header.svg"

@Composable
fun rememberSvgImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember(context.applicationContext) {
        ImageLoader.Builder(context.applicationContext)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }
}

/** Official-style horizontal wordmark (SVG from Flutter `city_of_albuquerque_wordmark_header.svg`). */
@Composable
fun CityOfAlbuquerqueWordmark(
    modifier: Modifier = Modifier,
    maxHeight: Dp = 56.dp,
) {
    val context = LocalContext.current
    val loader = rememberSvgImageLoader()
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(WORDMARK_ASSET)
            .crossfade(true)
            .build(),
        contentDescription = "City of Albuquerque",
        imageLoader = loader,
        modifier = modifier
            .fillMaxWidth()
            .height(maxHeight),
        contentScale = ContentScale.Fit,
        alignment = Alignment.CenterStart,
    )
}

/** Gray City seal only; matches Flutter `CitySealBadge`. */
@Composable
fun CitySealBadge(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
) {
    Image(
        painter = painterResource(R.drawable.city_seal),
        contentDescription = "City of Albuquerque seal",
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center,
    )
}
