package com.example.homiee.ui.screens.allpreviews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.homiee.ui.screens.resident.ResFormAddressScreen
import com.example.homiee.ui.screens.resident.ResFormPhotoScreen

@Preview(showBackground = true)
@Composable
private fun PreviewAddressFormScreen() {
    ResFormAddressScreen(
        houseNo = "", onHouseNoChange = {},
        area = "", onAreaChange = {},
        city = "", onCityChange = {},
        pincode = "", onPincodeChange = {},
        onUseCurrentLocation = { _, _ -> },
        onNext = {}
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewResFormPhotoScreen() {
    ResFormPhotoScreen(
        onFinish = {},
        onBack = {}
    )
}