package camera

// Возвращает Android Context, если доступен (на iOS всегда null)
expect fun androidExtractContext(preview: Any?): Any?

// Возвращает кадр (Bitmap) из Android PreviewView, если готов (на iOS всегда null)
expect suspend fun androidCaptureBitmap(preview: Any?): Any?