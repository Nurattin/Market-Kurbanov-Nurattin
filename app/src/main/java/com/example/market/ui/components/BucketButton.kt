package com.example.market.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.market.R


@Composable
fun BucketButton(
    modifier: Modifier = Modifier,
) {
    var bucketCount by remember {
        mutableIntStateOf(0)
    }
    OutlinedButton(
        modifier = modifier,
        onClick = {
            bucketCount++
        },
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, Color.Gray),
        enabled = bucketCount <= 0,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
        )
    ) {
        if (bucketCount > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null,
                        ) {
                            bucketCount--
                        },
                    imageVector = ImageVector.vectorResource(R.drawable.ic_minus),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
                AnimatedNumber(
                    counter = bucketCount,
                )
                Icon(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null,
                        ) {
                            bucketCount++
                        },
                    imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        } else {
            Icon(
                imageVector = Icons.Rounded.AddShoppingCart,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedNumber(counter: Int) {

    AnimatedContent(
        targetState = counter,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInVertically(initialOffsetY = { height -> height }) + fadeIn()).togetherWith(
                    slideOutVertically(targetOffsetY = { height -> -height }) + fadeOut()
                )
            } else {
                slideInVertically(initialOffsetY = { height -> -height }) + fadeIn() with
                        slideOutVertically(targetOffsetY = { height -> height }) + fadeOut()
            }
        }
    ) { targetCount ->
        Text(
            text = targetCount.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}