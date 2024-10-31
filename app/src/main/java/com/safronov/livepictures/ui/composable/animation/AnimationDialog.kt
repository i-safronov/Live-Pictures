package com.safronov.livepictures.ui.composable.animation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.safronov.livepictures.ui.composable.PathData
import com.safronov.livepictures.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationDialog(
    modifier: Modifier = Modifier,
    text: String = "Made with care from Yandex ❤\uFE0F",
    loadingDescription: String = "Please wait, we are preparing an interesting video for you",
    animation: List<PathData>,
    isLoading: Boolean = false,
    properties: DialogProperties = DialogProperties(
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false,
        dismissOnBackPress = true
    ),
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        modifier = modifier
            .fillMaxSize()
            .background(Colors.TransparentGray)
            .padding(
                top = 72.dp,
                bottom = 72.dp,
                end = 24.dp,
                start = 24.dp,
            )
            .clip(RoundedCornerShape(size = 20.dp)),
        onDismissRequest = onDismiss,
        properties = properties,
        content = {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 32.dp),
                verticalArrangement = Arrangement
                    .spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Colors.LightTextColor
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(80.dp),
                        color = Colors.Blue
                    )

                    Text(
                        text = loadingDescription,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Colors.LightTextGray
                    )
                } else {
                    SmoothAnimatedPathsCanvas(
                        modifier = Modifier
                            .clip(RoundedCornerShape(size = 20.dp))
                            .fillMaxSize()
                            .background(Colors.White),
                        paths = animation
                    )
                }
            }
        }
    )
}