package app.citydoorbell.post

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.citydoorbell.MainNavigation
import app.citydoorbell.MainViewModel
import app.citydoorbell.R
import app.citydoorbell.content.ui.IconButtonViewModel
import app.citydoorbell.content.ui.PrimaryIconButton
import app.citydoorbell.content.ui.darkModeColors
import app.citydoorbell.content.ui.lightModeColors


@Composable
fun TextEntryBackground(
    mainViewModel: MainViewModel,
    screenTitle: String,
    navController: NavController,
    navDestination: String,
    text: String,
    action: ((String) -> Unit)?,
    cat: String,
    content: @Composable () -> Unit
) {

    // FocusRequester for managing focus
    val focusRequester = remember { FocusRequester() }
    // LocalSoftwareKeyboardController for managing keyboard visibility
    val keyboardController = LocalSoftwareKeyboardController.current
    // State to manage focus
    var isFocused by remember { mutableStateOf(false) }
    var isTextFieldFocused by remember { mutableStateOf(false) }


    var messageText by remember { mutableStateOf(text) }

    Scaffold { innerPadding: PaddingValues ->
        val bottomPadding = innerPadding.calculateBottomPadding()
        Box(modifier = Modifier
            .background(mainViewModel.appColors.collectAsState().value.backgroundColor)
            .fillMaxSize()
            .onFocusChanged {
                if (!it.isFocused) {
                    keyboardController?.hide()
                }
            }
            .clickable {
                if (isTextFieldFocused) {
                    keyboardController?.hide()
                    messageText = ""
                }
            }
            .padding(bottom = if (screenTitle == "viewComments") 0.dp else bottomPadding),
            contentAlignment = Alignment.BottomEnd) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp) // Adjust this to match the height of the text field
            ) {
                BackNavigationHeader(screenTitle = navDestination, navController = navController, navDestination = navDestination, mainViewModel) {
                    messageText = ""
                }
                Text(
                    text = cat,
                    fontSize = 19.sp,
                    fontFamily = FontFamily.Serif,
                    color = mainViewModel.appColors.collectAsState().value.textColor,
                    fontWeight = FontWeight.W300,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier
                        .padding(14.dp)
                )
                Spacer(modifier = Modifier.weight(1f)) // Takes up remaining space to push the content up
                content() // Screen Content
            }

            if (screenTitle == "viewComments") {
                PrimaryIconButton(IconButtonViewModel(true, mainViewModel), 42.dp, R.mipmap.sendicon) {
                    val id = "sendCommentPost"
                    val title = "Post Comment"
                    navController.navigate("postingScreen/${id}/${title}")
                    messageText = ""
                }
            }

            if (screenTitle == "sendPost" || screenTitle == "sendCommentPost") {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = {
                        messageText = it
                        action?.invoke(messageText)
                    },
                    placeholder = { Text("Type a message") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            // Handle send action
                            if (isTextFieldFocused) {
                                keyboardController?.hide()
                            }
                        },
                        onDone = {
                            if (isTextFieldFocused) {
                                keyboardController?.hide()
                            }
                        }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isTextFieldFocused = focusState.isFocused
                            isFocused = focusState.isFocused
                            if (!focusState.isFocused) {
                                keyboardController?.hide()
                                messageText = ""
                            }
                        }
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(
                            1.18.dp,
                            mainViewModel.appColors.collectAsState().value.secondaryTextColor
                        )
                    ,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        errorContainerColor = Color.Transparent,
                        cursorColor = Color.Black
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview_4() {

    val mainViewModel = MainViewModel(LocalContext.current)
    val navController = rememberNavController()
    val startDestination = "landingScreen"
    // Launch App
    MainNavigation(
        navController,
        mainViewModel,
        LocalContext.current,
        Application(),
        startDestination
    )
}