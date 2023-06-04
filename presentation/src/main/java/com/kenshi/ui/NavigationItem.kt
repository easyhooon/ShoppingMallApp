package com.kenshi.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.*
import com.kenshi.domain.model.Category
import com.kenshi.ui.component.NavigationRouteName.DEEP_LINK_SCHEME
import com.kenshi.ui.component.NavigationTitle.MAIN_CATEGORY
import com.kenshi.ui.component.NavigationTitle.MAIN_HOME
import com.kenshi.ui.component.NavigationTitle.MAIN_LIKE
import com.kenshi.ui.component.NavigationTitle.MAIN_MY_PAGE
import com.kenshi.utils.GsonUtils

//sealed class NavigationItem(open val route: String) {
//    sealed class MainNav(override val route: String, val icon: ImageVector, val name: String) : NavigationItem(route) {
//        object Home : MainNav(MAIN_HOME, Icons.Filled.Home, MAIN_HOME)
//        object Category : MainNav(MAIN_CATEGORY, Icons.Filled.Star, MAIN_CATEGORY)
//        object MyPage : MainNav(MAIN_MY_PAGE, Icons.Filled.AccountBox, MAIN_MY_PAGE)
//        object Like : MainNav(MAIN_LIKE, Icons.Filled.Favorite, MAIN_LIKE)
//
//        companion object {
//            // 메인 화면일 경우만 헤더와 바텀 네비게이션이 보이도록
//            fun isMainRoute(route: String?): Boolean {
//                return when (route) {
//                    MAIN_HOME, MAIN_LIKE, MAIN_CATEGORY, MAIN_MY_PAGE -> true
//                    else -> false
//                }
//            }
//        }
//    }
//
//    // 홈 화면이 아닌 경우
//    data class CategoryNav(val category: Category) : NavigationItem(CATEGORY)
//
//    data class ProductDetailNav(val product: Product) : NavigationItem(PRODUCT_DETAIL)
//
//    object SearchNav : NavigationItem(PRODUCT_DETAIL)
//
//    object Basket : NavigationItem(BASKET)
//}

sealed class MainNav(override val route: String, val icon: ImageVector, override val title: String) : Destination {
    object Home : MainNav(MAIN_HOME, Icons.Filled.Home, MAIN_HOME)
    object Category : MainNav(MAIN_CATEGORY, Icons.Filled.Star, MAIN_CATEGORY)
    object MyPage : MainNav(MAIN_MY_PAGE, Icons.Filled.AccountBox, MAIN_MY_PAGE)
    object Like : MainNav(MAIN_LIKE, Icons.Filled.Favorite, MAIN_LIKE)

    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME${SearchNav.route}" }
    )

    companion object {
        // 메인 화면일 경우만 헤더와 바텀 네비게이션이 보이도록
        fun isMainRoute(route: String?): Boolean {
            return when (route) {
                MAIN_HOME, MAIN_LIKE, MAIN_CATEGORY, MAIN_MY_PAGE -> true
                else -> false
            }
        }
    }
}

object SearchNav : Destination {
    override val route: String = NavigationRouteName.SEARCH
    override val title: String = NavigationTitle.SEARCH
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$route" }
    )
}

object BasketNav : Destination {
    override val route: String = NavigationRouteName.BASKET
    override val title: String = NavigationTitle.BASKET
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$route" }
    )
}

object PurchaseHistoryNav : Destination {
    override val route: String = NavigationRouteName.PURCHASE_HISTORY
    override val title: String = NavigationTitle.PURCHASE_HISTORY
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$route" }
    )
}

object CategoryNav : DestinationArg<Category> {
    override val route: String = NavigationRouteName.CATEGORY
    override val title: String = NavigationTitle.CATEGORY
    override val argName: String = "category"
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$route/{$argName}" }
    )
    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(argName) { type = NavType.StringType }
    )

    // json 으로 변환함여 넘겨준다
    override fun navigateWithArg(item: Category): String {
        val arg = GsonUtils.toJson(item)
        return "$route/{$arg}"
    }

    override fun findArgument(navBackStackEntry: NavBackStackEntry): Category? {
        val categoryString = navBackStackEntry.arguments?.getString(argName)
        return GsonUtils.fromJson<Category>(categoryString)
    }
}

object ProductDetailNav : DestinationArg<String> {
    override val route: String = NavigationRouteName.PRODUCT_DETAIL
    override val title: String = NavigationTitle.PRODUCT_DETAIL
    override val argName: String = "productId"
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink { uriPattern = "$DEEP_LINK_SCHEME$route/{$argName}" }
    )
    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(argName) { type = NavType.StringType }
    )

    override fun navigateWithArg(item: String): String {
        val arg = GsonUtils.toJson(item)
        return "$route/{$arg}"
    }

    override fun findArgument(navBackStackEntry: NavBackStackEntry): String? {
        val categoryString = navBackStackEntry.arguments?.getString(argName)
        return GsonUtils.fromJson<String>(categoryString)
    }
}

// title, deepLink 가 모든 destination 에 존재한다고 가정하고 만듬
interface Destination {
    val route: String
    val title: String // 헤더에서 사용
    val deepLinks: List<NavDeepLink>
}

interface DestinationArg<T> : Destination {
    val argName: String // -> /{category} 같은 값을 대체
    val arguments: List<NamedNavArgument> // -> navArgument 를 대체

    fun routeWithArgName() = "$route/{$argName}"
    fun navigateWithArg(item: T): String
    fun findArgument(navBackStackEntry: NavBackStackEntry): T?
}

object NavigationRouteName {
    const val DEEP_LINK_SCHEME = "fastcampus://"
    const val MAIN_HOME = "main_home"
    const val MAIN_CATEGORY = "main_category"
    const val MAIN_MY_PAGE = "main_my_page"
    const val MAIN_LIKE = "main_like"
    const val CATEGORY = "category"
    const val PRODUCT_DETAIL = "product_detail"
    const val SEARCH = "search"
    const val BASKET = "basket"
    const val PURCHASE_HISTORY = "purchase_history"
}

object NavigationTitle {
    const val MAIN_HOME = "홈"
    const val MAIN_CATEGORY = "카테고"
    const val MAIN_MY_PAGE = "마이페이지"
    const val MAIN_LIKE = "좋아요 모아보기"
    const val CATEGORY = "카테고리별 보기"
    const val PRODUCT_DETAIL = "상품 상세페이지"
    const val SEARCH = "검색"
    const val BASKET = "장바구니"
    const val PURCHASE_HISTORY = "결제내역"
}