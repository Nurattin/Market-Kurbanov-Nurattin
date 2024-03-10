package com.example.market.ui.screens.main.products.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.market.ui.theme.MarketTheme

@Preview
@Composable
private fun PreviewProductPrice() {
    MarketTheme {
        Column {
            ProductPrice(
                price = 65_990
            )
            ProductPrice(
                price = 65_990,
                discountPercentage = 12
            )
        }

    }
}

@Composable
fun ProductPrice(
    price: Int,
    discountPercentage: Int? = null,
) {

    val oldPrice = discountPercentage?.let {
        price / (1 - it / 100f)
    }
    val textColor = MaterialTheme.colorScheme.onBackground

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = if (oldPrice == null) textColor else Color.Red,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )
        ) {
            append(String.format("%,d ₽", price))
            append(" ")
        }
        if (oldPrice != null) {
            withStyle(
                style = SpanStyle(
                    color = textColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.LineThrough
                )
            ) {
                append(String.format("%,d ₽", oldPrice.toInt()))
            }
        }
    }

    Text(text = annotatedString)
}
