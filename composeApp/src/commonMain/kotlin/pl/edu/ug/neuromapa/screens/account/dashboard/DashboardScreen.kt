package pl.edu.ug.neuromapa.screens.account.dashboard

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.screens.home.settings.widePadding
import pl.edu.ug.neuromapa.screens.home.settings.wideSpacing

@Composable
fun DashboardScreen(
    headerTitle: String,
    userProfileImage: DrawableResource,
    scrollState: ScrollState = rememberScrollState()
) {

    // Wrapper of the whole screen
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        // One more wrapper...
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)  // Make the screen scrollable
        ) {

            Header(
                title = headerTitle,
                userProfileImage = userProfileImage,
                showProfileTopRightCorner = false,
                roundBottomCorners = true,
                modifier = Modifier,
            )

            // Body (main content)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            ) {

                /*
                 TODO:
                  Here go the other UI elements such as:
                  - user's pfp (
                            for new users (by default) show a no-pfp picture.
                            + button to edit.
                            )
                  - user's name (
                            for new users (by default) generate something like "User*****" where ***** = some id used in Supabase?
                            + button to edit.
                            )
                  - user's age (
                            by default should be empty.
                            + button to edit.
                            )
                  - "settings" button

                  TODO:
                   !!! VLAD !!!
                   Your task here will be to just add some simple Text() here and check if the
                   user data is shown correctly. You should NOT be doing fancy UI here - it's TARAS's job. Instead, add
                   very simple buttons and texts just to check if the sign in/up works correctly.
                   To be more sure of whether you did everything correctly, you can contact TARAS and/or ARTEM
                   and ask to quickly fix the favorites screen which will allows us to
                   see if we can see only this specific user's favorites or somebody else's too.
                   By the same logic TARAS can quickly then finish crowdsourcing form and check WHO is sending the
                   form.

                */

            }

        }

    }

}