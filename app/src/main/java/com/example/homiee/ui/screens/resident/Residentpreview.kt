package com.example.homiee.ui.screens.resident

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
private fun PreviewAddressFormScreen() {
    ResFormAddressScreen(
        houseNo = "", onHouseNoChange = {},
        area = "", onAreaChange = {},
        city = "", onCityChange = {},
        pincode = "", onPincodeChange = {},
        onUseCurrentLocation = {},
        onNext = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmergencyContactFormScreen() {
    ResFormEmergencyScreen(
        contactName = "", onContactNameChange = {},
        mobileNumber = "", onMobileNumberChange = {},
        onNext = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewIdentityVerificationFormScreen() {
    ResFormIdentityScreen(onUploadAadhaar = {}, onUploadPan = {}, onNext = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewResFormPhotoScreen() {
    ResFormPhotoScreen(onFinish = {})
}