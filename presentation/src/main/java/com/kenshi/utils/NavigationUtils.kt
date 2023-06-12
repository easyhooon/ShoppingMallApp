package com.kenshi.utils

import androidx.navigation.NavHostController
import com.kenshi.ui.component.*

object NavigationUtils {

//      // 개선 이전
//    fun navigate(
//        controller: NavHostController,
//        routeName: String,
//        args: Any? = null,
//        backStackRouteName: String? = null,
//        isLaunchSingleTop: Boolean = true,
//        needToRestoreState: Boolean = true
//    ) {
//        var argument = ""
//        if (args != null) {
//            when(args) {
//                is Parcelable -> {
//                    argument = String.format("%s", Uri.parse(Gson().toJson(args)))
//                }
//                is Category -> {
//                    argument = String.format("%s", Uri.parse(Gson().toJson(args)))
//                }
//                is Product -> {
//                    argument = String.format("%s", args.productId)
//                }
//            }
//        }
//        controller.navigate("$routeName$argument") {
//            if(backStackRouteName != null) {
//                popUpTo(backStackRouteName) {
//                    saveState = true
//                }
//                launchSingleTop = isLaunchSingleTop
//                restoreState = needToRestoreState
//            }
//        }
//    }

    // 개선 후
    fun navigate(
        navController: NavHostController,
        routeName: String,
        backStackRouteName: String? = null,
        isLaunchSingleTop: Boolean = true,
        needToRestoreState: Boolean = true
    ) {
        navController.navigate(routeName) {
            if (backStackRouteName != null) {
                popUpTo(backStackRouteName) {
                    saveState = true
                }
                launchSingleTop = isLaunchSingleTop
                restoreState = needToRestoreState
            }
        }
    }

    fun findDestination(route: String?): Destination {
        return when (route) {
            // 파라미터가 없는 경우
            NavigationRouteName.MAIN_MY_PAGE -> MainNav.MyPage
            NavigationRouteName.MAIN_LIKE -> MainNav.Like
            NavigationRouteName.MAIN_HOME -> MainNav.Home
            NavigationRouteName.MAIN_CATEGORY -> MainNav.Category
            NavigationRouteName.SEARCH -> SearchNav
            NavigationRouteName.BASKET -> BasketNav
            NavigationRouteName.PURCHASE_HISTORY -> PurchaseHistoryNav

            // 파라미터가 있는 경우
            ProductDetailNav.routeWithArgName() -> ProductDetailNav
            CategoryNav.routeWithArgName() -> CategoryNav
            else -> MainNav.Home
        }
    }
}