package com.fluidpotata.assignemnt2

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.fluidpotata.assignemnt2.R


@Composable
fun ImageViewerScreen() {
    val imageUrl = "https://images.unsplash.com/photo-1610212570263-63e670490335"

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ScalableImage(
            imageUrl = imageUrl,
            localDrawable = R.drawable.sample_image, // Fallback if URL is null
            contentDescription = "Zoomable Image"
        )
    }
}


@Composable
fun ScalableImage(
    imageUrl: String? = null,
    localDrawable: Int? = null,
    contentDescription: String?
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(0.5f, 5f)
        offset += panChange
    }

    val painter = rememberAsyncImagePainter(
        model = imageUrl ?: localDrawable,
        placeholder = localDrawable?.let { painterResource(id = it) },
        error = painterResource(id = R.drawable.ic_launcher_foreground)
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = transformableState)
        )

        when (painter.state) {
            is AsyncImagePainter.State.Error -> {
                Text(
                    text = "Failed to load image.",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> Unit // Show nothing during loading or success
        }
    }
}

