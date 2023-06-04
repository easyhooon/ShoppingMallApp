package com.kenshi.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kenshi.ui.basket.BasketScreen
import com.kenshi.ui.category.CategoryScreen
import com.kenshi.ui.component.*
import com.kenshi.ui.main.LikeScreen
import com.kenshi.ui.main.MainCategoryScreen
import com.kenshi.ui.main.MainHomeScreen
import com.kenshi.ui.main.MyPageScreen
import com.kenshi.ui.product_detail.ProductDetailScreen
import com.kenshi.ui.purchase_history.PurchaseHistoryScreen
import com.kenshi.ui.search.SearchScreen
import com.kenshi.utils.NavigationUtils
import com.kenshi.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//TODO 뷰모델을 직접 파라미터로 넘기는거 안티 패턴이라고 알고 있는데..
@Composable
fun MainScreen(googleSignInClient: GoogleSignInClient) {
    val viewModel = hiltViewModel<MainViewModel>()
    val navController = rememberNavController()
    // TODO scaffold 의 state 를 관리 (어떤 state 를 관리)
    val scaffoldState = rememberScaffoldState()
    // TODO padding 값도 state 로 관리해야 하는 건가
    var padding by remember { mutableStateOf(PaddingValues()) }
    // TODO 정확한 쓰임세 확인
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            MainHeader(viewModel = viewModel, navController = navController, currentRoute = currentRoute)
        },
        scaffoldState = scaffoldState,
        bottomBar = {
            if (MainNav.isMainRoute(currentRoute)) {
                MainBottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        },
        // 결제가 완료 되었을 때 띄워줄 스낵바
        snackbarHost = {
            SnackbarHost(hostState = scaffoldState.snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data, modifier = Modifier.padding(50.dp),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        content = {
            // padding values 를 지정해줘야 Top Bar 에 의해 리스트 아이템이 가려지는 것을 방지할 수 있음
            padding = it
            MainNavigationScreen(viewModel = viewModel, navController = navController, googleSignInClient = googleSignInClient, scaffoldState = scaffoldState)
        }
    )
}


// TODO 이렇게 두개를 인자로 받다니.. 최악이다.
// Main 에서만 사용하는 헤더
@Composable
fun MainHeader(viewModel: MainViewModel, navController: NavHostController, currentRoute: String?) {
    TopAppBar(
        title = {
            Text(NavigationUtils.findDestination(currentRoute).title)
        },
        // Main 화면이 아닌 경우에 백 버튼을 넣어줌
        navigationIcon = {
            if (!MainNav.isMainRoute(currentRoute)) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "back_icon")
                }
            }
        },
        actions = {
            if (MainNav.isMainRoute(currentRoute)) {
                IconButton(onClick = {
                    viewModel.openSearchForm(navController)
                }) {
                    Icon(
                        Icons.Filled.Search,
                        "search_icon"
                    )
                }
                IconButton(onClick = {
                    viewModel.openBasket(navController)
                }) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        "shopping_icon"
                    )
                }
            }
        }
    )
}

@Composable
fun MainBottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?
) {
    val bottomNavigationItems = listOf(
        // NavigationItem.MainNav.Home,
        MainNav.Home,
        MainNav.Category,
        MainNav.MyPage,
        MainNav.Like
    )

    BottomNavigation {
        bottomNavigationItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, item.route) },
                selected = currentRoute == item.route,
                onClick = {
                    // 개선 이전
//                    navController.navigate(item.route) {
//                        navController.navigate(item.route) {
//                            navController.graph.startDestinationRoute?.let {
//                                popUpTo(it) {
//                                    saveState = true
//                                }
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
                    NavigationUtils.navigate(
                        controller = navController,
                        routeName = item.route,
                        navController.graph.startDestinationRoute
                    )
                },
            )
        }
    }
}

// TODO 모든 화면에 deepLinks argument 를 넣어주는 이유가 뭘까
@Composable
fun MainNavigationScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    scaffoldState: ScaffoldState
) {
    NavHost(
        navController = navController,
//        startDestination = NavigationRouteName.MAIN_HOME
        startDestination = MainNav.Home.route
    ) {
//        composable(NavigationRouteName.MAIN_HOME) {
        composable(
            route = MainNav.Home.route,
            deepLinks = MainNav.Home.deepLinks
        ) {
            MainHomeScreen(viewModel, navController)
        }
        composable(
            route = MainNav.Category.route,
            deepLinks = MainNav.Category.deepLinks
        ) {
            MainCategoryScreen(viewModel, navController)
        }
        composable(
            route = MainNav.MyPage.route,
            deepLinks = MainNav.MyPage.deepLinks
        ) {
            MyPageScreen(viewModel = viewModel, googleSignInClient = googleSignInClient, navHostController = navController)
        }
        composable(
            route = MainNav.Like.route,
            deepLinks = MainNav.Like.deepLinks
        ) {
            LikeScreen(viewModel, navController)
        }
        composable(
            route = BasketNav.route,
            deepLinks = BasketNav.deepLinks
        ) {
            BasketScreen(scaffoldState)
        }
        composable(
            route = PurchaseHistoryNav.route,
            deepLinks = PurchaseHistoryNav.deepLinks
        ) {
            PurchaseHistoryScreen()
        }
        // TODO data class 를 넘겨주는 방법
//        composable(
//            NavigationRouteName.CATEGORY + "/{category}",
//            arguments = listOf(navArgument("category") { type = NavType.StringType })
//        ) {
//            val categoryString = it.arguments?.getString("category")
//            val category = Gson().fromJson(categoryString, Category::class.java)
//            if (category != null) {
//                CategoryScreen(navHostController = navController, category = category)
//            }
//        }
        // 개선 (하드 코딩을 지양하는 방향으로)
        composable(
            route = CategoryNav.routeWithArgName(),
            arguments = CategoryNav.arguments,
            deepLinks = CategoryNav.deepLinks
        ) {
            val category = CategoryNav.findArgument(it)
            if (category != null) {
                CategoryScreen(
                    navHostController = navController,
                    category = category
                )
            }
        }
        // 일반적인 string 전달
//        composable(
//            route = NavigationRouteName.PRODUCT_DETAIL + "/{product}",
//            arguments = listOf(navArgument("product") { type = NavType.StringType })
//        ) {
//            val productString = it.arguments?.getString("product")
//            if (productString != null) {
//                ProductDetailScreen(productString)
//            }
//        }
//        composable(NavigationRouteName.SEARCH) {
//            SearchScreen(navController)
//        }

        // 개선
        composable(
            route = ProductDetailNav.routeWithArgName(),
            arguments = ProductDetailNav.arguments,
            deepLinks = ProductDetailNav.deepLinks
        ) {
            val productString = ProductDetailNav.findArgument(it)
            if (productString != null) {
                ProductDetailScreen(productString)
            }
        }
        composable(SearchNav.route) {
            SearchScreen(navController)
        }
    }
}

fun popupSnackBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    message: String,
    onDismissCallback: () -> Unit = {}
) {
    scope.launch {
        scaffoldState.snackbarHostState.showSnackbar(message = message)
        onDismissCallback.invoke()
    }
}