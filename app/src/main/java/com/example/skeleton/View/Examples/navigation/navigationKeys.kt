package com.example.skeleton.View.Examples.navigation

import kotlinx.serialization.Serializable

import androidx.navigation3.runtime.NavKey

@Serializable
data object LatLong : NavKey

@Serializable
data object Predict : NavKey

@Serializable
data object Maps : NavKey

@Serializable
data object Aqi : NavKey

@Serializable
data object Result : NavKey

@Serializable
data object Analytics : NavKey
