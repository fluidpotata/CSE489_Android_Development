package com.fluidpotata.assignemnt2

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import com.fluidpotata.assignemnt2.R


@Composable
fun ImageViewerScreen() {
    SimpleImageViewer(
        drawableRes = R.drawable.sample_image,
        contentDescription = "Sample zoomable image"
    )
}



@Composable
fun SimpleImageViewer(
    drawableRes: Int,
    contentDescription: String? = null
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 5f)
                    offset += pan
                }
            },
        contentAlignment = Alignment.Center
    ) {
        val painter = painterResource(id = drawableRes)

        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .fillMaxSize()
        )
    }
}
