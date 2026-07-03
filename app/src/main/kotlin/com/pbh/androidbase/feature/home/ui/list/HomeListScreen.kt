package com.pbh.androidbase.feature.home.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pbh.androidbase.R
import com.pbh.androidbase.core.designsystem.AppTheme
import com.pbh.androidbase.core.designsystem.components.AppTextButton
import com.pbh.androidbase.core.ui.BaseScreen
import com.pbh.androidbase.domain.entity.Item

@Composable
fun HomeListScreen(
    onLogout: () -> Unit,
    onOpenDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    BaseScreen(
        viewModel = viewModel,
        title = stringResource(R.string.home_title),
        actions = {
            AppTextButton(
                text = stringResource(R.string.home_action_refresh),
                onClick = viewModel::refresh,
            )
            AppTextButton(
                text = stringResource(R.string.home_action_logout),
                onClick = viewModel::logout,
            )
        },
        onEffect = { effect ->
            when (effect) {
                HomeListEffect.LoggedOut -> onLogout()
                is HomeListEffect.ShowMessage -> Unit // shown by BaseScreen
            }
        },
    ) { state, padding ->
        when (state) {
            HomeListUiState.Loading -> {
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
            is HomeListUiState.Content -> {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(AppTheme.spacing.large),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium),
                ) {
                    items(state.items, key = { it.id }) { item ->
                        ItemRow(item = item, onClick = { onOpenDetail(item.id) })
                    }
                }
            }
            is HomeListUiState.Error -> {
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

@Composable
private fun ItemRow(
    item: Item,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.decoration.cardElevation),
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small),
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f),
                    style = AppTheme.typography.titleMedium,
                )
            }
            Text(
                text = item.description,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
