package com.watchhawthornestereo.storage

import java.time.LocalDateTime

trait StorageUtils {
  def now: String = LocalDateTime.now().toString // use time as filename to keep multiple copies
}
