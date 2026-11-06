package lat.pam.gayocoffeeshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lat.pam.gayocoffeeshop.R

@Composable
fun CoffeeLogo(
    modifier: Modifier = Modifier,
    size: Dp = 180.dp
) {
    Image(
        painter = painterResource(id = R.drawable.logo_gayo),
        contentDescription = "Gayo Coffee House Logo",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
    )
}
