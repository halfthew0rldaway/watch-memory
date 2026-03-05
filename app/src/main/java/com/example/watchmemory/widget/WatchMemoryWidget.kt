package com.example.watchmemory.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.watchmemory.MainActivity
import com.example.watchmemory.R
import com.example.watchmemory.data.ShowEntity
import com.example.watchmemory.data.WatchMemoryDatabase
import com.example.watchmemory.data.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

private val ShowIdKey = ActionParameters.Key<Long>("show_id")

class WatchMemoryWidget : GlanceAppWidget() {

    // Enhanced responsive size set
    override val sizeMode = SizeMode.Responsive(
        setOf(
            DpSize(60.dp, 60.dp),   // Tiny
            DpSize(120.dp, 120.dp), // Small square
            DpSize(240.dp, 100.dp), // Wide
            DpSize(300.dp, 240.dp), // Large
            DpSize(400.dp, 400.dp)  // Full screen
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val db = WatchMemoryDatabase.getInstance(context)
        val userPrefs = UserPreferences(context)
        
        // Use a broader query or ensure it's not restricted
        val shows = withContext(Dispatchers.IO) {
            db.showDao().getAllShowsList()
        }
        val userName = withContext(Dispatchers.IO) {
            userPrefs.userName.first()
        }

        provideContent {
            GlanceTheme {
                WidgetContent(shows, userName)
            }
        }
    }

    @Composable
    private fun WidgetContent(shows: List<ShowEntity>, userName: String) {
        val size = LocalSize.current
        val isVerySmall = size.width < 120.dp
        val isShort = size.height < 120.dp

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(R.color.widget_background))
                .padding(if (isVerySmall) 4.dp else 12.dp)
        ) {
            // Header
            if (!isVerySmall || size.width > 80.dp) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = GlanceModifier.defaultWeight()) {
                        Text(
                            text = userName.uppercase(),
                            style = TextStyle(
                                color = ColorProvider(R.color.widget_text_secondary),
                                fontSize = if (isVerySmall) 8.sp else 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        if (!isShort) {
                            Text(
                                text = "FEED",
                                style = TextStyle(
                                    color = ColorProvider(R.color.widget_primary),
                                    fontSize = if (isVerySmall) 12.sp else 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    
                    if (size.width > 150.dp) {
                        Box(
                            modifier = GlanceModifier
                                .background(ColorProvider(R.color.widget_accent))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${shows.size} SHOWS",
                                style = TextStyle(
                                    color = ColorProvider(R.color.widget_border),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = GlanceModifier.height(if (isShort) 4.dp else 12.dp))
            }

            if (shows.isEmpty()) {
                EmptyWidgetState()
            } else {
                LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
                    items(shows) { show ->
                        WidgetShowItem(show, size)
                    }
                    item {
                        AddShowButton()
                    }
                }
            }
        }
    }

    @Composable
    private fun EmptyWidgetState() {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(R.color.widget_surface))
                .clickable(actionStartActivity<MainActivity>()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+ ADD",
                style = TextStyle(
                    color = ColorProvider(R.color.widget_primary),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }

    @Composable
    private fun AddShowButton() {
         Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 2.dp)
                .background(ColorProvider(R.color.widget_primary))
                .clickable(actionStartActivity<MainActivity>()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+ TRACK MORE",
                style = TextStyle(
                    color = ColorProvider(R.color.widget_surface),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = GlanceModifier.padding(vertical = 6.dp)
            )
        }
    }

    @Composable
    private fun WidgetShowItem(show: ShowEntity, widgetSize: DpSize) {
        val itemColorRes = when (kotlin.math.abs(show.id.toInt()) % 3) {
            0 -> R.color.widget_item_1
            1 -> R.color.widget_item_2
            else -> R.color.widget_item_3
        }
        
        val isNarrow = widgetSize.width < 180.dp
        
        // Neobrutal item without complex nested boxes that might clip
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .background(ColorProvider(itemColorRes))
                    .padding(8.dp)
                    .clickable(actionStartActivity<MainActivity>()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = GlanceModifier.defaultWeight()) {
                    Text(
                        text = show.title.uppercase(),
                        style = TextStyle(
                            color = ColorProvider(R.color.widget_text_primary),
                            fontSize = if (isNarrow) 11.sp else 13.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "EP ${show.episode}",
                        style = TextStyle(
                            color = ColorProvider(R.color.widget_primary),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                Spacer(modifier = GlanceModifier.width(4.dp))
                
                // +1 Mini Button
                Box(
                    modifier = GlanceModifier
                        .size(width = 40.dp, height = 32.dp)
                        .background(ColorProvider(R.color.widget_primary))
                        .clickable(
                            actionRunCallback<IncrementActionCallback>(
                                actionParametersOf(ShowIdKey to show.id)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+1",
                        style = TextStyle(
                            color = ColorProvider(R.color.widget_surface),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            // Simple shadow line
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(ColorProvider(R.color.widget_shadow))
            ) {}
        }
    }
}

class IncrementActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val showId = parameters[ShowIdKey] ?: return
        val db = WatchMemoryDatabase.getInstance(context)
        
        withContext(Dispatchers.IO) {
            val show = db.showDao().getShowById(showId)
            if (show != null) {
                db.showDao().updateShow(
                    show.copy(
                        episode = show.episode + 1,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }
        }
        
        WatchMemoryWidget().updateAll(context)
    }
}
