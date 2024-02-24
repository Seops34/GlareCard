package com.seop.glarecard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seop.glarecard.ui.theme.Purple
import com.seop.glarecard.ui.theme.White
import com.seop.glarecard.ui.theme.Yellow


@Composable
fun Screen() {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            GlareCard()
        }
    }
}


@Composable
fun GlareCard() {
    val density = LocalDensity.current
    val targetWidth = with(density) { 200.dp.toPx() }
    val targetHeight = with(density) { 300.dp.toPx() }

    var rotationX by remember { mutableStateOf(0f) }
    var rotationY by remember { mutableStateOf(0f) }

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val path = Path().apply {
        moveTo(targetWidth / 5, 0f)
        lineTo(targetWidth / 5 * 2, 0f)
        lineTo(targetWidth / 5, targetHeight)
        lineTo(0f, targetHeight)
        close()
    }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount.x).coerceIn(0f, targetWidth)
                        offsetY = (offsetY + dragAmount.y).coerceIn(0f, targetHeight)
                        // 일차 함수를 이용한 계산
                        rotationY = (0.1f * offsetX - 10).coerceIn(-10f, 10f)
                        rotationX = (-1 * 0.07f * offsetY + 10).coerceIn(-10f, 10f)
                    },
                    onDragStart = { offset ->
                        offsetX = offset.x
                        offsetY = offset.y
                    }
                )
            }
            .graphicsLayer {
                this@graphicsLayer.rotationY = rotationY
                this@graphicsLayer.rotationX = rotationX
            }
            .size(200.dp, 300.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.img_picka2),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Canvas(
            modifier = Modifier
                .size(200.dp, 300.dp)
                .graphicsLayer {
                    this.translationX = offsetX
                },
            onDraw = {
                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        colors = listOf(Yellow, Purple),
                        tileMode = TileMode.Decal
                    ),
                    alpha = 0.4f,
                    colorFilter = ColorFilter.lighting(Yellow, White),
                    blendMode = BlendMode.ColorDodge
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGlareCard() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GlareCard()
    }
}