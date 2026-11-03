package com.azurowski.clarus.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.azurowski.clarus.ui.components.nav.NavBar
import com.azurowski.clarus.ui.screens.HomeScreen
import com.azurowski.clarus.ui.screens.location.LocationChangeScreen
import com.azurowski.clarus.ui.screens.moon.MoonScreen
import com.azurowski.clarus.ui.screens.weather.FullWeatherScreen
import dev.chrisbanes.haze.HazeState
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun NavigationRoot() {
    val backstack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Home::class, Route.Home.serializer())
                    subclass(Route.LocationChange::class, Route.LocationChange.serializer())
                    subclass(Route.FullWeather::class, Route.FullWeather.serializer())
                    subclass(Route.Moon::class, Route.Moon.serializer())
                }
            }
        },
        Route.Home
    )

    val hazeState = remember { HazeState() }

    val currentRoute = backstack.lastOrNull()
    val selectedTabIndex = when (currentRoute) {
        is Route.Moon -> 1
        else -> 0
    }
    val animSpec = tween<Float>(durationMillis = 450, easing = FastOutSlowInEasing)
    Box(modifier = Modifier.fillMaxSize()) {
        NavDisplay(
            modifier = Modifier.fillMaxSize(),
            backStack = backstack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),

            transitionSpec = {
                (scaleIn(initialScale = 0.85f, animationSpec = animSpec) + fadeIn(animationSpec = animSpec))
                    .togetherWith(
                        scaleOut(targetScale = 1.15f, animationSpec = animSpec) + fadeOut(animationSpec = animSpec)
                    )
            },
            popTransitionSpec = {
                (scaleIn(initialScale = 1.15f, animationSpec = animSpec) + fadeIn(animationSpec = animSpec))
                    .togetherWith(
                        scaleOut(targetScale = 0.85f, animationSpec = animSpec) + fadeOut(animationSpec = animSpec)
                    )
            },
            predictivePopTransitionSpec = {
                (scaleIn(initialScale = 1.15f, animationSpec = animSpec) + fadeIn(animationSpec = animSpec))
                    .togetherWith(
                        scaleOut(targetScale = 0.85f, animationSpec = animSpec) + fadeOut(animationSpec = animSpec)
                    )
            },
            entryProvider = { key ->
                when (key) {
                    is Route.Home -> {
                        NavEntry(key) {
                            HomeScreen(
                                hazeState = hazeState,
                                toLocationChange = { longitude, latitude ->
                                    backstack.add(Route.LocationChange(longitude, latitude))
                                },
                                toFullWeather = {
                                    backstack.add(Route.FullWeather)
                                }
                            )
                        }
                    }

                    is Route.LocationChange -> {
                        NavEntry(key) {
                            LocationChangeScreen(
                                key.longitude,
                                key.latitude,
                                back = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                    }

                    is Route.FullWeather -> {
                        NavEntry(key) {
                            FullWeatherScreen(
                                back = {
                                    if (backstack.size > 1) {
                                        backstack.removeAt(backstack.lastIndex)
                                    }
                                }
                            )
                        }
                    }

                    is Route.Moon -> {
                        NavEntry(key) {
                            MoonScreen(
                                hazeState = hazeState
                            )
                        }
                    }

                    else -> error("Unknown route $key")
                }
            }
        )

        NavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedTabIndex = selectedTabIndex,
            hazeState = hazeState,
            Home = {
                if (currentRoute !is Route.Home) {
                    if (backstack.contains(Route.Home)) {
                        while (backstack.last() !is Route.Home) {
                            backstack.removeAt(backstack.lastIndex)
                        }
                    } else {
                        backstack.add(Route.Home)
                    }
                }
            },
            Moon = {
                if (currentRoute !is Route.Moon) {
                    backstack.add(Route.Moon)
                }
            }
        )
    }
}