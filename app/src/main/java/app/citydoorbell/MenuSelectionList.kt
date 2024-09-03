package app.citydoorbell

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.citydoorbell.content.model.CategoryStyleType
import app.citydoorbell.content.model.PostStyleType

@Composable
fun MenuSelectionList(
    isCategoryList: Boolean,
    mainViewModel: MainViewModel,
    action: (Pair<PostStyleType, CategoryStyleType>) -> Unit
) {
    var selectedMenuItem = mainViewModel.postStyle.collectAsState().value
    var selectedCategoryItem = mainViewModel.categoryStyle.collectAsState().value

    if (isCategoryList) {
        LazyRow {
            CategoryStyleType.entries.forEach { type ->
                item {
                    CategoryItem(type = type, selectedMenuItem = selectedCategoryItem, mainViewModel = mainViewModel) {
                        selectedCategoryItem = type
                        mainViewModel.updateSelectCategoryStyle(type)
                        action(Pair(selectedMenuItem, selectedCategoryItem))
                    }
                }
            }
        }
    } else {
        LazyRow {
            PostStyleType.entries.subList(0, PostStyleType.entries.size - 1).forEach { type ->
                item {
                    CategoryItem(type = type, selectedMenuItem = selectedMenuItem, mainViewModel = mainViewModel) {
                        selectedMenuItem = type
                        mainViewModel.updateSelectedPostStyle(type)
                        action(Pair(selectedMenuItem, selectedCategoryItem))
                    }
                }
            }
        }
    }
}
