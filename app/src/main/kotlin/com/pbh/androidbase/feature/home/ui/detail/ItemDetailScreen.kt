package com.pbh.androidbase.feature.home.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pbh.androidbase.R
import com.pbh.androidbase.core.designsystem.AppTheme
import com.pbh.androidbase.core.ui.BaseScreen

/** Item detail screen that loads and renders one cached item by [itemId]. */
@Composable
fun ItemDetailScreen(
    itemId: String,
    viewModel: ItemDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(itemId) {
        viewModel.load(itemId)
    }

    BaseScreen(
        viewModel = viewModel,
        title = stringResource(R.string.item_detail_title),
    ) { state, padding ->
        when (state) {
            ItemDetailUiState.Loading -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(padding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()
                }
            }
            is ItemDetailUiState.Content -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(AppTheme.spacing.screen),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium),
                ) {
                    Text(
                        text = state.item.title,
                        style = AppTheme.typography.headlineSmall,
                    )
                    Text(
                        text = state.item.description,
                        style = AppTheme.typography.bodyLarge,
                    )
                }
            }
            is ItemDetailUiState.Error -> {
                Text(
                    text = state.message.asString(),
                    modifier =
                        Modifier
                            .padding(padding)
                            .padding(AppTheme.spacing.large),
                    color = AppTheme.colors.error,
                )
            }
        }
    }
}
