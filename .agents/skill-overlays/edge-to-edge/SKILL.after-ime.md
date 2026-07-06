- In this template, screens usually render through `BaseScreen`, which passes
  `AppScaffold` padding into feature content. For centered form screens with
  `AppTextField` and no persistent footer, keep `adjustResize` on the hosting
  Activity and make the form container scrollable: apply scaffold padding,
  consume that padding, apply `imePadding()`, then call
  `verticalScroll(rememberScrollState())`.
- For screens with a persistent footer, bottom action bar, or bottom navigation,
  do not apply IME padding to the whole scaffold or fixed bottom view. Keep the
  body as the scrollable IME-aware area, keep the bottom view as a sibling, and
  include the bottom view height/inset in the body bottom padding so fields can
  scroll above it without pushing the fixed bottom UI into the content.
